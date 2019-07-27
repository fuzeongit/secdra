package com.junjie.secdraservice.service

import com.junjie.secdraservice.model.Footprint

interface FootprintService {
    fun countByDrawId(drawId: String): Long

    fun get(userId:String,drawId:String): Footprint
}