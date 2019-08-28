package com.junjie.secdraservice.serviceimpl

import com.junjie.secdradata.constant.CollectState
import com.junjie.secdradata.database.primary.dao.CollectionDAO
import com.junjie.secdradata.database.primary.entity.Collection
import com.junjie.secdraservice.service.CollectionService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service


@Service
class CollectionServiceImpl(private val collectionDAO: CollectionDAO) : CollectionService {
    override fun exists(userId: String?, drawId: String): CollectState {
        return if (userId == null) {
            CollectState.CONCERNED
        } else {
            if (collectionDAO.existsByUserIdAndDrawId(userId, drawId)) CollectState.CONCERNED else CollectState.STRANGE
        }
    }

    override fun save(userId: String, drawId: String): Collection {
        return collectionDAO.save(Collection(userId, drawId))
    }

    override fun remove(userId: String, drawId: String): Boolean {
        return try {
            collectionDAO.deleteByUserIdAndDrawId(userId, drawId)
            true
        } catch (e: Exception) {
            throw e
        }
    }

    override fun countByDrawId(drawId: String): Long {
        return collectionDAO.countByDrawId(drawId)
    }

    override fun pagingByUserId(userId: String, pageable: Pageable): Page<Collection> {
        return collectionDAO.findAllByUserId(userId, pageable)
    }

    override fun pagingByDrawId(drawId: String, pageable: Pageable): Page<Collection> {
        return collectionDAO.findAllByDrawId(drawId, pageable)
    }
}