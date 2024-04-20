/*
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 4/15/24, 10:04 PM
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
import com.akellolcc.cigars.databases.HumidorsTable
import kotlinx.serialization.Serializable

var emptyHumidor = Humidor(-1, "", "", 0, 0)

@Stable
@Serializable
data class Humidor(
    override var rowid: Long,
    var name: String,
    var brand: String,
    var holds: Long,
    var count: Long,
    var temperature: Long? = null,
    var humidity: Double? = null,
    var notes: String? = null,
    var link: String? = null,
    var price: Double? = null,
    var autoOpen: Boolean = false,
    var sorting: Long? = null,
    var type: Long = 0,
) : BaseEntity() {

    constructor(humidor: HumidorsTable) : this(
        humidor.rowid,
        humidor.name,
        humidor.brand,
        humidor.holds,
        humidor.count,
        humidor.temperature,
        humidor.humidity,
        humidor.notes,
        humidor.link,
        humidor.price,
        humidor.autoOpen ?: false,
        humidor.sorting,
        humidor.type
    )
}

