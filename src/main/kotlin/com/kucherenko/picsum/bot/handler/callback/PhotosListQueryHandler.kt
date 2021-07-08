package com.kucherenko.picsum.bot.handler.callback

import com.kucherenko.picsum.bot.BOT_CONTENT
import com.kucherenko.picsum.bot.UserState
import com.kucherenko.picsum.bot.getAuthorButtonText
import com.kucherenko.picsum.bot.handler.UpdateHandler
import com.kucherenko.picsum.entity.Photo
import com.kucherenko.picsum.entity.UserEntity
import com.kucherenko.picsum.service.PhotoService
import com.kucherenko.picsum.service.UserService
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton
import org.telegram.telegrambots.meta.bots.AbsSender

class PhotosListQueryHandler(
    private val photoService: PhotoService,
    private val userService: UserService
) : UpdateHandler {

    override fun handle(update: Update, user: UserEntity, sender: AbsSender) {
        val photos = photoService.findAllByUserId(
            user.id ?: throw IllegalStateException("User id must be not null")
        )

        sender.execute(
            EditMessageText()
                .setChatId(user.chatId)
                .setMessageId(user.mainMenuId)
                .setText(BOT_CONTENT[UserState.PHOTOS_LIST]?.text)
                .setReplyMarkup(getMarkupForPhotosList(photos))
        )
        userService.updateState(user.id, UserState.PHOTOS_LIST)
    }

    //TODO: add pagination
    private fun getMarkupForPhotosList(photos: List<Photo>): InlineKeyboardMarkup {
        val keyboard = mutableListOf<List<InlineKeyboardButton>>()
        photos.forEach {
            keyboard.add(
                listOf(
                    InlineKeyboardButton(getAuthorButtonText(it.author, it.id.toString()))
                        .setCallbackData("get_photo/${it.id}")
                )
            )
        }
        return InlineKeyboardMarkup().setKeyboard(keyboard)
    }
}