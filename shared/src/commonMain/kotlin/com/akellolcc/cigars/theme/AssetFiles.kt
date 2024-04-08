package com.akellolcc.cigars.theme

import com.akellolcc.cigars.MR
import dev.icerock.moko.resources.FileResource

expect fun readTextFile(file: FileResource): String?
class AssetFiles {
    companion object {
        val demo_humidors = MR.files.demo_humidors
        val demo_cigars_images = MR.files.demo_cigars_images
        val demo_cigars = MR.files.demo_cigars
    }
}