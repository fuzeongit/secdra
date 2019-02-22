package com.junjie.secdraweb.controller

import com.junjie.secdracore.annotations.Auth
import com.junjie.secdracore.annotations.CurrentUserId
import com.junjie.secdraservice.model.Notify
import com.junjie.secdraservice.service.INotifyService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("notify")
class NotifyController(private val notifyService: INotifyService) {
    @Auth
    @GetMapping("/count")
    fun count(@CurrentUserId userId: String): Long {
        return notifyService.countUnread(userId)
    }

    @Auth
    @GetMapping("/listUnread")
    fun listUnread(@CurrentUserId userId: String): List<Notify> {
        return notifyService.listUnread(userId)
    }

    @Auth
    @GetMapping("/list")
    fun list(@CurrentUserId userId: String): List<Notify> {
        return notifyService.list(userId)
    }
}
