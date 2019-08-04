package com.junjie.secdracore.fragment

import com.fasterxml.jackson.annotation.JsonIgnore


abstract class MixIn {
    @get:JsonIgnore
    abstract val keyAsNumber: Number
}