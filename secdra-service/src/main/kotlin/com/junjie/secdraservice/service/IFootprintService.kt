package com.junjie.secdraservice.service

import com.junjie.secdraservice.model.Footprint

interface IFootprintService {
    fun countByDrawId(drawId: String): Long

    fun get(userId:String,drawId:String): Footprint
}