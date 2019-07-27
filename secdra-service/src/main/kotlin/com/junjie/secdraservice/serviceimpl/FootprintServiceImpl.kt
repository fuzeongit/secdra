package com.junjie.secdraservice.serviceimpl

import com.junjie.secdraservice.dao.FootprintDAO
import com.junjie.secdraservice.model.Footprint
import com.junjie.secdraservice.service.FootprintService
import org.springframework.stereotype.Service

@Service
class FootprintServiceImpl(private val footprintDAO: FootprintDAO) : FootprintService {
    override fun get(userId: String, drawId: String): Footprint {
        return footprintDAO.findFirstByUserIdAndDrawId(userId, drawId)
    }

    override fun countByDrawId(drawId: String): Long {
        return footprintDAO.countByDrawId(drawId)
    }
}