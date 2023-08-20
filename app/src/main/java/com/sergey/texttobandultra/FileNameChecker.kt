package com.sergey.texttobandultra

import android.content.Context

const val errorSymbols = "\\/:*?\"<>|+—."

fun checkName(filename: String, context: Context, oldName: String = ""): String {
    if (filename.replace(" ", "").isEmpty())
        return context.getString(R.string.title_cannot_be_empty)
    if (filename in tabs.map { it.title } && filename != oldName)
        return context.getString(R.string.title_with_this_name_already_exists)
    if (filename.startsWith("."))
        return context.getString(R.string.title_can_not_started_with_dot)
    if (filename.endsWith(" "))
        return context.getString(R.string.title_can_not_ended_with_space)
    for (sym in errorSymbols) {
        if (sym in filename)
            return context.getString(R.string.symbol_sym_shouldt_use_in_title).replace('*', sym)
    }
    return ""
}