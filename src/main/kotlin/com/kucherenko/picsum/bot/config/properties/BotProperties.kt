package com.kucherenko.picsum.bot.config.properties

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
data class BotProperties(
    @Value("your bot token")
    val token: String,
    @Value("your bot username ")
    val botUserName: String
)