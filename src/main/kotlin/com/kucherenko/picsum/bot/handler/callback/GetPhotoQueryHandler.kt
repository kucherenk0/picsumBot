package com.kucherenko.picsum.bot.handler.callback

import com.kucherenko.picsum.bot.UserState
import com.kucherenko.picsum.bot.getGetPhotoKeyBoard
import com.kucherenko.picsum.bot.getPhotoDescriptionText
import com.kucherenko.picsum.bot.handler.UpdateHandler
import com.kucherenko.picsum.entity.UserEntity
import com.kucherenko.picsum.service.PhotoService
import com.kucherenko.picsum.service.UserService
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.bots.AbsSender

class GetPhotoQueryHandler(
    private val photoService: PhotoService,
    private val userService: UserService
) : UpdateHandler {

    override fun handle(update: Update, user: UserEntity, sender: AbsSender) {
        val photoId = update.callbackQuery.data.substringAfter("/").trim().toLong()

        val photo = photoService.getById(photoId)

        val text = getPhotoDescriptionText(
            photo.author,
            photo.id.toString(),
            photo.size, photo.url,
            photo.downloadUrl
        )

        val markup = getGetPhotoKeyBoard(photo.id.toString())

        sender.execute(
            EditMessageText()
                .setChatId(user.chatId)
                .setMessageId(user.mainMenuId)
                .setText(text)
                .setReplyMarkup(markup)
        )
        userService.updateState(user.id, UserState.GET_PHOTO)
    }
}