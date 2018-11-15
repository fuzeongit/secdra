package com.junjie.secdraweb.controller

import com.junjie.secdracore.annotations.CurrentUserId
import com.junjie.secdracore.util.IpUtil
import com.junjie.secdraservice.dao.IStatisticalDao
import com.junjie.secdraservice.model.Statistical
import com.junjie.secdraservice.service.IDrawService
import com.junjie.secdraservice.service.IStatisticalService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest


/**
 * @author fjj
 * 统计的控制器
 */
@RestController
@RequestMapping("/statistical")
class StatisticalController(private val statisticalService: IStatisticalService) {
    @PostMapping("/save")
    fun save(@CurrentUserId userId: String,path:String,request: HttpServletRequest): Statistical {
        val statistical = Statistical()
        statistical.userId = userId
        statistical.ip = IpUtil.getIpAddress(request)
        statistical.path = path
        return statisticalService.save(statistical)
    }
}