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
import dev.icerock.moko.resources.getImageByFileName

@Composable
fun loadIcon(
    id: ImageResource,
    size: Size = Size(24f, 24f),
    tint: Color? = null,
    modifier: Modifier = Modifier
) {
    val painter: Painter = painterResource(id)
    Icon(
        painter = painter,
        contentDescription = "",
        modifier = modifier.size(size.width.dp, size.height.dp),
        tint = tint ?: Color(LocalContentColor.current.value)
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

fun loadImageByName(name: String): ImageResource? {
    return MR.images.getImageByFileName(name)
}

expect fun imageData(name: String): ByteArray?

class Images {
    companion object {

        val default_cigar_image = MR.images.default_sigars

        val loading_spinner = MR.images.loading_spinner
        val tab_icon_humidors = MR.images.tab_icon_humidors
        val tab_icon_cigars = MR.images.tab_icon_cigars
        val tab_icon_favorites = MR.images.tab_icon_favorites

        val icon_menu = MR.images.icon_menu
        val icon_menu_delete = MR.images.icon_menu_delete
        val icon_menu_info = MR.images.icon_menu_info
        val icon_menu_minus = MR.images.icon_menu_minus
        val icon_menu_plus = MR.images.icon_menu_plus
        val icon_menu_dots = MR.images.icon_menu_dots
        val icon_menu_edit = MR.images.pencil
        val icon_menu_image = MR.images.icon_menu_image
        val icon_menu_camera = MR.images.icon_menu_camera
        val icon_star_empty = MR.images.icon_star_empty
        val icon_star_filled = MR.images.icon_star_full
        val icon_menu_history = MR.images.icon_menu_history

        val icon_arrow_left = MR.images.arrow_left
        val icon_arrow_right = MR.images.arrow_right
        val icon_bin = MR.images.icon_bin
        val icon_question = MR.images.icon_question
        val icon_tab = MR.images.icon_tab

        val cigar_sizes_info = MR.images.cigar_sizes_info
        val cigar_tobacco_info = MR.images.cigar_tobacco_info
        val cigar_ratings_info = MR.images.cigar_ratings_info

        val icon_menu_sort_alpha_asc = MR.images.icon_menu_sort_alpha_asc
        val icon_menu_sort_alpha_desc = MR.images.icon_menu_sort_alpha_desc
        val icon_menu_sort_amount_asc = MR.images.icon_menu_sort_amount_asc
        val icon_menu_sort_amount_desc = MR.images.icon_menu_sort_amount_desc
        val icon_menu_sort_numeric_desc = MR.images.icon_menu_sort_numberic_desc
        val icon_menu_sort_numeric_asc = MR.images.icon_menu_sort_numeric_asc
        val icon_menu_sort = MR.images.icon_menu_sort
        val tab_icon_search = MR.images.tab_icon_search
    }
}


