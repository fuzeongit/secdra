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
        CacheEvict("comment::count", key = "#comment.pictureId"),
        CacheEvict("comment::listTop4", key = "#comment.pictureId")
    ])
    override fun save(comment: Comment): Comment {
        return commentDAO.save(comment)
    }

    @Cacheable("comment::count", key = "#pictureId")
    override fun count(pictureId: String): Long {
        return commentDAO.countByPictureId(pictureId)
    }

    override fun list(pictureId: String): List<Comment> {
        return commentDAO.findAllByPictureIdOrderByCreateDateDesc(pictureId)
    }

    @Cacheable("comment::listTop4", key = "#pictureId")
    override fun listTop4(pictureId: String): List<Comment> {
        return commentDAO.findAllByPictureId(pictureId, PageRequest.of(0, 4, Sort(Sort.Direction.DESC, "createDate"))).content
    }

    override fun paging(pictureId: String, pageable: Pageable): Page<Comment> {
        return commentDAO.findAllByPictureId(pictureId, pageable)
    }
}