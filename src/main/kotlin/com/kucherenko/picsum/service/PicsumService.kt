package com.kucherenko.picsum.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import com.kucherenko.picsum.entity.Photo
import org.springframework.http.HttpStatus

import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate


@Service
class PicsumService(
    private val restTemplate: RestTemplate
) {
    val BASE_URL = "picsum.photos/v2/list"
    val ACCEPTED_PROTOCOLS = listOf("http", "https")
    val ACCEPTED_PARAMS = listOf("page", "limit")


    fun getPhotosByUrl(url: String, userId: Long?): List<Photo> {
        if (isValidUrl(url)) {
            val response = restTemplate.getForEntity(url, String::class.java)

            if (response.statusCode != HttpStatus.OK || !response.hasBody()) {
                //TODO: customize
                throw RuntimeException("Can't perform request: status code '${response.statusCode}'")
            }
            val photos: List<Photo> = ObjectMapper().registerModule(KotlinModule()).readValue(response.body ?: "")
            photos.forEach {
                it.userId = userId
                it.size = "${it.width} x ${it.height}"
            }
            return photos
        } else {
            throw RuntimeException("Invalid link: '$url'")
        }
    }

    fun isValidUrl(url: String): Boolean {
        val protocol = url.substringBefore("://")
        val baseUrl = url.substringAfter("://").substringBefore("?")
        val params = url.replace("$protocol://", "")
            .replace(baseUrl, "")
            .substringAfter("?")
            .split("&")
            .map { it.substringBefore("=") }

        return BASE_URL == baseUrl &&
                ACCEPTED_PROTOCOLS.contains(protocol) &&
                params.all { ACCEPTED_PARAMS.contains(it) || it == "" }
    }
}