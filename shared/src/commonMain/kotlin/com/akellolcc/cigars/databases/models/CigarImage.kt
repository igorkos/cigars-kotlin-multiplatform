/*******************************************************************************************************************************************
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 5/31/24, 8:18 PM
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

package com.akellolcc.cigars.databases.models

import androidx.compose.runtime.Stable
import kotlinx.serialization.Serializable

@Stable
@Serializable
data class CigarImage(
    override var rowid: Long,
    val image: String? = null,
    var bytes: ByteArray,
    var notes: String? = null,
    var type: Long? = null,
    var cigarId: Long? = null,
    var humidorId: Long? = null
) : BaseEntity() {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false
        if (!super.equals(other)) return false

        other as CigarImage

        if (rowid != other.rowid) return false
        if (image != other.image) return false
        if (notes != other.notes) return false
        if (type != other.type) return false
        if (cigarId != other.cigarId) return false
        if (humidorId != other.humidorId) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + rowid.hashCode()
        result = 31 * result + (image?.hashCode() ?: 0)
        result = 31 * result + (notes?.hashCode() ?: 0)
        result = 31 * result + (type?.hashCode() ?: 0)
        result = 31 * result + (cigarId?.hashCode() ?: 0)
        result = 31 * result + (humidorId?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "CigarImage(rowid=$rowid, image=$image, bytes=${bytes.size}, notes=$notes, type=$type, cigarId=$cigarId, humidorId=$humidorId)"
    }

}