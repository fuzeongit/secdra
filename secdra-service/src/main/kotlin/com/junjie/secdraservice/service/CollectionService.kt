package com.junjie.secdraservice.service

import com.junjie.secdraservice.dao.ICollectionDao
import com.junjie.secdraservice.model.Collection
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service


@Service
class CollectionService(private val collectionDao: ICollectionDao) : ICollectionService {
    override fun exists(userId: String, drawId: String): Boolean {
        return collectionDao.existsByUserIdAndDrawId(userId, drawId)
    }

    override fun get(userId: String, drawId: String): Collection {
        return collectionDao.findFirstByUserIdAndDrawId(userId, drawId)
    }

    override fun save(userId: String, drawId: String): Collection {
        val collection = Collection()
        collection.userId = userId
        collection.drawId = drawId
        return collectionDao.save(collection)
    }

    override fun remove(userId: String, drawId: String): Boolean {
        try {
            collectionDao.deleteByUserIdAndDrawId(userId, drawId)
            return true
        } catch (e: Exception) {
            throw e
        }
    }

    override fun countByDrawId(drawId: String): Long {
        return collectionDao.countByDrawId(drawId)
    }

    override fun paging(userId: String, pageable: Pageable): Page<Collection> {
        return collectionDao.findAllByUserId(userId, pageable)
    }
}