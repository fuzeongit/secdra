package com.junjie.secdraservice.serviceimpl

import com.junjie.secdraservice.dao.IStatisticalDao
import com.junjie.secdraservice.model.Statistical
import com.junjie.secdraservice.service.IStatisticalService
import org.springframework.stereotype.Service

@Service
class StatisticalService(private val statisticalDao: IStatisticalDao) : IStatisticalService {
    override fun save(statistical: Statistical): Statistical {
        return statisticalDao.save(statistical)
    }
}