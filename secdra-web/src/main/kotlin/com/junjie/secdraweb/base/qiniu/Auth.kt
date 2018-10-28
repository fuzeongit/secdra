package com.junjie.secdraweb.base.qiniu

import com.qiniu.http.Client
import com.qiniu.util.*

import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import java.net.URI
import java.security.GeneralSecurityException

class Auth(private val accessKey: String, private val secretKey: SecretKeySpec) {

    companion object {
        fun create(accessKey: String, secretKey: String): Auth {
            if (StringUtils.isNullOrEmpty(accessKey) || StringUtils.isNullOrEmpty(secretKey)) {
                throw IllegalArgumentException("empty key")
            }
            val sk = StringUtils.utf8Bytes(secretKey)
            val secretKeySpec = SecretKeySpec(sk, "HmacSHA1")
            return Auth(accessKey, secretKeySpec)
        }
    }
    /**
     * 上传策略
     * 参考文档：[上传策略](https://developer.qiniu.com/kodo/manual/put-policy)
     */
    private val policyFields = arrayOf(
            "callbackUrl", "callbackBody", "callbackHost", "callbackBodyType", "callbackFetchKey",
            "returnUrl", "returnBody", "endUser", "saveKey", "insertOnly", "isPrefixalScope",
            "detectMime", "mimeLimit", "fsizeLimit", "fsizeMin", "persistentOps", "persistentNotifyUrl",
            "persistentPipeline", "deleteAfterDays", "fileType")

    private val deprecatedPolicyFields = arrayOf("asyncOps")


    private fun copyPolicy(policy: StringMap, originPolicy: StringMap?, strict: Boolean) {
        if (originPolicy == null) {
            return
        }
        originPolicy.forEach(StringMap.Consumer { key, value ->
            if (StringUtils.inStringArray(key, deprecatedPolicyFields)) {
                throw IllegalArgumentException("$key is deprecated!")
            }
            if (!strict || StringUtils.inStringArray(key, policyFields)) {
                policy.put(key, value)
            }
        })
    }

    private fun createMac(): Mac {
        val mac: Mac
        try {
            mac = javax.crypto.Mac.getInstance("HmacSHA1")
            mac.init(secretKey)
        } catch (e: GeneralSecurityException) {
            e.printStackTrace()
            throw IllegalArgumentException(e)
        }

        return mac
    }

    private fun sign(data: ByteArray): String {
        val mac = createMac()
        val encodedSign = UrlSafeBase64.encodeToString(mac.doFinal(data))
        return this.accessKey + ":" + encodedSign
    }

    fun sign(data: String): String {
        return sign(StringUtils.utf8Bytes(data))
    }

    private fun signWithData(data: ByteArray): String {
        val s = UrlSafeBase64.encodeToString(data)
        return sign(StringUtils.utf8Bytes(s)) + ":" + s
    }

    fun signWithData(data: String): String {
        return signWithData(StringUtils.utf8Bytes(data))
    }

    /**
     * 生成HTTP请求签名字符串
     *
     * @param urlString
     * @param body
     * @param contentType
     * @return
     */
    private fun signRequest(urlString: String, body: ByteArray?, contentType: String?): String {
        val uri = URI.create(urlString)
        val path = uri.rawPath
        val query = uri.rawQuery

        val mac = createMac()

        mac.update(StringUtils.utf8Bytes(path))

        if (query != null && query.isNotEmpty()) {
            mac.update('?'.toByte())
            mac.update(StringUtils.utf8Bytes(query))
        }
        mac.update('\n'.toByte())
        if (body != null && Client.FormMime.equals(contentType, true)) {
            mac.update(body)
        }

        val digest = UrlSafeBase64.encodeToString(mac.doFinal())

        return this.accessKey + ":" + digest
    }

    /**
     * 验证回调签名是否正确
     *
     * @param originAuthorization 待验证签名字符串，以 "QBox "作为起始字符
     * @param url                 回调地址
     * @param body                回调请求体。原始请求体，不要解析后再封装成新的请求体--可能导致签名不一致。
     * @param contentType         回调ContentType
     * @return
     */
    fun isValidCallback(originAuthorization: String, url: String, body: ByteArray, contentType: String): Boolean {
        val authorization = "QBox " + signRequest(url, body, contentType)
        return authorization == originAuthorization
    }

    /**
     * 下载签名
     *
     * @param baseUrl 待签名文件url，如 http://img.domain.com/u/3.jpg 、
     * http://img.domain.com/u/3.jpg?imageView2/1/w/120
     * @return
     */
    fun privateDownloadUrl(baseUrl: String): String {
        return privateDownloadUrl(baseUrl, 3600)
    }

    /**
     * 下载签名
     *
     * @param baseUrl 待签名文件url，如 http://img.domain.com/u/3.jpg 、
     * http://img.domain.com/u/3.jpg?imageView2/1/w/120
     * @param expires 有效时长，单位秒。默认3600s
     * @return
     */
    private fun privateDownloadUrl(baseUrl: String, expires: Long): String {
        val deadline = System.currentTimeMillis() / 1000 + expires
        return privateDownloadUrlWithDeadline(baseUrl, deadline)
    }

    private fun privateDownloadUrlWithDeadline(baseUrl: String, deadline: Long): String {
        val b = StringBuilder()
        b.append(baseUrl)
        val pos = baseUrl.indexOf("?")
        if (pos > 0) {
            b.append("&e=")
        } else {
            b.append("?e=")
        }
        b.append(deadline)
        val token = sign(StringUtils.utf8Bytes(b.toString()))
        b.append("&token=")
        b.append(token)
        return b.toString()
    }

    /**
     * scope = bucket
     * 一般情况下可通过此方法获取token
     *
     * @param bucket 空间名
     * @return 生成的上传token
     */
    fun uploadToken(bucket: String): String {
        return uploadToken(bucket, null, 3600, null, true)
    }

    /**
     * scope = bucket:key
     * 同名文件覆盖操作、只能上传指定key的文件可以可通过此方法获取token
     *
     * @param bucket 空间名
     * @param key    key，可为 null
     * @return 生成的上传token
     */
    fun uploadToken(bucket: String, key: String): String {
        return uploadToken(bucket, key, 3600, null, true)
    }

    /**
     * 生成上传token
     *
     * @param bucket  空间名
     * @param key     key，可为 null
     * @param expires 有效时长，单位秒
     * @param policy  上传策略的其它参数，如 new StringMap().put("endUser", "uid").putNotEmpty("returnBody", "")。
     * scope通过 bucket、key间接设置，deadline 通过 expires 间接设置
     * @return 生成的上传token
     */
    fun uploadToken(bucket: String, key: String, expires: Long, policy: StringMap): String {
        return uploadToken(bucket, key, expires, policy, true)
    }

    /**
     * 生成上传token
     *
     * @param bucket  空间名
     * @param key     key，可为 null
     * @param expires 有效时长，单位秒。默认3600s
     * @param policy  上传策略的其它参数，如 new StringMap().put("endUser", "uid").putNotEmpty("returnBody", "")。
     * scope通过 bucket、key间接设置，deadline 通过 expires 间接设置
     * @param strict  是否去除非限定的策略字段，默认true
     * @return 生成的上传token
     */
    private fun uploadToken(bucket: String, key: String?, expires: Long, policy: StringMap?, strict: Boolean): String {
        val deadline = System.currentTimeMillis() / 1000 + expires
        return uploadTokenWithDeadline(bucket, key, deadline, policy, strict)
    }

    private fun uploadTokenWithDeadline(bucket: String, key: String?, deadline: Long, policy: StringMap?, strict: Boolean): String {
        // TODO   UpHosts Global
        var scope = bucket
        if (key != null) {
            scope = "$bucket:$key"
        }
        val x = StringMap()
        copyPolicy(x, policy, strict)
        x.put("scope", scope)
        x.put("deadline", deadline)

        val s = Json.encode(x)
        return signWithData(StringUtils.utf8Bytes(s))
    }

    fun uploadTokenWithPolicy(obj: Any): String {
        val s = Json.encode(obj)
        return signWithData(StringUtils.utf8Bytes(s))
    }

    fun authorization(url: String, body: ByteArray?, contentType: String?): StringMap {
        val authorization = "QBox " + signRequest(url, body, contentType)
        return StringMap().put("Authorization", authorization)
    }

    fun authorization(url: String): StringMap {
        return authorization(url, null, null)
    }

    /**
     * 生成HTTP请求签名字符串
     *
     * @param urlString
     * @param body
     * @param contentType
     * @return
     */
    private fun signRequestV2(urlString: String, method: String, body: ByteArray?, contentType: String?): String {
        val uri = URI.create(urlString)

        val mac = createMac()
        val sb = StringBuilder()

        sb.append(String.format("%s %s", method, uri.path))
        if (uri.query != null) {
            sb.append(String.format("?%s", uri.query))
        }

        sb.append(String.format("\nHost: %s", uri.host))
        if (uri.port > 0) {
            sb.append(String.format(":%d", uri.port))
        }

        if (contentType != null) {
            sb.append(String.format("\nContent-Type: %s", contentType))
        }

        // body
        sb.append("\n\n")
        if (body != null && body.size > 0 && !StringUtils.isNullOrEmpty(contentType)) {
            if (contentType == Client.FormMime || contentType == Client.JsonMime) {
                sb.append(String(body))
            }
        }

        mac.update(StringUtils.utf8Bytes(sb.toString()))

        val digest = UrlSafeBase64.encodeToString(mac.doFinal())
        return this.accessKey + ":" + digest
    }

    private fun authorizationV2(url: String, method: String, body: ByteArray?, contentType: String?): StringMap {
        val authorization = "Qiniu " + signRequestV2(url, method, body, contentType)
        return StringMap().put("Authorization", authorization)
    }

    fun authorizationV2(url: String): StringMap {
        return authorizationV2(url, "GET", null, null)
    }

    //连麦 RoomToken
    @Throws(Exception::class)
    fun signRoomToken(roomAccess: String): String {
        val encodedRoomAcc = UrlSafeBase64.encodeToString(roomAccess)
        val sign = createMac().doFinal(encodedRoomAcc.toByteArray())
        val encodedSign = UrlSafeBase64.encodeToString(sign)
        return this.accessKey + ":" + encodedSign + ":" + encodedRoomAcc
    }
}