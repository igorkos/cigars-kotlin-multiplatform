package com.akellolcc.cigars.components

import TextStyled
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.akellolcc.cigars.theme.MaterialColors
import com.akellolcc.cigars.theme.TextStyles
import com.akellolcc.cigars.theme.materialColor
import com.akellolcc.cigars.theme.textStyle

@Composable
fun <T> ValuePicker(modifier: Modifier = Modifier, label: String?, value: String?, items: Array<Pair<T,String>>, onClick: ((Pair<T,String>) -> Unit)? = null)  {
    var expanded by remember { mutableStateOf(false) }
    var selected by remember { mutableStateOf(value) }
    var with by remember { mutableStateOf(0) }
    Column(
        modifier = modifier.fillMaxSize().clickable(onClick = {
            expanded = true
        }).height(60.dp).background(materialColor(MaterialColors.color_surfaceVariant),TextFieldDefaults.shape),
        horizontalAlignment = Alignment.Start
    ) {
        Column(modifier = Modifier.padding(top = 4.dp, start = 16.dp, end = 16.dp, bottom = 4.dp).onSizeChanged {
            with = it.width
        }) {
            CompositionLocalProvider(
                LocalContentColor provides materialColor(MaterialColors.color_onSurfaceVariant)
            ){
                TextStyled(
                    label,
                    TextStyles.Description,
                )
            }
            TextStyled(
                selected,
                TextStyles.Subhead,
            )
        }

        DropdownMenu(
            expanded = expanded,
            offset = DpOffset(16.dp, 0.dp),
            modifier = Modifier.width(with.dp).background(materialColor(MaterialColors.color_surfaceVariant)),
            onDismissRequest = { expanded = false }
        ) {
            items.forEach {
                DropdownMenuItem(
                    text = {
                        TextStyled(
                            it.second,
                            TextStyles.Subhead
                        )
                    },
                    onClick = {
                        expanded = false
                        selected = it.second
                        onClick?.invoke(it)
                    }
                )
            }
        }
    }
}

