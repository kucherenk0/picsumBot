package com.kucherenko.picsum.service

import com.kucherenko.picsum.bot.exception.DataNotFoundException
import com.kucherenko.picsum.entity.Photo
import com.kucherenko.picsum.repository.PhotoRepository
import org.springframework.stereotype.Service

@Service
class PhotoService(
    private val photoRepository: PhotoRepository
) {
    fun getById(id: Long): Photo {
        if (photoRepository.findById(id).isPresent) {
            return photoRepository.findById(id).get()
        } else {
            throw DataNotFoundException("Can't find photo with id '$id'", null)
        }
    }

    fun deleteById(id: Long) {
        val photo = getById(id)
        photo.deleted = true
        photoRepository.save(photo)
    }

    fun findAllByUserId(userId: Long): List<Photo> {
        return photoRepository.findAllByUserId(userId).filter { !it.deleted }
    }

    fun saveAll(photos: List<Photo>) {
        photos.forEach { save(it) }
    }

    fun save(photo: Photo) {
        photoRepository.save(photo)
    }
}