package com.sergey.texttobandultra

import android.content.Context
import android.util.Log
import zip
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream


fun saveApp(context: Context) {
    if (!basePath.exists()) basePath.mkdir()

    for (file in basePath.listFiles()!!)
        if (file.name != "tabs")
            file.deleteRecursively()

    val appPath = basePath.resolve("app")
    appPath.mkdir()

    copyApp(context, appPath)

    val assetsPath = appPath.resolve("assets")

    for (el in tabs) {
        assetsPath
            .resolve("${el.title}.txt")
            .writeText("${UTF_SYM}${el.text.value}", Charsets.UTF_16LE)
    }

    assetsPath
        .resolve("pages.txt")
        .writeText(
            UTF_SYM + tabs.joinToString(",") { "${it.title}.txt" },
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

