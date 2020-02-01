package com.junjie.secdraweb.core.configurer

import com.junjie.secdraservice.service.CommentMessageService
import com.junjie.secdraservice.service.FollowMessageService
import com.junjie.secdraservice.service.ReplyMessageService
import com.junjie.secdraservice.service.SystemMessageService
import com.junjie.secdraweb.core.component.SchedulingComponent
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * 定时器的配置
 * @author fjj
 */
@Configuration
class SchedulingConfigurer(
        private val commentMessageService: CommentMessageService,
        private val replyMessageService: ReplyMessageService,
        private val followMessageService: FollowMessageService,
        private val systemMessageService: SystemMessageService) {

    @Bean
    internal fun schedulingComponent(): SchedulingComponent {
        return SchedulingComponent(
                commentMessageService,
                replyMessageService,
                followMessageService,
                systemMessageService)
    }
}