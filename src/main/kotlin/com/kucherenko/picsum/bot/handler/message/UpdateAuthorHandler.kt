package com.kucherenko.picsum.bot.handler.message

import com.kucherenko.picsum.bot.BOT_CONTENT
import com.kucherenko.picsum.bot.UserState
import com.kucherenko.picsum.bot.getGetPhotoKeyBoard
import com.kucherenko.picsum.bot.getPhotoDescriptionText
import com.kucherenko.picsum.bot.handler.UpdateHandler
import com.kucherenko.picsum.entity.UserEntity
import com.kucherenko.picsum.service.PhotoService
import com.kucherenko.picsum.service.UserService
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.bots.AbsSender

class UpdateAuthorHandler(
    private val userService: UserService,
    private val photoService: PhotoService
) : UpdateHandler {

    //TODO: refactor
    override fun handle(update: Update, user: UserEntity, sender: AbsSender) {
        val newAuthor = update.message.text

        val photo = photoService.getById(
            userService.getProperty("photoId", user).toLong()
        )

        photo.author = newAuthor
        photoService.save(photo)

        sender.execute(
            SendMessage(
                user.chatId,
                BOT_CONTENT[UserState.AUTHOR_UPDATED]?.text
            )
        )
        userService.updateState(user.id, UserState.AUTHOR_UPDATED)

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