/*******************************************************************************************************************************************
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 5/31/24, 1:10 PM
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************************************************************************/

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
import cigars.composeapp.generated.resources.Res
import cigars.composeapp.generated.resources.allDrawableResources
import cigars.composeapp.generated.resources.arrow_left
import cigars.composeapp.generated.resources.arrow_right
import cigars.composeapp.generated.resources.cigar_ratings_info
import cigars.composeapp.generated.resources.cigar_sizes_info
import cigars.composeapp.generated.resources.cigar_tobacco_info
import cigars.composeapp.generated.resources.default_sigars
import cigars.composeapp.generated.resources.icon_bin
import cigars.composeapp.generated.resources.icon_cigars
import cigars.composeapp.generated.resources.icon_drop_down
import cigars.composeapp.generated.resources.icon_drop_up
import cigars.composeapp.generated.resources.icon_menu
import cigars.composeapp.generated.resources.icon_menu_camera
import cigars.composeapp.generated.resources.icon_menu_checkmark
import cigars.composeapp.generated.resources.icon_menu_delete
import cigars.composeapp.generated.resources.icon_menu_dots
import cigars.composeapp.generated.resources.icon_menu_history
import cigars.composeapp.generated.resources.icon_menu_image
import cigars.composeapp.generated.resources.icon_menu_info
import cigars.composeapp.generated.resources.icon_menu_minus
import cigars.composeapp.generated.resources.icon_menu_plus
import cigars.composeapp.generated.resources.icon_menu_sort
import cigars.composeapp.generated.resources.icon_menu_sort_alpha_asc
import cigars.composeapp.generated.resources.icon_menu_sort_alpha_desc
import cigars.composeapp.generated.resources.icon_menu_sort_amount_asc
import cigars.composeapp.generated.resources.icon_menu_sort_amount_desc
import cigars.composeapp.generated.resources.icon_menu_sort_numberic_desc
import cigars.composeapp.generated.resources.icon_menu_sort_numeric_asc
import cigars.composeapp.generated.resources.icon_question
import cigars.composeapp.generated.resources.icon_star_empty
import cigars.composeapp.generated.resources.icon_star_full
import cigars.composeapp.generated.resources.icon_tab
import cigars.composeapp.generated.resources.loading_spinner
import cigars.composeapp.generated.resources.pencil
import cigars.composeapp.generated.resources.tab_icon_cigars
import cigars.composeapp.generated.resources.tab_icon_favorites
import cigars.composeapp.generated.resources.tab_icon_humidors
import cigars.composeapp.generated.resources.tab_icon_search
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun loadIcon(
    id: DrawableResource,
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
fun loadImage(id: DrawableResource) {
    val painter: Painter = painterResource(id)
    Image(painter = painter, contentDescription = "")
}

@Composable
fun imagePainter(id: DrawableResource): Painter {
    return painterResource(id)
}

fun loadImageByName(name: String): DrawableResource? {
    val res = Res.allDrawableResources.entries.find { it.key == name  }
    return res?.value
}

expect suspend fun imageData(name: String): ByteArray?

class Images {
    companion object {

        val default_cigar_image = Res.drawable.default_sigars

        val loading_spinner = Res.drawable.loading_spinner
        val tab_icon_humidors = Res.drawable.tab_icon_humidors
        val tab_icon_cigars = Res.drawable.tab_icon_cigars
        val tab_icon_favorites = Res.drawable.tab_icon_favorites

        val icon_menu = Res.drawable.icon_menu
        val icon_menu_delete = Res.drawable.icon_menu_delete
        val icon_menu_info = Res.drawable.icon_menu_info
        val icon_menu_minus = Res.drawable.icon_menu_minus
        val icon_menu_plus = Res.drawable.icon_menu_plus
        val icon_menu_dots = Res.drawable.icon_menu_dots
        val icon_menu_edit = Res.drawable.pencil
        val icon_menu_image = Res.drawable.icon_menu_image
        val icon_menu_camera = Res.drawable.icon_menu_camera
        val icon_star_empty = Res.drawable.icon_star_empty
        val icon_star_filled = Res.drawable.icon_star_full
        val icon_menu_history = Res.drawable.icon_menu_history
        val icon_cigars = Res.drawable.icon_cigars
        val icon_arrow_left = Res.drawable.arrow_left
        val icon_arrow_right = Res.drawable.arrow_right
        val icon_bin = Res.drawable.icon_bin
        val icon_question = Res.drawable.icon_question
        val icon_tab = Res.drawable.icon_tab

        val cigar_sizes_info = Res.drawable.cigar_sizes_info
        val cigar_tobacco_info = Res.drawable.cigar_tobacco_info
        val cigar_ratings_info = Res.drawable.cigar_ratings_info

        val icon_menu_sort_alpha_asc = Res.drawable.icon_menu_sort_alpha_asc
        val icon_menu_sort_alpha_desc = Res.drawable.icon_menu_sort_alpha_desc
        val icon_menu_sort_amount_asc = Res.drawable.icon_menu_sort_amount_asc
        val icon_menu_sort_amount_desc = Res.drawable.icon_menu_sort_amount_desc
        val icon_menu_sort_numeric_desc = Res.drawable.icon_menu_sort_numberic_desc
        val icon_menu_sort_numeric_asc = Res.drawable.icon_menu_sort_numeric_asc
        val icon_menu_sort = Res.drawable.icon_menu_sort
        val tab_icon_search = Res.drawable.tab_icon_search
        val icon_menu_checkmark = Res.drawable.icon_menu_checkmark
        val icon_drop_down = Res.drawable.icon_drop_down
        val icon_drop_up = Res.drawable.icon_drop_up
    }
}


