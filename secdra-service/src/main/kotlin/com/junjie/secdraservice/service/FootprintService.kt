package com.junjie.secdraservice.service

import com.junjie.secdraservice.dao.IFootprintDao
import com.junjie.secdraservice.model.Footprint
import org.springframework.stereotype.Service

@Service
class FootprintService(private val footprintDao: IFootprintDao) : IFootprintService {
    override fun get(userId: String, drawId: String): Footprint {
        return footprintDao.findFirstByUserIdAndDrawId(userId, drawId)
    }

    override fun countByDrawId(drawId: String): Long {
        return footprintDao.countByDrawId(drawId)
    }
}