package com.kucherenko.picsum.service

import com.kucherenko.picsum.bot.UserState
import com.kucherenko.picsum.bot.exception.DataNotFoundException
import com.kucherenko.picsum.entity.UserEntity
import com.kucherenko.picsum.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository
) {

    fun getByTelegramId(telegramId: String): UserEntity {
        val user = userRepository.getByTelegramId(telegramId)
            ?: throw DataNotFoundException("Can't find user with telegram_id '$telegramId'")
        if (user.deleted) {
            //TODO: change
            throw DataNotFoundException("User with telegram_id $telegramId was deleted")
        }
        return user
    }

    fun create(userEntity: UserEntity): UserEntity = userRepository.save(userEntity)

    fun update(userEntity: UserEntity): UserEntity = userRepository.save(userEntity)

    fun updateState(id: Long?, userState: UserState) {
        if (id != null) {
            val user = userRepository.getById(id)
            user.userState = userState
            userRepository.save(user)
        } else
            throw IllegalArgumentException("Id must be not null")
    }

    fun getProperty(name: String, user: UserEntity): String {
        return user.props.substringAfter("$name: ").trim()
    }
}

