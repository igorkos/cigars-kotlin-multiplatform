package com.akellolcc.cigars.theme

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import dev.icerock.moko.resources.ImageResource
import dev.icerock.moko.resources.compose.painterResource
import com.akellolcc.cigars.MR

@Composable
fun loadIcon(id: ImageResource, size: Size) {
    val painter: Painter = painterResource(id)
    Icon(painter = painter, contentDescription = "", modifier = Modifier.size(size.width.dp, size.height.dp))
}

@Composable
fun loadImage(id: ImageResource) {
    val painter: Painter = painterResource(id)
    Image(painter = painter, contentDescription = "")
}

@Composable
fun imagePainter(id: ImageResource): Painter {
    return painterResource(id)
}

class Images {
    companion object {
        val splash_background = MR.images.humidor_image_bg
        val splash_plate = MR.images.plate

        val default_background = MR.images.wood_background

        val loading_spinner = MR.images.loading_spinner
        val tab_icon_humidors = MR.images.tab_icon_humidors
        val tab_icon_cigars = MR.images.tab_icon_cigars
        val tab_icon_favorites = MR.images.tab_icon_favorites
    }
}


