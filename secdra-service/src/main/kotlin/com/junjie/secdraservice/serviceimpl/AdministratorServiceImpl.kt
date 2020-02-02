package com.junjie.secdraservice.serviceimpl

import com.junjie.secdracore.exception.NotFoundException
import com.junjie.secdradata.database.primary.dao.AdministratorDAO
import com.junjie.secdradata.database.primary.entity.Administrator
import com.junjie.secdraservice.service.AdministratorService
import org.springframework.stereotype.Service

/**
 * @author fjj
 *
 * 管理员的服务
 */
@Service
class AdministratorServiceImpl(private val administratorDAO: AdministratorDAO) : AdministratorService {
    override fun get(accessKey: String, secretKey: String): Administrator {
        return administratorDAO.findOneByAccessKeyAndSecretKey(accessKey, secretKey).orElseThrow { NotFoundException("登录失败") }
    }
}