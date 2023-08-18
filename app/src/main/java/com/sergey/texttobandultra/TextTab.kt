package com.sergey.texttobandultra

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf

data class TextTab(
    var title: String,
    var text: MutableState<String> = mutableStateOf("")
)

val tabs = mutableStateListOf<TextTab>()
val currentIndex = mutableStateOf(0)
