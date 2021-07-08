package com.kucherenko.picsum.bot

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton

enum class UserState() {
    ONBOARDING,
    FIRST_PHOTOS_DOWNLOAD,
    PHOTOS_DOWNLOAD,
    PHOTOS_SAVED,
    INCORRECT_LINK,
    FIRST_INCORRECT_LINK,
    MAIN_MENU,
    PHOTOS_LIST,
    GET_PHOTO,
    UPDATE_AUTHOR,
    DELETE_PHOTO,
    PHOTO_DELETED,
    AUTHOR_UPDATED
}

val BOT_CONTENT = mapOf(
    UserState.ONBOARDING to MessageContent("", null),
    UserState.FIRST_INCORRECT_LINK to MessageContent(
        "Incorrect link, try again",
        null
    ),
    UserState.FIRST_PHOTOS_DOWNLOAD to MessageContent(
        "Hi, this is onboarding...\n" +
                "Send me a specific format link to a list of photos from picsum.photos like this:\n" +
                "https://picsum.photos/v2/list?page=2&limit=100",
        null
    ),
    UserState.PHOTOS_DOWNLOAD to MessageContent(
        "Hi, this is onboarding...\n" +
                "Send me a specific format link to a list of photos from picsum.photos like this:\n" +
                "https://picsum.photos/v2/list?page=2&limit=100",
        getBackKeyboard("main_menu")
    ),
    UserState.INCORRECT_LINK to MessageContent(
        "Incorrect link, try again",
        getBackKeyboard("main_menu")
    ),
    UserState.MAIN_MENU to MessageContent(
        "Main menu",
        getMainMenuKeyboard()
    ),
    UserState.PHOTOS_SAVED to MessageContent(
        "Photos saved",
        null
    ),
    UserState.PHOTOS_LIST to MessageContent(
        "Photo List",
        null
    ),
    UserState.GET_PHOTO to MessageContent(
        "",
        null
    ),
    UserState.PHOTO_DELETED to MessageContent(
        "Photo was successfully deleted",
        null
    ),
    UserState.UPDATE_AUTHOR to MessageContent(
        "Enter a new author name",
        null
    ),
    UserState.AUTHOR_UPDATED to MessageContent(
        "The author successfully updated",
        null
    )
)

val LIST_OF_COMMANDS = listOf(
    "/start",
    "/help"
)

data class MessageContent(
    val text: String,
    val markup: InlineKeyboardMarkup?
)

fun getMainMenuKeyboard(): InlineKeyboardMarkup {
    val addPhotoButton = InlineKeyboardButton()
    addPhotoButton.text = "➕ Add new photos"
    addPhotoButton.callbackData = "add_photos"

    val photoListbutton = InlineKeyboardButton()
    photoListbutton.text = "📃 Photo list"
    photoListbutton.callbackData = "photos_list"

    val markup = InlineKeyboardMarkup()
    markup.keyboard = listOf(listOf(addPhotoButton), listOf(photoListbutton))
    return markup
}

fun getSkipKeyboard(buttonCommand: String): InlineKeyboardMarkup {
    val button = InlineKeyboardButton()
    button.text = "Skip"
    button.callbackData = buttonCommand
    val markup = InlineKeyboardMarkup()
    markup.keyboard = listOf(listOf(button))
    return markup
}

fun getGetPhotoKeyBoard(photoId: String): InlineKeyboardMarkup {
    val changeAuthorButton = InlineKeyboardButton()
    changeAuthorButton.text = "✏️ Update Author"
    changeAuthorButton.callbackData = "update_author/$photoId"

    val deletePhotoButton = InlineKeyboardButton()
    deletePhotoButton.text = "🗑 Delete"
    deletePhotoButton.callbackData = "delete_photo/$photoId"

    val getBackButton = InlineKeyboardButton()
    getBackButton.text = "🔙 Back"
    getBackButton.callbackData = "photos_list"

    val markup = InlineKeyboardMarkup()
    markup.keyboard = listOf(
        listOf(changeAuthorButton),
        listOf(deletePhotoButton),
        listOf(getBackButton)
    )
    return markup
}

fun getBackKeyboard(buttonCommand: String): InlineKeyboardMarkup {
    val button = InlineKeyboardButton()
    button.text = "🔙 Back"
    button.callbackData = buttonCommand
    val markup = InlineKeyboardMarkup()
    markup.keyboard = listOf(listOf(button))
    return markup
}

fun getAuthorButtonText(author: String, id: String) = "$author ($id)"

fun getPhotoDescriptionText(author: String, id: String, size: String, url: String, downloadUrl: String) = """
    Author: $author
    ID: $id
    Size: $size
    URL: $url
    Download URL: $downloadUrl
""".trimIndent()

fun getDeleteConfirmText(author: String, id: String) = """
    Are you sure you want to delete this photo?
    $author ($id)
""".trimIndent()

fun getDeleteConfirmKeyBoard(photoId: String): InlineKeyboardMarkup {
    val yesButton = InlineKeyboardButton()
    yesButton.text = "✅ Yes"
    yesButton.callbackData = "confirm_delete/$photoId"

    val noButton = InlineKeyboardButton()
    noButton.text = "❎ No"
    noButton.callbackData = "get_photo/$photoId"

    return InlineKeyboardMarkup().setKeyboard(listOf(listOf(yesButton, noButton)))
}

fun getUpdateAuthorKeyboard(photoId: String): InlineKeyboardMarkup {
    val button = InlineKeyboardButton()
    button.text = "🔙 Back"
    button.callbackData = "get_photo/$photoId"

    return InlineKeyboardMarkup().setKeyboard(listOf(listOf(button)))
}
