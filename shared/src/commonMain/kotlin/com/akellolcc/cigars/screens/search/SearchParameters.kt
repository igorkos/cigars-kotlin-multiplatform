/*******************************************************************************************************************************************
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 4/28/24, 10:22 PM
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


data class SearchParam<T : Comparable<T>>(val key: String, var value: T, val label: String, val icon: ImageResource) :
    Comparable<SearchParam<T>> {
    constructor(key: String, value: T) : this(key, value, "", Images.tab_icon_search)

    override fun compareTo(other: SearchParam<T>): Int {
        if (other == this) return 0
        return value.compareTo(other.value)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as SearchParam<*>

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

abstract class SearchParameters<T : Comparable<T>, R>() : Iterable<R> {
    protected lateinit var params: List<SearchParam<T>>
    protected var selectedParams: List<SearchParam<T>> = mutableListOf()
    val selected: List<SearchParam<T>>
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

    abstract fun build(param: SearchParam<T>): R

    val showLeading: Boolean
        get() = selectedParams.size > 1

    val availableFields: List<SearchParam<T>>
        get() {
            return params.filter { it !in selectedParams }
        }


    fun add(value: SearchParam<T>) {
        selectedParams = selectedParams + value
    }

    fun remove(value: SearchParam<T>) {
        selectedParams = selectedParams - value
    }

    fun clear() {
        selectedParams = mutableListOf()
    }

}