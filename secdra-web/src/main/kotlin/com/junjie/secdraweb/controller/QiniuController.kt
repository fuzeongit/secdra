package com.junjie.secdraweb.controller

import com.junjie.secdracore.annotations.RestfulPack
import com.junjie.secdracore.model.Result
import com.junjie.secdradata.database.primary.dao.PictureDAO
import com.junjie.secdraqiniu.core.component.QiniuConfig
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import com.junjie.secdraqiniu.core.util.Auth as QiniuAuth


@RestController
@RequestMapping("qiniu")
class QiniuController(private val qiniuConfig: QiniuConfig, private val pictureDAO: PictureDAO) {
    @GetMapping("/getUploadToken")
    @RestfulPack
    fun get(): Result<String> {
        val auth = QiniuAuth.create(qiniuConfig.qiniuAccessKey, qiniuConfig.qiniuSecretKey)
        return Result(200, "", auth.uploadToken(qiniuConfig.qiniuTempBucket))
    }
}


