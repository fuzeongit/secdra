package com.junjie.secdraweb.controller

import com.junjie.secdracore.model.Result
import com.junjie.secdraservice.model.User
import com.junjie.secdraservice.qiniu.UploadAuth
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/qiniu")
class QiniuController() {
    var ACCESS_KEY = "_I-tvI9XSNEzwjzeEWz3KxfBQwL9mk3GOcDbNKST"
    var SECRET_KEY = "DMEfYejDMunmcgz07VfUraR9OhEvRX_ekY58H-2w"
    @GetMapping("/getUploadToken")
    fun get():  Result<String> {
        val uploadAuth = UploadAuth.create(ACCESS_KEY, SECRET_KEY)
        return Result<String>(200,"", uploadAuth.uploadToken("images").toString());
    }
}