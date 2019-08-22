package com.junjie.secdraservice.serviceimpl

import com.junjie.secdracore.exception.NotFoundException
import com.junjie.secdracore.exception.ProgramException
import com.junjie.secdraservice.dao.FootprintDAO
import com.junjie.secdraservice.model.Collection
import com.junjie.secdraservice.model.Footprint
import com.junjie.secdraservice.service.FootprintService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service
import java.util.ArrayList
import javax.persistence.criteria.Predicate

@Service
class FootprintServiceImpl(private val footprintDAO: FootprintDAO) : FootprintService {
    override fun get(userId: String, drawId: String): Footprint {
        return footprintDAO.findFirstByUserIdAndDrawId(userId, drawId).orElseThrow { NotFoundException("找不到足迹") }
    }

    override fun exists(userId: String, drawId: String): Boolean {
        return footprintDAO.existsByUserIdAndDrawId(userId, drawId)
    }

    override fun save(userId: String, drawId: String): Footprint {
        return footprintDAO.save(Footprint(userId, drawId))
    }

    override fun update(userId: String, drawId: String): Footprint {
        val footprint = get(userId, drawId)
        return footprintDAO.save(footprint)
    }

    override fun remove(userId: String, drawId: String): Boolean {
        return try {
            footprintDAO.deleteByUserIdAndDrawId(userId, drawId)
            true
        } catch (e: Exception) {
            throw e
        }
    }

    override fun countByDrawId(drawId: String): Long {
        return footprintDAO.countByDrawId(drawId)
    }

    override fun pagingByUserId(userId: String, pageable: Pageable): Page<Footprint> {
        return footprintDAO.findAllByUserId(userId, pageable)
    }

    override fun pagingByDrawId(drawId: String, pageable: Pageable): Page<Footprint> {
        return footprintDAO.findAllByDrawId(drawId, pageable)
    }
}