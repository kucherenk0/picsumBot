package com.kucherenko.picsum.bot

import org.telegram.telegrambots.meta.TelegramBotsApi
import org.telegram.telegrambots.meta.exceptions.TelegramApiException


class BotInitializer(
    telegramBotsApi: TelegramBotsApi,
    picsumBot: PicsumBot
) {
    init {
        try {
            telegramBotsApi.registerBot(picsumBot)
        } catch (ex: TelegramApiException) {
            ex.printStackTrace()
        }
    }
}