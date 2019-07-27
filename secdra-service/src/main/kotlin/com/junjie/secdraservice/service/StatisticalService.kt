package com.junjie.secdraservice.service

import com.junjie.secdraservice.model.Statistical

interface StatisticalService {
    fun save(statistical: Statistical): Statistical
}