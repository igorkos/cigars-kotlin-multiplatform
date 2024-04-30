/*******************************************************************************************************************************************
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 4/29/24, 3:28 PM
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

package com.akellolcc.cigars.screens.search

import TextStyled
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.text.input.ImeAction
import com.akellolcc.cigars.databases.rapid.rest.GetCigarsBrands
import com.akellolcc.cigars.databases.rapid.rest.RapidCigarBrand
import com.akellolcc.cigars.logging.Log
import com.akellolcc.cigars.theme.Images
import com.akellolcc.cigars.theme.TextStyles
import com.akellolcc.cigars.theme.loadIcon
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class CigarsSearchBrandField(
    parameter: FilterParameter<String>,
    showLeading: Boolean,
    onAction: ((SearchParameterAction, FilterParameter<String>) -> Flow<Any?>)?
) : SearchParameterField<String>(parameter, showLeading, onAction) {

    private var brandRequest: GetCigarsBrands? = null

    private fun getBrands(query: String, callback: (List<RapidCigarBrand>) -> Unit) {
        if (query.isNotBlank()) {
            if (brandRequest == null || brandRequest?.brand != query) {
                brandRequest = GetCigarsBrands(brand = query)
            }
            CoroutineScope(Dispatchers.IO).launch {
                brandRequest!!.next().collect {
                    callback(it)
                }
            }
        }
    }

    @Composable
    override fun Render(enabled: Boolean) {
        var expanded by remember { mutableStateOf(false) }
        var value by remember { mutableStateOf("") }
        var brands by mutableStateOf(listOf<RapidCigarBrand>())

        LaunchedEffect(parameter.value) {
            Log.debug("Brand search field: ${parameter.value}")
            getBrands(parameter.value) {
                Log.debug("Get Brands: $it")
                brands = it
                expanded = true
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (showLeading) {
                IconButton(
                    modifier = Modifier.wrapContentSize(),
                    onClick = { onAction(SearchParameterAction.Remove) }
                ) {
                    loadIcon(Images.icon_menu_delete, Size(12.0F, 12.0F))
                }
            }
            Column(
                modifier = Modifier.weight(1f),
            ) {
                TextStyled(
                    modifier = Modifier.fillMaxWidth(),
                    label = parameter.label,
                    text = value,
                    enabled = enabled,
                    editable = true,
                    style = TextStyles.Headline,
                    maxLines = 1,
                    onValueChange = {
                        value = it
                    },
                    onKeyboardAction = {
                        parameter.value = value
                    },
                    imeAction = ImeAction.Search
                )
                DropdownMenu(expanded = expanded,
                    onDismissRequest = { expanded = false }) {
                    brands.map {
                        DropdownMenuItem(
                            leadingIcon = {
                                loadIcon(Images.tab_icon_search, Size(24.0F, 24.0F))
                            },
                            text = {
                                TextStyled(
                                    it.name,
                                    TextStyles.Subhead
                                )
                            },
                            onClick = {
                                value = it.name ?: ""
                                parameter.value = value
                                onAction(SearchParameterAction.Completed)
                            }
                        )
                    }
                }
            }
        }
    }
}