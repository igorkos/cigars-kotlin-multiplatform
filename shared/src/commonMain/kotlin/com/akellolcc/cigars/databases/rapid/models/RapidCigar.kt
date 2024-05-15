/*******************************************************************************************************************************************
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 5/15/24, 1:46 PM
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

package com.akellolcc.cigars.databases.rapid.models

import com.akellolcc.cigars.databases.models.Cigar
import com.akellolcc.cigars.databases.models.CigarStrength
import com.akellolcc.cigars.databases.models.Relations
import com.akellolcc.cigars.utils.fraction
import kotlinx.serialization.Serializable

@Serializable
data class RapidCigar(
    val cigarId: Long,
    val brandId: Long,
    val name: String?,
    val length: Double?,
    val ringGauge: Long?,
    val country: String?,
    val filler: String?,
    val wrapper: String?,
    val color: String?,
    val strength: String?,
) {
    var brand: String? = null
    fun toCigar(): Cigar {
        return Cigar(
            -1,
            name ?: "Unknown",
            brand,
            country,
            null,
            "",
            wrapper ?: "",
            "",
            ringGauge ?: 0,
            length?.fraction ?: "0'",
            CigarStrength.fromString(strength) ?: CigarStrength.Mild,
            null,
            null,
            null,
            filler ?: "",
            null,
            1,
            shopping = false,
            favorites = false,
            price = null,
            relations = listOf(Relations("RapidAPI-Cigar", cigarId), Relations("RapidAPI-Brand", brandId)),
        )
    }
}