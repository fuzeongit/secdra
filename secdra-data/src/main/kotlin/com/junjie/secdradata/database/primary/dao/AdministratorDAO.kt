package com.junjie.secdradata.database.primary.dao

import com.junjie.secdradata.database.primary.entity.Administrator
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface AdministratorDAO : JpaRepository<Administrator, String>, JpaSpecificationExecutor<Administrator> {
    fun findOneByAccessKeyAndSecretKey(accessKey: String, secretKey: String): Optional<Administrator>
}