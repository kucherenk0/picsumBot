package com.kucherenko.picsum.bot.handler.command

import com.kucherenko.picsum.bot.LIST_OF_COMMANDS
import com.kucherenko.picsum.bot.handler.UpdateHandler
import com.kucherenko.picsum.entity.UserEntity
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.bots.AbsSender

class HelpCommandHandler : UpdateHandler {
    override fun handle(update: Update, user: UserEntity, sender: AbsSender) {
        val text = StringBuilder("<b>Help</b>\nThese are the registered commands for this Bot:\n")

        LIST_OF_COMMANDS.forEach { text.append(it + "\n") }

        sender.execute(
            SendMessage(
                user.chatId,
                text.toString()
            ).enableHtml(true)
        )
    }
}