package com.junjie.secdraservice.service

import com.junjie.secdradata.constant.PictureLifeState
import com.junjie.secdradata.index.primary.document.PictureDocument
import com.junjie.secdradata.database.primary.entity.Picture
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.*

/**
 * 画的服务
 *
 * @author fjj
 */
interface PictureService {
    @Deprecated("由于ES的引入，弃用改查询，查询的时候使用ES查询")
    fun paging(pageable: Pageable, tag: String?, startDate: Date?, endDate: Date?): Page<Picture>

    @Deprecated("由于ES的引入，弃用改查询，以合并到ES的piging")
    fun pagingByUserId(pageable: Pageable, userId: String, startDate: Date?, endDate: Date?, isSelf: Boolean): Page<Picture>

    @Deprecated("由于ES的引入，弃用改查询，现在暂时按数据库直接出，加了足迹功能后会写推荐")
    fun pagingRand(pageable: Pageable): Page<Picture>

    @Deprecated("由于ES的引入，弃用改查询，使用ES的countByTag")
    fun countByTag(tag: String): Long

    fun paging(pageable: Pageable, userId: String?, nickname: String?, name: String?, startDate: Date?, endDate: Date?): Page<Picture>

    fun pagingByLife(life: PictureLifeState, pageable: Pageable): Page<Picture>

    fun get(id: String): Picture

    fun getByLife(id: String, life: PictureLifeState?): Picture

    /**
     * 逻辑删除
     */
    fun remove(picture: Picture): Boolean

    /**
     * 还原
     */
    fun reduction(id: String): PictureDocument

    /**
     * 物理删除
     */
    fun delete(id: String)

    fun list(): List<Picture>

    fun listByLife(life: PictureLifeState?): List<Picture>

    fun listByUserId(userId: String): List<Picture>

    fun listByUserIdAndLife(userId: String, life: PictureLifeState?): List<Picture>
    /**
     * @param force 是否强制更新
     */
    fun save(picture: Picture, force: Boolean = false): PictureDocument

    fun synchronizationIndexPicture(): Long
}