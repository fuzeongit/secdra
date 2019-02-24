package com.junjie.secdraservice.serviceimpl

import com.junjie.secdraservice.dao.ICommentDao
import com.junjie.secdraservice.model.Comment
import com.junjie.secdraservice.service.ICommentService
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.cache.annotation.Caching
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service

@Service
class CommentService(private val commentDao: ICommentDao) : ICommentService {
    @Caching(evict = [
        CacheEvict("comment::count", key = "#comment.drawId"),
        CacheEvict("comment::listTop4", key = "#comment.drawId")
    ])
    override fun save(comment: Comment): Comment {
        return commentDao.save(comment)
    }

    @Cacheable("comment::count", key = "#drawId")
    override fun count(drawId: String): Long{
        return commentDao.countByDrawId(drawId)
    }

    override fun list(drawId: String): List<Comment> {
        return commentDao.findAllByDrawIdOrderByCreateDateDesc(drawId)
    }

    @Cacheable("comment::listTop4", key = "#drawId")
    override fun listTop4(drawId: String): List<Comment> {
        return commentDao.findAllByDrawId(drawId, PageRequest.of(0, 4, Sort(Sort.Direction.DESC, "createDate"))).content
    }

    override fun paging(drawId: String, pageable: Pageable): Page<Comment> {
        return commentDao.findAllByDrawId(drawId, pageable)
    }
}