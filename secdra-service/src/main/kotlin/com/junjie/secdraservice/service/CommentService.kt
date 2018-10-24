package com.junjie.secdraservice.service

import com.junjie.secdraservice.model.Comment
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class CommentService : ICommentService {
    override fun save(userId: String, drawId: String, content: String): Comment {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun pagingUnread(userId: String, pageable: Pageable): Page<Comment> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun pagingByDrawId(drawId: String, pageable: Pageable): Page<Comment> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}