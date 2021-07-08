package com.kucherenko.picsum.bot.handler

import com.kucherenko.picsum.entity.UserEntity
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.bots.AbsSender

interface UpdateHandler {
    fun handle(update: Update, user: UserEntity, sender: AbsSender)
}