package com.junjie.secdraweb.service

import com.junjie.secdraweb.core.component.BaseConfig
import com.junjie.secdraweb.core.qiniu.Auth
import com.junjie.secdraweb.model.QiniuImageInfo
import com.qiniu.util.UrlSafeBase64
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.client.RestTemplate

/**
 * @author fjj
 * 七牛云的服务
 */
@Component
class QiniuComponent(private val baseConfig: BaseConfig) {
    /**
     * 移动目标
     */
    fun move(url: String, bucket: String, sourceBucket: String? = baseConfig.qiniuTempBucket): Boolean {
        val sourceNameEncodeBase64 = UrlSafeBase64.encodeToString("$sourceBucket:$url")!!
        val nameEncodeBase64 = UrlSafeBase64.encodeToString("$bucket:$url")!!

        val qiniuUrl = "http://rs.qiniu.com/move/$sourceNameEncodeBase64/$nameEncodeBase64"

        val auth = Auth.create(baseConfig.qiniuAccessKey, baseConfig.qiniuSecretKey)
        val authorizationMap = auth.authorization(qiniuUrl, null, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
        val authorization = authorizationMap.get("Authorization") as String

        val client = RestTemplate()
        val headers = HttpHeaders()
        val params = LinkedMultiValueMap<String, String>()
        //  请勿轻易改变此提交方式，大部分的情况下，提交方式都是表单提交
        headers.contentType = MediaType.APPLICATION_FORM_URLENCODED
        headers.add("Host", "rs.qiniu.com")
        headers.add("Accept", "*/*")
        headers.add("Content-Type", "application/json")
        headers.add("Authorization", authorization)
        val requestEntity = HttpEntity<MultiValueMap<String, String>>(params, headers)
        try {
            client.exchange(qiniuUrl, HttpMethod.POST, requestEntity, String::class.java)
            return true
        } catch (e: Exception) {
            throw e
        }
    }

    /**
     * 获取图片信息
     */
    fun getImageInfo(url: String, bucketUrl: String? = baseConfig.qiniuBucketUrl): QiniuImageInfo? {
        val client = RestTemplate()
        return client.getForObject<QiniuImageInfo>("$bucketUrl/$url?imageInfo", QiniuImageInfo::class.java)
    }
}