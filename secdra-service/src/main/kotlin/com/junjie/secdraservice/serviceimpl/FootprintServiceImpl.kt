package com.junjie.secdraservice.serviceimpl

import com.junjie.secdracore.exception.NotFoundException
import com.junjie.secdradata.database.primary.dao.FootprintDAO
import com.junjie.secdradata.database.primary.entity.Footprint
import com.junjie.secdraservice.service.FootprintService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.*

@Service
class FootprintServiceImpl(private val footprintDAO: FootprintDAO) : FootprintService {
    override fun get(userId: String, pictureId: String): Footprint {
        return footprintDAO.getFirstByUserIdAndPictureId(userId, pictureId).orElseThrow { NotFoundException("找不到足迹") }
    }

    override fun exists(userId: String, pictureId: String): Boolean {
        return footprintDAO.existsByUserIdAndPictureId(userId, pictureId)
    }

    override fun save(userId: String, pictureId: String): Footprint {
        return footprintDAO.save(Footprint(userId, pictureId))
    }

    override fun update(userId: String, pictureId: String): Footprint {
        val footprint = get(userId, pictureId)
        // 比较特殊，因为没有数据发生变化，所以修改时间不会进行更改，所以要手动修改
        footprint.modifiedDate = Date()
        return footprintDAO.save(footprint)
    }

    override fun remove(userId: String, pictureId: String): Boolean {
        return try {
            footprintDAO.deleteByUserIdAndPictureId(userId, pictureId)
            true
        } catch (e: Exception) {
            throw e
        }
    }

    override fun countByPictureId(pictureId: String): Long {
        return footprintDAO.countByPictureId(pictureId)
    }

    override fun pagingByUserId(userId: String, pageable: Pageable): Page<Footprint> {
        return footprintDAO.findAllByUserId(userId, pageable)
    }

    override fun pagingByPictureId(pictureId: String, pageable: Pageable): Page<Footprint> {
        return footprintDAO.findAllByPictureId(pictureId, pageable)
    }
}