package com.kucherenko.picsum.bot.config

import com.kucherenko.picsum.bot.UserState
import com.kucherenko.picsum.bot.handler.UpdateHandler
import com.kucherenko.picsum.bot.handler.callback.*
import com.kucherenko.picsum.bot.handler.command.HelpCommandHandler
import com.kucherenko.picsum.bot.handler.command.StartCommandHandler
import com.kucherenko.picsum.bot.handler.message.FirstPhotoDownloadHandler
import com.kucherenko.picsum.bot.handler.message.OnboardingHandler
import com.kucherenko.picsum.bot.handler.message.PhotoDownloadHandler
import com.kucherenko.picsum.bot.handler.message.UpdateAuthorHandler
import com.kucherenko.picsum.service.PhotoService
import com.kucherenko.picsum.service.PicsumService
import com.kucherenko.picsum.service.UserService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class BotConfiguration {

    @Bean
    fun messageHandlers(
        userService: UserService,
        picsumService: PicsumService,
        photoService: PhotoService
    ): Map<UserState, UpdateHandler> {
        return mapOf(
            UserState.ONBOARDING to OnboardingHandler(userService),
            UserState.PHOTOS_DOWNLOAD to PhotoDownloadHandler(userService, picsumService, photoService),
            UserState.UPDATE_AUTHOR to UpdateAuthorHandler(userService, photoService),
            UserState.FIRST_PHOTOS_DOWNLOAD to FirstPhotoDownloadHandler(userService, picsumService, photoService)
        )
    }

    @Bean
    fun commandHandlers(userService: UserService): Map<String, UpdateHandler> {
        return mapOf(
            "start" to StartCommandHandler(userService),
            "help" to HelpCommandHandler()
        )
    }

    @Bean
    fun queryCallBackHandlers(
        userService: UserService,
        photoService: PhotoService
    ): Map<String, UpdateHandler> {
        //TODO: move button commands to constants
        return mapOf(
            "add_photos" to AddPhotoCallBackHandler(userService),
            "photos_list" to PhotosListQueryHandler(photoService, userService),
            "get_photo" to GetPhotoQueryHandler(photoService, userService),
            "delete_photo" to DeletePhotoQueryHandler(photoService, userService),
            "confirm_delete" to ConfirmDeleteQueryHandler(photoService, userService),
            "update_author" to UpdateAuthorQueryHandler(userService),
            "main_menu" to MainMenuCallBackHandler(userService)
        )
    }
}