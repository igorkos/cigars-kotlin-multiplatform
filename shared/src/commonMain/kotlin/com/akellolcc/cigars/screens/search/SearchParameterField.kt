/*******************************************************************************************************************************************
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 4/29/24, 12:21 AM
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

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

enum class SearchParameterAction {
    Add,
    Remove,
    Completed,
    LoadData
}

abstract class SearchParameterField<T : Comparable<T>>(
    val parameter: SearchParam<T>,
    var showLeading: Boolean = false,
    var onAction: ((SearchParameterAction, SearchParam<T>) -> Flow<Any?>)? = null
) {
    var value by mutableStateOf("")

    @Composable
    abstract fun Render(enabled: Boolean)

    fun validate(): Boolean {
        return true
    }

    fun dropDownMenu(): List<Pair<T, String>>? {
        return null
    }

    protected fun onAction(action: SearchParameterAction) {
        CoroutineScope(Dispatchers.IO).launch {
            onAction?.invoke(action, parameter)?.collect {}
        }
    }
}
