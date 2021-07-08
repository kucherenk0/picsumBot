package com.kucherenko.picsum.bot.config.properties

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
data class BotProperties(
    @Value("1757103980:AAEeog6xn7MxRCkswqbqBh_g0U8VlX23Ttc")
    val token: String,
    @Value("picsum_service_bot")
    val botUserName: String = "MyPicsumBot"
)