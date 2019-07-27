package com.junjie.secdraservice.serviceimpl

import com.junjie.secdraservice.dao.CollectionDAO
import com.junjie.secdraservice.model.Collection
import com.junjie.secdraservice.service.CollectionService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service


@Service
class CollectionServiceImpl(private val collectionDAO: CollectionDAO) : CollectionService {
    override fun exists(userId: String, drawId: String): Boolean {
        return collectionDAO.existsByUserIdAndDrawId(userId, drawId)
    }

    override fun get(userId: String, drawId: String): Collection {
        return collectionDAO.findFirstByUserIdAndDrawId(userId, drawId)
    }

    override fun save(userId: String, drawId: String): Collection {
        val collection = Collection()
        collection.userId = userId
        collection.drawId = drawId
        return collectionDAO.save(collection)
    }

    override fun remove(userId: String, drawId: String): Boolean {
        try {
            collectionDAO.deleteByUserIdAndDrawId(userId, drawId)
            return true
        } catch (e: Exception) {
            throw e
        }
    }

    override fun countByDrawId(drawId: String): Long {
        return collectionDAO.countByDrawId(drawId)
    }

    override fun paging(userId: String, pageable: Pageable): Page<Collection> {
        return collectionDAO.findAllByUserId(userId, pageable)
    }
}