package com.junjie.secdraservice.service

import com.junjie.secdraservice.dao.IStatisticalDao
import com.junjie.secdraservice.model.Statistical
import org.springframework.stereotype.Service

@Service
class StatisticalService(private val statisticalDao: IStatisticalDao) : IStatisticalService {
    override fun save(statistical: Statistical): Statistical {
        return statisticalDao.save(statistical)
    }
}