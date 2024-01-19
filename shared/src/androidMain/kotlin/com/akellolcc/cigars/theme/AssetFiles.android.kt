package com.akellolcc.cigars.theme

import com.akellolcc.cigars.utils.appContext
import dev.icerock.moko.resources.FileResource

actual fun readTextFile(file: FileResource): String? {
    var text: String? = null;
     appContext?.let { text = file.readText(it) }
    return text;
}