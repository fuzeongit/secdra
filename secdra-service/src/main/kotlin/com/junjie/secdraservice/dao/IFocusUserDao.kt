package com.junjie.secdraservice.dao

import com.junjie.secdraservice.model.FocusUser
import org.springframework.data.jpa.repository.JpaRepository

interface IFocusUserDao : JpaRepository<FocusUser, String>