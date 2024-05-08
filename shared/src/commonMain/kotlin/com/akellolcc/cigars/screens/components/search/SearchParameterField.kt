/*******************************************************************************************************************************************
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 5/7/24, 10:51 PM
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

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import com.akellolcc.cigars.logging.Log
import com.akellolcc.cigars.mvvm.search.CigarsSearchFieldBaseViewModel
import com.akellolcc.cigars.screens.components.search.data.FilterParameter

abstract class SearchParameterField<T : Comparable<T>>(
    val parameter: FilterParameter<T>,
    var showLeading: Boolean = false,
    var enabled: Boolean = true,
    var onAction: ((CigarsSearchFieldBaseViewModel.Action) -> Unit)? = null
) {

    abstract fun validate(): Boolean

    @Composable
    abstract fun Content()

    abstract fun handleAction(event: Any, navigator: Navigator?)

    protected fun onAction(action: CigarsSearchFieldBaseViewModel.Action) {
        Log.debug("Send action: $action")
        onAction?.invoke(action)
    }
}