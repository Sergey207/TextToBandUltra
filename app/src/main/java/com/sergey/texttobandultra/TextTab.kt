package com.sergey.texttobandultra

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import com.google.gson.Gson
import java.io.File

const val UTF_SYM = '\uFEFF'
val tabsPath = File("")

data class TextTab(
    var title: String,
    var text: MutableState<String> = mutableStateOf(""),
    var toSave: MutableState<Boolean> = mutableStateOf(false)
)

fun MutableList<TextTab>.save() {
    for (tab in this) {
        tabsPath
            .resolve("${tab.title}.txt")
            .writeText("$UTF_SYM${tab.text}", Charsets.UTF_16LE)
        tab.toSave.value = false
    }
    tabsPath.resolve("tabs.json").writeText(
        Gson().toJson(this.map { "${it.title}.txt" })
    )
}

val tabs = mutableStateListOf<TextTab>()
val currentIndex = mutableStateOf(0)
