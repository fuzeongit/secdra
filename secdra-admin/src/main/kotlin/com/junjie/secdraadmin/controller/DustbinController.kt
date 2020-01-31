package com.junjie.secdraadmin.controller

import com.junjie.secdracore.annotations.RestfulPack
import com.junjie.secdraqiniu.core.component.QiniuConfig
import com.junjie.secdraqiniu.service.BucketService
import com.junjie.secdraservice.service.PictureService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("dustbin")
class DustbinController(private val qiniuConfig: QiniuConfig,
                        private val bucketService: BucketService,
                        private val pictureService: PictureService) {
    /**
     * 还原
     */
    @PostMapping("reduction")
    @RestfulPack
    fun reduction(@RequestParam("idList") idList: Array<String>): Boolean {
        for (id in idList) {
            pictureService.reduction(id)
        }
        return true
    }

    /**
     * 清空回收站
     * 物理删除
     */
    @PostMapping("delete")
    @RestfulPack
    fun delete(@RequestParam("idList") idList: Array<String>): Boolean {
        for (id in idList) {
            val picture = pictureService.get(id)
            bucketService.move(picture.url, qiniuConfig.qiniuTempBucket, qiniuConfig.qiniuBucket)
            pictureService.delete(id)
        }
        return true
    }
}