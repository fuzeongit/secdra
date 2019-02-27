package com.junjie.secdraweb.base.component

import com.junjie.secdraservice.service.ICommentMessageService
import com.junjie.secdraservice.service.IFollowMessageService
import com.junjie.secdraservice.service.IReplyMessageService
import com.junjie.secdraservice.service.ISystemMessageService
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

/**
 * @author fjj
 * 定时任务服务
 */
@Component
@EnableScheduling
class SchedulingComponent(private val commentMessageService: ICommentMessageService,
                          private val replyMessageService: IReplyMessageService, private val followMessageService: IFollowMessageService,
                          private val systemMessageService: ISystemMessageService) {
    @Scheduled(cron = "0/10 * * * * ?")
    fun removeMessageByMonthAgo() {
        try {
            commentMessageService.deleteByMonthAgo()
        }catch (e:Exception){
            println(e)
        }
        try {
            replyMessageService.deleteByMonthAgo()
        }catch (e:Exception){
            println(e)
        }
        try {
            followMessageService.deleteByMonthAgo()
        }catch (e:Exception){
            println(e)
        }
        try {
            systemMessageService.deleteByMonthAgo()
        }catch (e:Exception){
            println(e)
        }
    }
}