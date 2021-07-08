package com.kucherenko.picsum.bot.handler

import com.kucherenko.picsum.bot.UserState
import com.kucherenko.picsum.bot.exception.DataNotFoundException
import com.kucherenko.picsum.bot.exception.UndefinedBehaviourException
import com.kucherenko.picsum.entity.UserEntity
import com.kucherenko.picsum.service.UserService
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.bots.AbsSender

@Service
class UpdateDispatcher(
    private val messageHandlers: Map<UserState, UpdateHandler>,
    private val commandHandlers: Map<String, UpdateHandler>,
    private val queryCallBackHandlers: Map<String, UpdateHandler>,
    private val userService: UserService,
) {
    enum class UpdateType {
        MESSAGE, QUERY_CALLBACK, COMMAND, UNKNOWN
    }

    fun dispatch(update: Update, sender: AbsSender) {
        val user = getUserOrCreate(update)
        when (getUpdateType(update)) {
            UpdateType.MESSAGE -> handleMessage(update, user, sender)
            UpdateType.COMMAND -> handleCommand(update, user, sender)
            UpdateType.QUERY_CALLBACK -> handleQueryCallBack(update, user, sender)
            else -> throw UndefinedBehaviourException("Unknown Update: $update")
        }
    }

    private fun handleUnknownUpdate(update: Update, user: UserEntity, sender: AbsSender) {
        commandHandlers["help"]?.handle(update, user, sender)
    }

    private fun handleQueryCallBack(update: Update, user: UserEntity, sender: AbsSender) {
        val callbackQueryCommand = update.callbackQuery.data.substringBefore("/")
        queryCallBackHandlers[callbackQueryCommand]?.handle(update, user, sender)
            ?: throw UndefinedBehaviourException("Unknown callback query command $callbackQueryCommand")
    }

    private fun handleCommand(update: Update, user: UserEntity, sender: AbsSender) {
        val commandText = update.message.text.substringAfter("/").substringBefore(" ")
        commandHandlers[commandText]?.handle(update, user, sender)
            ?: throw UndefinedBehaviourException("Unknown command $commandText")
    }

    private fun handleMessage(update: Update, user: UserEntity, sender: AbsSender) {
        messageHandlers[user.userState]?.handle(update, user, sender)
            ?: throw UndefinedBehaviourException("Handler for state ${user.userState} is not defined")
    }

    private fun getUserOrCreate(update: Update): UserEntity {
        val telegramId: String
        if (update.hasCallbackQuery()) {
            telegramId = update.callbackQuery.from.id.toString()
        } else if (update.hasMessage()) {
            telegramId = update.message.from.id.toString()
        } else {
            throw IllegalArgumentException("Update should contain callback or message")
        }

        return try {
            userService.getByTelegramId(telegramId)
        } catch (e: DataNotFoundException) {
            userService.create(
                UserEntity(
                    telegramId = update.message.from.id.toString(),
                    telegramName = update.message.from.userName,
                    chatId = update.message.chatId.toString(),
                    userState = UserState.ONBOARDING,
                    mainMenuId = null
                )
            )
        }

    }

    private fun getUpdateType(update: Update): UpdateType {
        if (update.hasCallbackQuery()) {
            return UpdateType.QUERY_CALLBACK
        } else if (update.hasMessage()) {
            val message = update.message.text
            if (message.startsWith("/")) {
                return UpdateType.COMMAND
            }
            return UpdateType.MESSAGE
        }
        return UpdateType.UNKNOWN
    }

}