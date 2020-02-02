package com.junjie.secdraservice.service

import com.junjie.secdradata.database.primary.entity.Administrator

interface AdministratorService {
    fun get(accessKey: String, secretKey: String): Administrator
}