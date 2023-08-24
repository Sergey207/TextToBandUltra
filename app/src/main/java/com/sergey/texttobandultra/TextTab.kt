package com.sergey.texttobandultra

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import com.google.gson.Gson
import java.io.File

const val UTF_SYM = '\uFEFF'

var basePath = File("")
var tabsPath = File("")

data class TextTab(
    var title: String,
    var text: MutableState<String> = mutableStateOf(""),
    var toSave: MutableState<Boolean> = mutableStateOf(false)
)

fun MutableList<TextTab>.save(clear: Boolean = false) {
    if (clear) {
        tabsPath.deleteRecursively()
        tabsPath.mkdir()
    }

    for (tab in this) {
        tabsPath
            .resolve("${tab.title}.txt")
            .writeText("$UTF_SYM${tab.text.value}", Charsets.UTF_16LE)
        tab.toSave.value = false
    }
    tabsPath.resolve("tabs.json").writeText(
        Gson().toJson(this.map { it.title })
    )
}

val tabs = mutableStateListOf<TextTab>()
val currentIndex = mutableStateOf(0)
