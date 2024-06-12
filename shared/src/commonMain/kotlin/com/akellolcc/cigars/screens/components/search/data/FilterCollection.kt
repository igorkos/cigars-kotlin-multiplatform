/*******************************************************************************************************************************************
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 6/11/24, 5:06 PM
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

package com.akellolcc.cigars.screens.components.search.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.akellolcc.cigars.screens.components.search.SearchParameterField

open class FiltersList(val list: List<FilterParameter<*>> = emptyList()) : List<FilterParameter<*>> by list {
    companion object {
        inline fun <reified T> getSQLWhere(list: FiltersList?, key: String): T {
            var entry: FilterParameter<*>? = list?.find { it.key == key }
            if (entry != null) {
                return when (entry.value) {
                    is Int -> entry.value as T
                    is Long -> entry.value as T
                    is Double -> entry.value as T
                    is Boolean -> entry.value as T
                    else -> "%${entry.value}%" as T
                }
            }
            return when (T::class) {
                Int::class -> 0 as T
                Long::class -> 0L as T
                Double::class -> 0.0 as T
                Boolean::class -> false as T
                else -> "%%" as T
            }
        }
    }

    fun hasParameter(key: String): Boolean {
        return list.find { it.key == key } != null
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is FiltersList) return false
        return compareFilterParameters(list, other.list)
    }

    override fun hashCode(): Int {
        return list.hashCode()
    }

    override fun toString(): String {
        return "FiltersList(list=$list)"
    }

    fun append(items: List<FilterParameter<*>>, tail: Boolean = true): FiltersList {
        val updated = if (tail) list + items else items + list
        return FiltersList(updated)
    }

    internal infix operator fun plus(other: FilterParameter<*>): FiltersList {
        return FiltersList(list + other)
    }

    internal infix operator fun minus(other: FilterParameter<*>): FiltersList {
        return FiltersList(list - other)
    }
}

abstract class FilterCollection {
    protected var params = FiltersList()
    protected var selectedParams = FiltersList()

    val selected: FiltersList
        get() {
            return selectedParams
        }

    var controls: List<SearchParameterField<*>> by mutableStateOf(listOf())

    abstract fun build(param: FilterParameter<*>): SearchParameterField<*>

    fun validate(): Boolean {
        return controls.all { it.validate() }
    }

    val showLeading: Boolean
        get() = selectedParams.size > 1

    val availableFields: List<FilterParameter<*>>
        get() {
            return params.filter { it !in selectedParams }
        }


    fun add(value: FilterParameter<*>) {
        controls = controls + build(value)
        selectedParams = (selectedParams + value) as FiltersList
    }

    fun remove(value: FilterParameter<*>) {
        selectedParams = (selectedParams - value) as FiltersList

        val control = controls.firstOrNull { it.parameter == value }
        control?.let {
            controls = controls - it
        }
    }

    fun clear() {
        selectedParams = FiltersList()
        controls = listOf()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is FilterCollection) return false
        return compareFilterParameters(selectedParams, other.selectedParams)
    }

    override fun hashCode(): Int {
        var result = params.hashCode()
        result = 31 * result + selectedParams.hashCode()
        return result
    }

    override fun toString(): String {
        return "FilterCollection(selectedParams=$selectedParams)"
    }
}

