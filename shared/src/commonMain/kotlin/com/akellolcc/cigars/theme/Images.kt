package com.akellolcc.cigars.theme

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import com.akellolcc.cigars.MR
import dev.icerock.moko.resources.ImageResource
import dev.icerock.moko.resources.compose.painterResource

@Composable
fun loadIcon(id: ImageResource, size: Size, tint: Color? = null) {
    val painter: Painter = painterResource(id)
    Icon(
        painter = painter,
        contentDescription = "",
        modifier = Modifier.size(size.width.dp, size.height.dp),
        tint = tint ?:Color(LocalContentColor.current.value)
    )
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
        val default_background_borders = MR.images.wood_background_borders
        val default_cigar_image = MR.images.default_sigars

        val loading_spinner = MR.images.loading_spinner
        val tab_icon_humidors = MR.images.tab_icon_humidors
        val tab_icon_cigars = MR.images.tab_icon_cigars
        val tab_icon_favorites = MR.images.tab_icon_favorites

        val icon_menu = MR.images.icon_menu
        val icon_menu_dots = MR.images.icon_menu_dots
        val icon_menu_edit = MR.images.pencil
        val icon_menu_image = MR.images.icon_menu_image
        val icon_menu_camera = MR.images.icon_menu_camera
        val icon_menu_photos = MR.images.icon_menu_photos
    }
}


