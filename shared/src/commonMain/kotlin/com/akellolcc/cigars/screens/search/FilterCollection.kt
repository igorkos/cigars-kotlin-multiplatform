/*******************************************************************************************************************************************
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 4/29/24, 1:18 PM
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

import com.akellolcc.cigars.theme.Images
import dev.icerock.moko.resources.ImageResource


data class FilterParameter<T>(val key: String, var value: T, val label: String, val icon: ImageResource) :
    Comparable<FilterParameter<T>> {
    constructor(key: String, value: T) : this(key, value, "", Images.tab_icon_search)

    override fun compareTo(other: FilterParameter<T>): Int {
        if (other == this) return 0
        if (key != other.key) return key.compareTo(other.key)
        when (value) {
            is String -> {
                return (value as String).compareTo(other.value as String)
            }

            is Long -> {
                return (value as Long).compareTo(other.value as Long)
            }

            is Double -> {
                return (value as Double).compareTo(other.value as Double)
            }

            is Boolean -> {
                return (value as Boolean).compareTo(other.value as Boolean)
            }

            else -> {
                return -1
            }
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as FilterParameter<*>

        if (key != other.key) return false
        if (value != other.value) return false

        return true
    }

    override fun hashCode(): Int {
        var result = key.hashCode()
        result = 31 * result + (value.hashCode())
        return result
    }
}

abstract class FilterCollection<T : Comparable<T>, out R> : Iterable<R> {
    protected lateinit var params: List<FilterParameter<T>>
    protected var selectedParams: List<FilterParameter<T>> = mutableListOf()
    val selected: List<FilterParameter<T>>
        get() = selectedParams

    override fun iterator(): Iterator<R> {
        return object : Iterator<R> {
            var index = 0
            override fun hasNext(): Boolean {
                return index < selectedParams.size
            }

            override fun next(): R {
                val param = selectedParams[index++]
                return build(param)
            }
        }
    }

    abstract fun build(param: FilterParameter<T>): R

    val showLeading: Boolean
        get() = selectedParams.size > 1

    val availableFields: List<FilterParameter<T>>
        get() {
            return params.filter { it !in selectedParams }
        }


    fun add(value: FilterParameter<T>) {
        selectedParams = selectedParams + value
    }

    fun remove(value: FilterParameter<T>) {
        selectedParams = selectedParams - value
    }

    fun clear() {
        selectedParams = mutableListOf()
    }

}