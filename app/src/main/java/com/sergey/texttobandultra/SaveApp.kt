package com.sergey.texttobandultra

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import com.google.gson.Gson
import zip
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream


fun saveApp(context: Context) {
    val basePath = context.getExternalFilesDir(null) ?: return
    if (!basePath.exists()) basePath.mkdir()

    for (file in basePath.listFiles()!!) {
        if (file.name != "tabs")
            file.deleteRecursively()
    }

    val appPath = basePath.resolve("app")
    appPath.mkdir()

    copyApp(context, appPath)

    val assetsPath = appPath.resolve("assets")

    for (el in tabs) {
        assetsPath
            .resolve("${el.title}.txt")
            .writeText("${'\uFEFF'}${el.text.value}", Charsets.UTF_16LE)
    }

    assetsPath
        .resolve("pages.txt")
        .writeText(
            '\uFEFF' + tabs.joinToString(",") { "${it.title}.txt" },
            Charsets.UTF_16LE
        )

    val zipPath = basePath.resolve("app.zip")

    zip(appPath, zipPath)
    zipPath.renameTo(basePath.resolve("app.bin"))
    appPath.deleteRecursively()
}


fun copyFile(inputStream: InputStream, outputStream: FileOutputStream) {
    val buffer = ByteArray(1024)
    var length = inputStream.read(buffer)
    while (length > 0) {
        outputStream.write(buffer, 0, length)
        length = inputStream.read(buffer)
    }
    inputStream.close()
    outputStream.close()
}

fun copyApp(context: Context, basePath: File) {
    basePath.resolve("assets").mkdir()
    basePath.resolve("assets").resolve("images").mkdir()

    basePath.resolve("page").mkdir()
    basePath.resolve("page").resolve("192x490_s_l66").mkdir()
    basePath.resolve("page").resolve("192x490_s_l66").resolve("home").mkdir()

    val assets = context.assets
    val names = mutableListOf("app.bin", "app.json")
    names.addAll((assets.list("assets/images") ?: emptyArray()).map { "assets/images/$it" })
    names.addAll((assets.list("page/192x490_s_l66/home")
        ?: emptyArray()).map { "page/192x490_s_l66/home/$it" })
    Log.d("MyLog", names.joinToString("|"))
    for (name in names) {
        copyFile(assets.open(name), FileOutputStream(basePath.resolve(name)))
    }
}

fun getTabsFolder(context: Context): File {
    val basePath = context.getExternalFilesDir(null) ?: return File("")
    val tabsPath = basePath.resolve("tabs")
    if (!tabsPath.exists())
        tabsPath.mkdir()
    if (!(tabsPath.resolve("tabs.json").exists()))
        tabsPath.resolve("tabs.json").writeText("[]")
    return tabsPath
}


fun saveTabs(context: Context) {
    val tabsPath = getTabsFolder(context)
    for (tab in tabs) {
        tab.toSave.value = false
        tabsPath.resolve("${tab.title}.txt").writeText(tab.text.value)
    }

    val gson = Gson()
    tabsPath.resolve("tabs.json").writeText(gson.toJson(tabs.map { it.title }))
}

fun getTabs(context: Context): MutableList<TextTab> {
    val tabsPath = getTabsFolder(context)
    val gson = Gson()
    val tabNames = gson.fromJson(
        tabsPath.resolve("tabs.json").readText(),
        mutableListOf<String>()::class.java
    )

    val res = mutableListOf<TextTab>()
    for (tabName in tabNames) {
        val tabFile = tabsPath.resolve("${tabName}.txt")
        if (!tabFile.exists())
            tabFile.writeText("")

        res.add(
            TextTab(
                tabName,
                mutableStateOf(tabFile.readText().drop(1)),
                toSave = mutableStateOf(false)
            )
        )
    }
    return res
}
