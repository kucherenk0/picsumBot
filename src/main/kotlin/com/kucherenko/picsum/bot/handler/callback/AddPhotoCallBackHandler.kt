package com.kucherenko.picsum.bot.handler.callback

import com.kucherenko.picsum.bot.BOT_CONTENT
import com.kucherenko.picsum.bot.UserState
import com.kucherenko.picsum.bot.getBackKeyboard
import com.kucherenko.picsum.bot.handler.UpdateHandler
import com.kucherenko.picsum.entity.UserEntity
import com.kucherenko.picsum.service.UserService
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.bots.AbsSender

class AddPhotoCallBackHandler(
    private val userService: UserService
) : UpdateHandler {

    override fun handle(update: Update, user: UserEntity, sender: AbsSender) {
        sender.execute(
            EditMessageText()
                .setChatId(user.chatId)
                .setMessageId(user.mainMenuId)
                .setText(BOT_CONTENT[UserState.PHOTOS_DOWNLOAD]?.text)
                .setReplyMarkup(getBackKeyboard("main_menu"))
        )
        userService.updateState(user.id, UserState.PHOTOS_DOWNLOAD)
    }
}