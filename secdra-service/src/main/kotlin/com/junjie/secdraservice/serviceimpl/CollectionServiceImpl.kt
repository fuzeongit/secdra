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
    override fun exists(userId: String?, pictureId: String): CollectState {
        return if (userId == null) {
            CollectState.CONCERNED
        } else {
            if (collectionDAO.existsByCreatedByAndPictureId(userId, pictureId)) CollectState.CONCERNED else CollectState.STRANGE
        }
    }

    override fun save(userId: String, pictureId: String): Collection {
        return collectionDAO.save(Collection(pictureId))
    }

    override fun remove(userId: String, pictureId: String): Boolean {
        return try {
            collectionDAO.deleteByCreatedByAndPictureId(userId, pictureId)
            true
        } catch (e: Exception) {
            throw e
        }
    }

    override fun countByPictureId(pictureId: String): Long {
        return collectionDAO.countByPictureId(pictureId)
    }

    override fun pagingByUserId(userId: String, pageable: Pageable): Page<Collection> {
        return collectionDAO.findAllByCreatedBy(userId, pageable)
    }

    override fun pagingByPictureId(pictureId: String, pageable: Pageable): Page<Collection> {
        return collectionDAO.findAllByPictureId(pictureId, pageable)
    }
}