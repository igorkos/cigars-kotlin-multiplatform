package com.akellolcc.cigars.theme

import dev.icerock.moko.resources.FileResource

actual fun readTextFile(file: FileResource): String? {
    return file.readText()
}