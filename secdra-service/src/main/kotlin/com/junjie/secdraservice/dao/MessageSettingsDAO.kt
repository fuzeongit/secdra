package com.junjie.secdraservice.dao

import com.junjie.secdraservice.model.MessageSettings
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MessageSettingsDAO : JpaRepository<MessageSettings, String> {

}