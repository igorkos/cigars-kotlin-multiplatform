/*******************************************************************************************************************************************
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 5/4/24, 10:44 AM
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

package com.akellolcc.cigars.screens.search.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.akellolcc.cigars.screens.search.SearchParameterField


abstract class FilterCollection {
    protected lateinit var params: List<FilterParameter<*>>
    protected var selectedParams: List<FilterParameter<*>> = mutableListOf()

    val selected: List<FilterParameter<*>>
        get() {
            return selectedParams
        }

    var controls: List<SearchParameterField<*>> by mutableStateOf(listOf())

    abstract fun build(param: FilterParameter<*>): SearchParameterField<*>

    val showLeading: Boolean
        get() = selectedParams.size > 1

    val availableFields: List<FilterParameter<*>>
        get() {
            return params.filter { it !in selectedParams }
        }


    fun add(value: FilterParameter<*>) {
        controls = controls + build(value)
        selectedParams = selectedParams + value
    }

    fun remove(value: FilterParameter<*>) {
        selectedParams = selectedParams - value

        val control = controls.firstOrNull { it.parameter == value }
        control?.let {
            controls = controls - it
        }
    }

    fun clear() {
        selectedParams = mutableListOf()
        controls = listOf()
    }

}