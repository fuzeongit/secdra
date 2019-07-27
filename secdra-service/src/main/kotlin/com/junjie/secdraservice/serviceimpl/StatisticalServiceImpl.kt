package com.junjie.secdraservice.serviceimpl

import com.junjie.secdraservice.dao.StatisticalDAO
import com.junjie.secdraservice.model.Statistical
import com.junjie.secdraservice.service.StatisticalService
import org.springframework.stereotype.Service

@Service
class StatisticalServiceImpl(private val statisticalDAO: StatisticalDAO) : StatisticalService {
    override fun save(statistical: Statistical): Statistical {
        return statisticalDAO.save(statistical)
    }
}