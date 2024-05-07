/*******************************************************************************************************************************************
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 5/5/24, 11:08 PM
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

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.Navigator
import com.akellolcc.cigars.logging.Log
import com.akellolcc.cigars.mvvm.CigarsSearchFieldBaseViewModel
import com.akellolcc.cigars.mvvm.CigarsSearchFieldViewModel
import com.akellolcc.cigars.mvvm.createViewModel
import com.akellolcc.cigars.screens.components.TextStyled
import com.akellolcc.cigars.screens.search.data.FilterParameter
import com.akellolcc.cigars.theme.Images
import com.akellolcc.cigars.theme.TextStyles
import com.akellolcc.cigars.theme.loadIcon

class CigarsSearchParameterField<T : Comparable<T>>(
    parameter: FilterParameter<T>,
    showLeading: Boolean = false,
    enabled: Boolean = true,
    onAction: ((SearchParameterAction, FilterParameter<T>) -> Unit)? = null
) : SearchParameterField<T>(parameter, showLeading, enabled, onAction) {
    override fun handleAction(event: Any, navigator: Navigator?) {
        Log.debug("Handle action: $event")
        when (event) {
            is CigarsSearchFieldBaseViewModel.Action.Selected -> {
                onAction(SearchParameterAction.Completed)
            }

            else -> {}
        }
    }

    private val viewModel = createViewModel(CigarsSearchFieldViewModel::class, parameter)

    override fun validate(): Boolean {
        val valid = viewModel.validate()
        Log.debug("validate(${parameter.key}): $valid")
        return valid
    }

    @Composable
    override fun Content() {
        Log.debug("Compose: error: ${viewModel.isError} ")

        LaunchedEffect(Unit) {
            viewModel.observeEvents {
                Log.debug("Control state: $it")
                handleAction(it, null)
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
            TextStyled(
                modifier = Modifier.weight(1f).onFocusChanged {
                    viewModel.onFocusChange(it.isFocused)
                },
                label = parameter.label,
                text = viewModel.value,
                enabled = enabled,
                editable = true,
                style = TextStyles.Headline,
                maxLines = 1,
                onValueChange = {
                    viewModel.value = it
                },
                onKeyboardAction = {
                    viewModel.onCompleted()
                },
                imeAction = ImeAction.Search,
                isError = viewModel.isError,
                inputMode = viewModel.keyboardType,
                supportingText = if (!viewModel.isError) null else {
                    @Composable {
                        TextStyled(
                            modifier = Modifier.fillMaxWidth().padding(top = 4.dp, bottom = 4.dp),
                            text = viewModel.annotation,
                            style = TextStyles.Error
                        )
                    }
                }
            )
        }
    }

}