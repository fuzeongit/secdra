package com.junjie.secdraweb.controller

import com.junjie.secdracore.annotations.RestfulPack
import com.junjie.secdracore.model.Result
import com.junjie.secdraservice.dao.DrawDAO
import com.junjie.secdraweb.base.component.BaseConfig
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
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


