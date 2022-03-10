package com.longle.data.mapper.model

interface ModelMapper<in T, out R> {
    fun toModel(input: T): R
}
