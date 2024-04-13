package com.akellolcc.cigars.components

import TextStyled
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.akellolcc.cigars.logging.Log
import com.akellolcc.cigars.theme.MaterialColors
import com.akellolcc.cigars.theme.TextStyles
import com.akellolcc.cigars.theme.loadIcon
import com.akellolcc.cigars.theme.materialColor
import com.akellolcc.cigars.ui.dpToPx
import com.akellolcc.cigars.ui.pxToDp
import com.akellolcc.cigars.ui.screenWidth
import dev.icerock.moko.resources.ColorResource
import dev.icerock.moko.resources.ImageResource

data class ValuePickerItem<T : Comparable<T>>(
    val value: T?,
    val label: String,
    val icon: ImageResource?
)

@Composable
fun <T : Comparable<T>> ValuePicker(
    modifier: Modifier = Modifier,
    label: String?,
    value: ValuePickerItem<T>?,
    items: List<ValuePickerItem<T>>,
    onClick: ((ValuePickerItem<T>) -> Unit)? = null,
    backgroundColor: ColorResource? = null
) {
    var expanded by remember { mutableStateOf(false) }
    var selected by remember { mutableStateOf(value) }
    var with by remember { mutableStateOf(0) }
    val screenWidth = screenWidth()
    LaunchedEffect(items) {
        if (items.size == 1 && items[0].value != null && selected?.value?.compareTo(items[0].value!!) != 0) {
            selected = items[0]
            onClick?.invoke(items[0])
        }
    }
    Column(
        modifier = modifier.fillMaxWidth().clickable(onClick = {
            expanded = true
        }).height(60.dp).background(
            materialColor(backgroundColor ?: MaterialColors.color_surfaceVariant),
            TextFieldDefaults.shape
        ).onSizeChanged {
            with = it.width
           Log.debug("width ${it.width.dp} $screenWidth")
        },
        horizontalAlignment = Alignment.Start
    ) {
        Column(
            modifier = Modifier.padding(top = 4.dp, start = 16.dp, end = 16.dp, bottom = 4.dp).wrapContentWidth()
                .onSizeChanged {
                    Log.debug("1 width ${it.width.dp}")
                   // with = it.width.dp
                }) {
            CompositionLocalProvider(
                LocalContentColor provides materialColor(MaterialColors.color_onSurfaceVariant)
            ) {
                TextStyled(
                    label,
                    TextStyles.Description,
                )
            }
            TextStyled(
                selected?.label,
                TextStyles.Subhead,
            )
        }
        if (items.size > 1) {
            DropdownMenu(
                expanded = expanded,
                modifier = Modifier.width(with.pxToDp()).requiredSizeIn(maxHeight = 200.dp)
                    .background(materialColor(MaterialColors.color_surfaceVariant)),
                onDismissRequest = { expanded = false }
            ) {
                items.forEach {
                    DropdownMenuItem(
                        modifier = Modifier.width(with.pxToDp()),
                        leadingIcon = { if (it.icon != null) loadIcon(it.icon) },
                        text = {
                            TextStyled(
                                it.label,
                                TextStyles.Subhead
                            )
                        },
                        onClick = {
                            expanded = false
                            selected = it
                            onClick?.invoke(it)
                        }
                    )
                }
            }
        }
    }
}

