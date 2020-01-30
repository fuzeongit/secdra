package com.junjie.secdraqiniu.service

import com.junjie.secdraqiniu.core.component.QiniuConfig
import com.junjie.secdraqiniu.core.util.Auth
import com.junjie.secdraqiniu.model.QiniuImageInfo
import com.qiniu.util.UrlSafeBase64
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.client.RestTemplate


/**
 * @author fjj
 * 七牛云的服务
 */
@Service
class BucketService(private val qiniuConfig: QiniuConfig) {
    /**
     * 移动目标
     * @param url 路径
     * @param bucket 目标空间
     * @param sourceBucket 源空间
     */
    fun move(url: String, bucket: String, sourceBucket: String? = qiniuConfig.qiniuTempBucket): Boolean {
        val sourceNameEncodeBase64 = UrlSafeBase64.encodeToString("$sourceBucket:$url")!!
        val nameEncodeBase64 = UrlSafeBase64.encodeToString("$bucket:$url")!!

        val qiniuUrl = "http://rs.qiniu.com/move/$sourceNameEncodeBase64/$nameEncodeBase64"

        val auth = Auth.create(qiniuConfig.qiniuAccessKey, qiniuConfig.qiniuSecretKey)
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
    fun getImageInfo(url: String, bucketUrl: String? = qiniuConfig.qiniuBucketUrl): QiniuImageInfo? {
        val client = RestTemplate()
        return client.getForObject("$bucketUrl/$url?imageInfo", QiniuImageInfo::class.java)
    }
}