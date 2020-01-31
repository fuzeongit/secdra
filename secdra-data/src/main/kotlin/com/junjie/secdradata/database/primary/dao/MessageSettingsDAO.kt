package com.junjie.secdradata.database.primary.dao

import com.junjie.secdradata.database.primary.entity.MessageSettings
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MessageSettingsDAO : JpaRepository<MessageSettings, String>