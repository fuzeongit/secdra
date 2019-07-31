package com.junjie.secdrasearch.dao

import com.junjie.secdrasearch.model.IndexDraw
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository

interface IndexDrawDAO : ElasticsearchRepository<IndexDraw, String>