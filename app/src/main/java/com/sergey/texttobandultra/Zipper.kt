import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

fun zip(unzipped: File, zipped: File) {
    if (!unzipped.exists() || !unzipped.isDirectory) return
    ZipOutputStream(BufferedOutputStream(FileOutputStream(zipped))).use { zos ->
        unzipped.walkTopDown().filter { it.absolutePath != unzipped.absolutePath }.forEach { file ->
            val zipFileName =
                file.absolutePath.removePrefix(unzipped.absolutePath).removePrefix(File.separator)
            val entry = ZipEntry("$zipFileName${(if (file.isDirectory) "/" else "")}")
            zos.putNextEntry(entry)
            if (file.isFile) file.inputStream().use { it.copyTo(zos) }
        }
    }
}
