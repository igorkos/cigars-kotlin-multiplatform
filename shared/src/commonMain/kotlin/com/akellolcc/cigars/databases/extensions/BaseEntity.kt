/*
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 4/21/24, 1:08 PM
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
 */

package com.akellolcc.cigars.databases.extensions

import androidx.compose.runtime.Stable
import com.akellolcc.cigars.utils.randomString
import kotlinx.serialization.Serializable

@Stable
@Serializable
abstract class BaseEntity : Comparable<BaseEntity> {
    abstract var rowid: Long
    val key: String = randomString()

    override fun hashCode(): Int = rowid.hashCode()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as BaseEntity

        return key == other.key
    }

    override operator fun compareTo(other: BaseEntity): Int {
        return rowid.compareTo(other.rowid)
    }
}
