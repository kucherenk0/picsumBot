package com.kucherenko.picsum.bot.handler.message

import com.kucherenko.picsum.bot.BOT_CONTENT
import com.kucherenko.picsum.bot.UserState
import com.kucherenko.picsum.bot.getMainMenuKeyboard
import com.kucherenko.picsum.bot.getSkipKeyboard
import com.kucherenko.picsum.bot.handler.UpdateHandler
import com.kucherenko.picsum.entity.UserEntity
import com.kucherenko.picsum.service.PhotoService
import com.kucherenko.picsum.service.PicsumService
import com.kucherenko.picsum.service.UserService
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.bots.AbsSender

class FirstPhotoDownloadHandler(
    private val userService: UserService,
    private val picsumService: PicsumService,
    private val photoService: PhotoService
) : UpdateHandler {


    override fun handle(update: Update, user: UserEntity, sender: AbsSender) {
        val url = update.message.text
        if (picsumService.isValidUrl(url)) {
            handleCorrectLink(url, user, sender)
        } else {
            handleIncorrectLink(sender, user)
        }
    }

    private fun handleIncorrectLink(sender: AbsSender, user: UserEntity) {
        sender.execute(
            SendMessage(
                user.chatId,
                BOT_CONTENT[UserState.INCORRECT_LINK]?.text,
            ).setReplyMarkup(getSkipKeyboard("main_menu"))
        )
        userService.updateState(user.id, UserState.FIRST_PHOTOS_DOWNLOAD)
    }

    private fun handleCorrectLink(
        url: String,
        user: UserEntity,
        sender: AbsSender
    ) {
        val photos = picsumService.getPhotosByUrl(url, user.id)
        photoService.saveAll(photos)

        sender.execute(SendMessage(user.chatId, BOT_CONTENT[UserState.PHOTOS_SAVED]?.text))
        userService.updateState(user.id, UserState.PHOTOS_SAVED)

        val mainMenuId = sender.execute(
            SendMessage(
                user.chatId,
                BOT_CONTENT[UserState.MAIN_MENU]?.text
            ).setReplyMarkup(getMainMenuKeyboard())
        ).messageId

        user.mainMenuId = mainMenuId
        user.userState = UserState.MAIN_MENU

        userService.update(user)
    }
}