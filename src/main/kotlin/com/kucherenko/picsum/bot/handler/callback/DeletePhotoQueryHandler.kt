package com.kucherenko.picsum.bot.handler.callback

import com.kucherenko.picsum.bot.UserState
import com.kucherenko.picsum.bot.getDeletConfirmText
import com.kucherenko.picsum.bot.getDeleteConfirmKeyBoard
import com.kucherenko.picsum.bot.handler.UpdateHandler
import com.kucherenko.picsum.entity.UserEntity
import com.kucherenko.picsum.service.PhotoService
import com.kucherenko.picsum.service.UserService
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.bots.AbsSender

class DeletePhotoQueryHandler(
    private val photoService: PhotoService,
    private val userService: UserService
) : UpdateHandler {

    override fun handle(update: Update, user: UserEntity, sender: AbsSender) {
        val photoId = update.callbackQuery.data.substringAfter("/").trim().toLong()
        val photo = photoService.getById(photoId)

        val text = getDeletConfirmText(photo.author, photo.id.toString()) + "\n" + photo.downloadUrl
        val markup = getDeleteConfirmKeyBoard(photo.id.toString())

        sender.execute(
            EditMessageText()
                .setChatId(user.chatId)
                .setMessageId(user.mainMenuId)
                .setText(text)
                .setReplyMarkup(markup)
        )
        userService.updateState(user.id, UserState.DELETE_PHOTO)
    }
}