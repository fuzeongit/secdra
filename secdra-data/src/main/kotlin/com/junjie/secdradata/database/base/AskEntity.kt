package com.junjie.secdradata.database.base

import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.LastModifiedBy
import java.io.Serializable
import javax.persistence.Column
import javax.persistence.MappedSuperclass


@MappedSuperclass
open class AskEntity : BaseEntity(), Serializable {
    //创建人，自动获取（有可能是管理员也有可能为用户）
    @CreatedBy
    @Column(name = "created_by", length = 32)
    var createdBy: String? = null

    //最后修改，自动获取
    @LastModifiedBy
    @Column(name = "last_modified_by", length = 32)
    var lastModifiedBy: String? = null
}