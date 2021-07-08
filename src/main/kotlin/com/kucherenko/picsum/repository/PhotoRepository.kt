package com.kucherenko.picsum.repository

import com.kucherenko.picsum.entity.Photo
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface PhotoRepository : CrudRepository<Photo, Long> {
    fun findAllByUserId(userId: Long): List<Photo>
}

