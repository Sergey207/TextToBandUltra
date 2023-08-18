package com.sergey.texttobandultra

const val errorSymbols = "\\/:*?\"<>|+—."

fun checkName(filename: String, oldName: String = ""): String {
    if (filename.replace(" ", "").isEmpty())
        return "Title can not be empty!"
    if (filename in tabs.map { it.title } && filename != oldName)
        return "Title with this name already exists!"
    if (filename.startsWith("."))
        return "Title can not started with ."
    if (filename.endsWith(" "))
        return "Title can not ended with space!"
    for (sym in errorSymbols) {
        if (sym in filename)
            return "Symbol $sym should`t use in title!"
    }
    return ""
}