package com.junjie.secdraservice.dao

import com.junjie.secdraservice.model.MessageSettings
import org.springframework.data.jpa.repository.JpaRepository

interface IMessageSettingsDao : JpaRepository<MessageSettings, String> {

}