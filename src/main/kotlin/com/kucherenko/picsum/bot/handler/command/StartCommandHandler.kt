package com.kucherenko.picsum.bot.handler.command

import com.kucherenko.picsum.bot.BOT_CONTENT
import com.kucherenko.picsum.bot.UserState
import com.kucherenko.picsum.bot.handler.UpdateHandler
import com.kucherenko.picsum.entity.UserEntity
import com.kucherenko.picsum.service.UserService
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.bots.AbsSender

class StartCommandHandler(
    private val userService: UserService
) : UpdateHandler {

    override fun handle(update: Update, user: UserEntity, sender: AbsSender) {
        sender.execute(
            SendMessage(
                user.chatId,
                BOT_CONTENT[UserState.FIRST_PHOTOS_DOWNLOAD]?.text,
            ).setReplyMarkup(BOT_CONTENT[UserState.FIRST_PHOTOS_DOWNLOAD]?.markup)
        )

        user.userState = UserState.FIRST_PHOTOS_DOWNLOAD
        user.mainMenuId = null
        userService.update(user)
    }
}