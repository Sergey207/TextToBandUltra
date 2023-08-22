package com.sergey.texttobandultra

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf

data class TextTab(
    var title: String,
    var text: MutableState<String> = mutableStateOf(""),
    var toSave: MutableState<Boolean> = mutableStateOf(true)
) {
    fun renameTab(context: Context, newName: String) {
        val tabsFolder = getTabsFolder(context)
        tabsFolder.resolve("$title.txt").delete()
        tabsFolder.resolve("$newName.txt").writeText(
            "${'\uFEFF'}${text.value}",
            Charsets.UTF_16LE
        )
        title = newName
        saveTabs(context)
    }

    fun delete(context: Context) {
        val tabsPath = getTabsFolder(context)
        val tabPath = tabsPath.resolve("$title.txt")
        if (tabPath.exists())
            tabPath.delete()
    }

    fun writeToFile(context: Context) {
        val tabsPath = getTabsFolder(context)
        tabsPath.resolve("$title.txt").writeText(
            "${'\uFEFF'}${text.value}",
            Charsets.UTF_16LE
        )
        toSave.value = false
        saveTabs(context)
    }
}

val tabs = mutableStateListOf<TextTab>()
val currentIndex = mutableStateOf(0)
