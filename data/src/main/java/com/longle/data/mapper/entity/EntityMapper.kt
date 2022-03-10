package com.longle.data.mapper.entity

interface EntityMapper<in T, out R> {
    fun toEntity(input: T): R
}
