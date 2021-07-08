package com.kucherenko.picsum.bot.handler.callback

import com.kucherenko.picsum.bot.BOT_CONTENT
import com.kucherenko.picsum.bot.UserState
import com.kucherenko.picsum.bot.getMainMenuKeyboard
import com.kucherenko.picsum.bot.handler.UpdateHandler
import com.kucherenko.picsum.entity.UserEntity
import com.kucherenko.picsum.service.UserService
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.bots.AbsSender

class MainMenuCallBackHandler(
    private val userService: UserService
) : UpdateHandler {

    override fun handle(update: Update, user: UserEntity, sender: AbsSender) {
        if (user.mainMenuId != null) {
            sender.execute(
                EditMessageText()
                    .setChatId(user.chatId)
                    .setMessageId(user.mainMenuId)
                    .setText(BOT_CONTENT[UserState.MAIN_MENU]?.text)
                    .setReplyMarkup(getMainMenuKeyboard())
            )
            userService.updateState(user.id, UserState.MAIN_MENU)
        } else {
            val mainMenuId = sender.execute(
                SendMessage(
                    user.chatId,
                    BOT_CONTENT[UserState.MAIN_MENU]?.text
                ).setReplyMarkup(getMainMenuKeyboard())
            ).messageId
            user.mainMenuId = mainMenuId
            userService.update(user)
        }
    }
}