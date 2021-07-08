package com.kucherenko.picsum.bot.handler.callback

import com.kucherenko.picsum.bot.BOT_CONTENT
import com.kucherenko.picsum.bot.UserState
import com.kucherenko.picsum.bot.handler.UpdateHandler
import com.kucherenko.picsum.entity.UserEntity
import com.kucherenko.picsum.service.PhotoService
import com.kucherenko.picsum.service.UserService
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.bots.AbsSender

class ConfirmDeleteQueryHandler(
    private val photoService: PhotoService,
    private val userService: UserService
) : UpdateHandler {

    override fun handle(update: Update, user: UserEntity, sender: AbsSender) {
        val photoId = update.callbackQuery.data.substringAfter("/").trim().toLong()
        photoService.deleteById(photoId)

        sender.execute(SendMessage(user.chatId, BOT_CONTENT[UserState.PHOTO_DELETED]?.text))
        userService.updateState(user.id, UserState.PHOTO_DELETED)

        sender.execute(
            EditMessageText()
                .setChatId(user.chatId)
                .setMessageId(user.mainMenuId)
                .setText(BOT_CONTENT[UserState.MAIN_MENU]?.text)
                .setReplyMarkup(BOT_CONTENT[UserState.MAIN_MENU]?.markup)
        )
        userService.updateState(user.id, UserState.MAIN_MENU)
    }
}