package com.kucherenko.picsum.bot

import com.kucherenko.picsum.bot.config.properties.BotProperties
import com.kucherenko.picsum.bot.exception.GlobalExceptionHandler
import com.kucherenko.picsum.bot.handler.UpdateDispatcher
import org.springframework.stereotype.Service
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.objects.Update

@Service
class PicsumBot(
    private val botProperties: BotProperties,
    private val updateDispatcher: UpdateDispatcher,
    private val exceptionHandler: GlobalExceptionHandler
) : TelegramLongPollingBot() {

    override fun onUpdateReceived(update: Update) {
        try {
            updateDispatcher.dispatch(update, this)
        } catch (e: Exception) {
            exceptionHandler.handle(e)
        }
    }

    override fun getBotUsername() = botProperties.botUserName
    override fun getBotToken() = botProperties.token
}