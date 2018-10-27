package com.junjie.secdraservice.service

import com.junjie.secdraservice.model.Statistical

interface IStatisticalService {
    fun save(statistical: Statistical): Statistical
}