package com.junjie.secdraservice.serviceimpl

import com.junjie.secdracore.component.BaseConfig
import com.junjie.secdracore.exception.NotFoundException
import com.junjie.secdracore.exception.PermissionException
import com.junjie.secdracore.exception.SignInException
import com.junjie.secdradata.constant.PictureState
import com.junjie.secdradata.constant.PrivacyState
import com.junjie.secdradata.database.primary.dao.UserDAO
import com.junjie.secdradata.database.primary.entity.Picture
import com.junjie.secdradata.database.primary.entity.Tag
import com.junjie.secdradata.database.primary.entity.User
import com.junjie.secdraservice.service.UserService
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service
import java.util.*
import javax.persistence.criteria.Join
import javax.persistence.criteria.JoinType
import javax.persistence.criteria.Predicate

@Service
class UserServiceImpl(private val userDAO: UserDAO) : UserService {

    @CachePut("user::get", key = "#user.id")
    override fun save(user: User): User {
        return userDAO.save(user)
    }

    @Cacheable("user::get")
    override fun get(id: String): User {
        return userDAO.findById(id).orElseThrow { NotFoundException("用户信息不存在") }
    }

    @Cacheable("user::getByAccountId")
    override fun getByAccountId(accountId: String): User {
        return userDAO.findOneByAccountId(accountId).orElseThrow { NotFoundException("用户信息不存在") }
    }

    override fun list(name: String?): List<User> {
        return if (name != null && name.isNotEmpty()) {
            userDAO.findAllByNameLike("%$name%")
        } else {
            userDAO.findAll()
        }
    }

    override fun paging(pageable: Pageable, name: String?, accountIdList: List<String>): Page<User> {
        val specification = Specification<User> { root, _, criteriaBuilder ->
            val predicatesList = ArrayList<Predicate>()
            if (accountIdList.isNotEmpty()) {
                val path = root.get<String>("accountId")
                val inValue = criteriaBuilder.`in`(path)
                accountIdList.forEach {
                    inValue.value(it)
                }
                predicatesList.add(criteriaBuilder.and(inValue))
            }
            if (name != null && name.isNotEmpty()) {
                predicatesList.add(criteriaBuilder.like(root.get<String>("name"), "%$name%"))
            }
            criteriaBuilder.and(*predicatesList.toArray(arrayOfNulls<Predicate>(predicatesList.size)))
        }
        return userDAO.findAll(specification, pageable)
    }
}





