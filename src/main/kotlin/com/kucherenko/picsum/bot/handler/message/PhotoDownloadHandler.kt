package com.kucherenko.picsum.bot.handler.message

import com.kucherenko.picsum.bot.BOT_CONTENT
import com.kucherenko.picsum.bot.UserState
import com.kucherenko.picsum.bot.handler.UpdateHandler
import com.kucherenko.picsum.entity.UserEntity
import com.kucherenko.picsum.service.PhotoService
import com.kucherenko.picsum.service.PicsumService
import com.kucherenko.picsum.service.UserService
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.meta.bots.AbsSender

class PhotoDownloadHandler(
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
            EditMessageText()
                .setMessageId(user.mainMenuId)
                .setChatId(user.chatId)
                .setText(BOT_CONTENT[UserState.PHOTOS_DOWNLOAD]?.text)
                .setReplyMarkup(InlineKeyboardMarkup())
        )
        sender.execute(
            SendMessage(
                user.chatId,
                BOT_CONTENT[UserState.INCORRECT_LINK]?.text,
            ).setReplyMarkup(BOT_CONTENT[UserState.INCORRECT_LINK]?.markup)
        )
        userService.updateState(user.id, UserState.PHOTOS_DOWNLOAD)
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

        sender.execute(
            EditMessageText()
                .setChatId(user.chatId)
                .setMessageId(user.mainMenuId)
                .setText(BOT_CONTENT[UserState.MAIN_MENU]?.text)
                .setReplyMarkup(BOT_CONTENT[UserState.MAIN_MENU]?.markup)
        )

        user.userState = UserState.MAIN_MENU
        userService.update(user)
    }
}