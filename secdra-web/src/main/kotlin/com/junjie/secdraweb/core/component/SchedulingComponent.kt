package com.junjie.secdraweb.core.component

import com.junjie.secdraservice.service.CommentMessageService
import com.junjie.secdraservice.service.FollowMessageService
import com.junjie.secdraservice.service.ReplyMessageService
import com.junjie.secdraservice.service.SystemMessageService
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

/**
 * @author fjj
 * 定时任务服务
 */
@Component
class SchedulingComponent(private val commentMessageService: CommentMessageService,
                          private val replyMessageService: ReplyMessageService,
                          private val followMessageService: FollowMessageService,
                          private val systemMessageService: SystemMessageService) {
    //这里有个bug，会执行了两次
    @Scheduled(cron = "0 0 3/23 * * ?")
//    @Scheduled(cron = "0/5 * * * * ? ")
    fun removeMessageByMonthAgo() {
        try {
            commentMessageService.deleteByMonthAgo()
        } catch (e: Exception) {
            println(e)
        }
        try {
            replyMessageService.deleteByMonthAgo()
        } catch (e: Exception) {
            println(e)
        }
        try {
            followMessageService.deleteByMonthAgo()
        } catch (e: Exception) {
            println(e)
        }
        try {
            systemMessageService.deleteByMonthAgo()
        } catch (e: Exception) {
            println(e)
        }
    }
}