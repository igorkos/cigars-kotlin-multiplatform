/*******************************************************************************************************************************************
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 6/11/24, 7:03 PM
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

package com.akellolcc.cigars.screens.components.search

import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import com.akellolcc.cigars.screens.components.TextStyled
import com.akellolcc.cigars.screens.components.search.data.FilterParameter
import com.akellolcc.cigars.screens.components.search.data.FiltersList
import com.akellolcc.cigars.theme.Images
import com.akellolcc.cigars.theme.Localize
import com.akellolcc.cigars.theme.TextStyles
import com.akellolcc.cigars.theme.loadIcon

@Composable
fun TopBarActionsMenu(menuItems: FiltersList?, onClick: (FilterParameter<*>) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    var selected by remember { mutableStateOf<FilterParameter<*>?>(null) }
    var action by remember { mutableStateOf<FilterParameter<*>?>(null) }
    var click by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        menuItems?.first {
            it.selectable
        }?.let {
            selected = it
            onClick(it)
        }
    }

    LaunchedEffect(click) {
        action?.let { onClick(it) }
    }

    IconButton(modifier = Modifier.semantics { contentDescription = Localize.screen_list_sort_fields_action_descr },
        onClick = { expanded = !expanded }) {
        loadIcon(Images.icon_menu, Size(24.0F, 24.0F))
        DropdownMenu(
            modifier = Modifier.semantics { contentDescription = Localize.screen_list_sort_fields_list_descr },
            expanded = expanded,
            onDismissRequest = { expanded = false }) {
            menuItems?.map {
                if (it.key == Localize.cigar_details_top_bar_divider_desc) {
                    HorizontalDivider()
                } else {
                    DropdownMenuItem(
                        leadingIcon = {
                            val icon = if (it.selectable) {
                                if (selected == it) {
                                    Images.icon_menu_checkmark
                                } else {
                                    it.icon
                                }
                            } else {
                                it.icon
                            }
                            if (icon != null) {
                                loadIcon(icon, Size(24.0F, 24.0F))
                            }
                        },
                        text = {
                            TextStyled(
                                it.label,
                                Localize.list_sorting_item,
                                TextStyles.Subhead,
                                labelStyle = TextStyles.None
                            )
                        },
                        onClick = {
                            expanded = false
                            if (it.selectable) {
                                selected = it
                            }
                            action = it
                            click = !click
                        }
                    )
                }
            }
        }
    }

}