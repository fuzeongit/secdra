package com.junjie.secdraservice.dao

import com.junjie.secdraservice.model.Comment
import org.springframework.data.jpa.repository.JpaRepository

interface ICommentDao : JpaRepository<Comment, String>