package com.junjie.secdraservice.serviceimpl

import com.junjie.secdracore.exception.NotFoundException
import com.junjie.secdracore.exception.PermissionException
import com.junjie.secdracore.util.DateUtil
import com.junjie.secdradata.constant.PictureLifeState
import com.junjie.secdradata.constant.PictureState
import com.junjie.secdradata.constant.PrivacyState
import com.junjie.secdradata.database.primary.dao.PictureDAO
import com.junjie.secdradata.index.primary.document.PictureDocument
import com.junjie.secdradata.database.primary.entity.Picture
import com.junjie.secdradata.database.primary.entity.Tag
import com.junjie.secdradata.database.primary.entity.User
import com.junjie.secdraservice.service.PictureDocumentService
import com.junjie.secdraservice.service.PictureService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service
import java.util.*
import javax.persistence.criteria.Join
import javax.persistence.criteria.JoinType
import javax.persistence.criteria.Predicate


@Service
class PictureServiceImpl(private val pictureDAO: PictureDAO,
                         private val pictureDocumentService: PictureDocumentService) : PictureService {
    override fun paging(pageable: Pageable, tag: String?, startDate: Date?, endDate: Date?): Page<Picture> {
        val specification = Specification<Picture> { root, _, criteriaBuilder ->
            val predicatesList = ArrayList<Predicate>()
            if (!tag.isNullOrEmpty()) {
                val joinTag: Join<Picture, Tag> = root.join("tagList", JoinType.LEFT)
                predicatesList.add(criteriaBuilder.like(joinTag.get<String>("name"), "%$tag%"))
            }
            if (startDate != null) {
                predicatesList.add(criteriaBuilder.greaterThan(root.get("createDate"), startDate))
            }
            if (endDate != null) {
                predicatesList.add(criteriaBuilder.lessThan(root.get("createDate"), endDate))
            }
            predicatesList.add(criteriaBuilder.equal(root.get<Int>("pictureState"), PictureState.PASS))
            predicatesList.add(criteriaBuilder.equal(root.get<Int>("privacy"), PrivacyState.PUBLIC))
            criteriaBuilder.and(*predicatesList.toArray(arrayOfNulls<Predicate>(predicatesList.size)))
        }
        return pictureDAO.findAll(specification, pageable)
    }

    override fun pagingByUserId(pageable: Pageable, userId: String, startDate: Date?, endDate: Date?, isSelf: Boolean): Page<Picture> {
        val specification = Specification<Picture> { root, _, criteriaBuilder ->
            val predicatesList = ArrayList<Predicate>()
            if (startDate != null) {
                predicatesList.add(criteriaBuilder.greaterThan(root.get("createDate"), startDate))
            }
            if (endDate != null) {
                predicatesList.add(criteriaBuilder.lessThan(root.get("createDate"), endDate))
            }
            val userJoin = root.join<Picture, User>("user", JoinType.LEFT)
            predicatesList.add(criteriaBuilder.equal(userJoin.get<String>("id"), userId))
            predicatesList.add(criteriaBuilder.equal(root.get<String>("pictureState"), PictureState.PASS))
            if (!isSelf) {
                predicatesList.add(criteriaBuilder.equal(root.get<String>("privacy"), PrivacyState.PUBLIC))
            }
            criteriaBuilder.and(*predicatesList.toArray(arrayOfNulls<Predicate>(predicatesList.size)))
        }
        return pictureDAO.findAll(specification, pageable)
    }

    override fun pagingRand(pageable: Pageable): Page<Picture> {
        return pictureDAO.pagingRand(pageable)
    }

    override fun countByTag(tag: String): Long {
        val specification = Specification<Picture> { root, criteriaQuery, criteriaBuilder ->
            val predicatesList = ArrayList<Predicate>()
            val joinTag: Join<Picture, Tag> = root.join("tagList", JoinType.LEFT)
            predicatesList.add(criteriaBuilder.like(joinTag.get<String>("name"), "%$tag%"))
            predicatesList.add(criteriaBuilder.equal(root.get<String>("pictureState"), PictureState.PASS))
            criteriaQuery.distinct(true)
            criteriaBuilder.and(*predicatesList.toArray(arrayOfNulls<Predicate>(predicatesList.size)))
        }
        return pictureDAO.count(specification)
    }

    override fun paging(pageable: Pageable, userId: String?, name: String?, privacy: PrivacyState?, life: PictureLifeState?, master: Boolean?, startDate: Date?, endDate: Date?): Page<Picture> {
        val specification = Specification<Picture> { root, _, criteriaBuilder ->
            val predicatesList = ArrayList<Predicate>()
            if (!name.isNullOrEmpty()) {
                predicatesList.add(criteriaBuilder.like(root.get<String>("name"), "%$name%"))
            }
            if (privacy != null) {
                predicatesList.add(criteriaBuilder.equal(root.get<Int>("privacy"), privacy))
            }
            if (life != null) {
                predicatesList.add(criteriaBuilder.equal(root.get<Int>("life"), life))
            }
            if (master != null) {
                if(master){
                    predicatesList.add(criteriaBuilder.like(root.get<String>("url"), "%_p0%"))
                }else{
                    predicatesList.add(criteriaBuilder.notLike(root.get<String>("url"), "%_p0%"))
                }
            }
            if (startDate != null) {
                predicatesList.add(criteriaBuilder.greaterThan(root.get("createDate"), DateUtil.getDayBeginTime(startDate)))
            }
            if (endDate != null) {
                predicatesList.add(criteriaBuilder.lessThan(root.get("createDate"), DateUtil.getDayEndTime(endDate)))
            }
            if (!userId.isNullOrEmpty()) {
                val joinUser: Join<Picture, User> = root.join("user", JoinType.LEFT)
                predicatesList.add(criteriaBuilder.like(joinUser.get<String>("id"), userId))
            }
            criteriaBuilder.and(*predicatesList.toArray(arrayOfNulls<Predicate>(predicatesList.size)))
        }
        return pictureDAO.findAll(specification, pageable)
    }

    override fun get(id: String): Picture {
        return getByLife(id, PictureLifeState.EXIST)
    }

    override fun getByLife(id: String, life: PictureLifeState?): Picture {
        if (life != null) {
            return pictureDAO.findByIdAndLife(id, life).orElseThrow { NotFoundException("图片不存在") }
        }
        return pictureDAO.findById(id).orElseThrow { NotFoundException("图片不存在") }
    }

    override fun remove(picture: Picture): Boolean {
        return try {
            picture.life = PictureLifeState.DISAPPEAR
            save(picture, true)
            //删除索引
            pictureDocumentService.remove(picture.id!!)
            true
        } catch (e: Exception) {
            throw e
        }
    }

    override fun reduction(id: String): PictureDocument {
        val picture = getByLife(id)
        picture.life = PictureLifeState.EXIST
        return save(picture, true)
    }

    override fun delete(id: String) {
        return pictureDAO.deleteById(id)
    }

    override fun list(): List<Picture> {
        return listByLife(PictureLifeState.EXIST)
    }

    override fun listByLife(life: PictureLifeState?): List<Picture> {
        if (life != null) {
            return pictureDAO.findAllByLife(life)
        }
        return pictureDAO.findAll()
    }

    override fun listByUserId(userId: String): List<Picture> {
        return listByUserIdAndLife(userId, PictureLifeState.EXIST)
    }

    override fun listByUserIdAndLife(userId: String, life: PictureLifeState?): List<Picture> {
        if (life != null) {
            return pictureDAO.findAllByUser_IdAndLife(userId, life)
        }
        return pictureDAO.findAllByUser_Id(userId)
    }

    override fun save(picture: Picture, force: Boolean): PictureDocument {
        if (picture.life == PictureLifeState.DISAPPEAR && !force) {
            throw NotFoundException("图片不存在")
        }
        return pictureDocumentService.save(PictureDocument(pictureDAO.save(picture)))
    }

    override fun synchronizationIndexPicture(): Long {
        return pictureDocumentService.saveAll(list().map {
            PictureDocument(it)
        }).toList().size.toLong()
    }
}