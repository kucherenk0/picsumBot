package com.kucherenko.picsum.bot.handler.message

import com.kucherenko.picsum.bot.BOT_CONTENT
import com.kucherenko.picsum.bot.UserState
import com.kucherenko.picsum.bot.handler.UpdateHandler
import com.kucherenko.picsum.entity.UserEntity
import com.kucherenko.picsum.service.UserService
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.bots.AbsSender

class OnboardingHandler(
    private val userService: UserService
) : UpdateHandler {

    override fun handle(update: Update, user: UserEntity, sender: AbsSender) {
        sender.execute(
            SendMessage(
                user.chatId,
                BOT_CONTENT[UserState.FIRST_PHOTOS_DOWNLOAD]?.text,
            ).setReplyMarkup(BOT_CONTENT[UserState.FIRST_PHOTOS_DOWNLOAD]?.markup)
        )
        userService.updateState(user.id, UserState.FIRST_PHOTOS_DOWNLOAD)
    }
}
