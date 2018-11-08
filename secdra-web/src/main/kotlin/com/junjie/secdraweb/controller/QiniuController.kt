package com.junjie.secdraweb.controller

import com.junjie.secdracore.annotations.Auth
import com.junjie.secdracore.annotations.CurrentUserId
import com.junjie.secdracore.model.Result
import com.junjie.secdraservice.dao.IDrawDao
import com.junjie.secdraweb.base.component.BaseConfig
import com.junjie.secdraweb.base.qiniu.QiniuImageInfo
import com.qiniu.util.UrlSafeBase64
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestTemplate
import com.junjie.secdraweb.base.qiniu.Auth as QiniuAuth


@RestController
@RequestMapping("/qiniu")
class QiniuController(private val baseConfig: BaseConfig, private val drawDao: IDrawDao) {
    @GetMapping("/getUploadToken")
    fun get(): Result<String> {
        val auth = QiniuAuth.create(baseConfig.qiniuAccessKey, baseConfig.qiniuSecretKey)
        return Result(200, "", auth.uploadToken("images").toString());
    }

    @Auth
    @PostMapping("move")
    fun move(
            @CurrentUserId userId: String,
            name: String): Boolean {
        //空间名前缀
        val sourceBucket = baseConfig.qiniuTempBucket
        val bucket = baseConfig.qiniuBucket
        val sourceNameEncodeBase64 = UrlSafeBase64.encodeToString("$sourceBucket:$name")
        val nameEncodeBase64 = UrlSafeBase64.encodeToString("$bucket:$name")

        val url = "http://rs.qiniu.com/move/$sourceNameEncodeBase64/$nameEncodeBase64";
        val auth = QiniuAuth.create(baseConfig.qiniuAccessKey, baseConfig.qiniuSecretKey);
        val authorizationMap = auth.authorization(url, null, MediaType.APPLICATION_FORM_URLENCODED_VALUE);
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
        //  执行HTTP请求
        try {
            client.exchange(url, HttpMethod.POST, requestEntity, String::class.java)
            return true
        } catch (e: Exception) {
            throw e
        }
    }

    @GetMapping("getImageInfo")
    fun getImageInfo(): Boolean {
        val all = drawDao.findAll()
        val client = RestTemplate()
        for (item in all) {
            if (item.width == 0) {
                val qiniuImageInfo = client.getForObject("http://ph9jy186h.bkt.clouddn.com/${item.url}?imageInfo", QiniuImageInfo::class.java)
                item.height = qiniuImageInfo!!.height
                item.width = qiniuImageInfo.width
                drawDao.save(item)
            }
        }
        return true
    }

    @GetMapping("testGetImageInfo")
    fun testGetImageInfo(): QiniuImageInfo? {
        val client = RestTemplate()
        val qiniuImageInfo = client.getForObject("http://ph9jy186h.bkt.clouddn.com/32740714_p0.jpg?imageInfo", QiniuImageInfo::class.java)
        println(qiniuImageInfo!!.size)
        return qiniuImageInfo
    }
}


