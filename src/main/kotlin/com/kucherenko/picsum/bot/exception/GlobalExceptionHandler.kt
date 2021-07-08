package com.kucherenko.picsum.bot.exception

import org.springframework.stereotype.Service

@Service
class GlobalExceptionHandler {

    //TODO: implement
    fun handle(exception: Exception) {
        throw exception
    }
}