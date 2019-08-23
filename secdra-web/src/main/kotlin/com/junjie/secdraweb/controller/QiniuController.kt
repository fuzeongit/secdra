package com.junjie.secdraweb.controller

import com.junjie.secdracore.annotations.Auth
import com.junjie.secdracore.annotations.CurrentUserId
import com.junjie.secdracore.annotations.RestfulPack
import com.junjie.secdracore.model.Result
import com.junjie.secdraservice.dao.DrawDAO
import com.junjie.secdraweb.base.component.BaseConfig
import com.junjie.secdraweb.model.QiniuImageInfo
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
@RequestMapping("qiniu")
class QiniuController(private val baseConfig: BaseConfig, private val drawDAO: DrawDAO) {
    @GetMapping("/getUploadToken")
    @RestfulPack
    fun get(): Result<String> {
        val auth = QiniuAuth.create(baseConfig.qiniuAccessKey, baseConfig.qiniuSecretKey)
        return Result(200, "", auth.uploadToken(baseConfig.qiniuTempBucket))
    }
}


