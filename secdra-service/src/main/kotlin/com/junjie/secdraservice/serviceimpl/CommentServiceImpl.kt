package com.junjie.secdraservice.serviceimpl

import com.junjie.secdradata.database.primary.dao.CommentDAO
import com.junjie.secdradata.database.primary.entity.Comment
import com.junjie.secdraservice.service.CommentService
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.cache.annotation.Caching
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service

@Service
class CommentServiceImpl(private val commentDAO: CommentDAO) : CommentService {
    @Caching(evict = [
        CacheEvict("comment::count", key = "#comment.drawId"),
        CacheEvict("comment::listTop4", key = "#comment.drawId")
    ])
    override fun save(comment: Comment): Comment {
        return commentDAO.save(comment)
    }

    @Cacheable("comment::count", key = "#drawId")
    override fun count(drawId: String): Long {
        return commentDAO.countByDrawId(drawId)
    }

    override fun list(drawId: String): List<Comment> {
        return commentDAO.findAllByDrawIdOrderByCreateDateDesc(drawId)
    }

    @Cacheable("comment::listTop4", key = "#drawId")
    override fun listTop4(drawId: String): List<Comment> {
        return commentDAO.findAllByDrawId(drawId, PageRequest.of(0, 4, Sort(Sort.Direction.DESC, "createDate"))).content
    }

    override fun paging(drawId: String, pageable: Pageable): Page<Comment> {
        return commentDAO.findAllByDrawId(drawId, pageable)
    }
}