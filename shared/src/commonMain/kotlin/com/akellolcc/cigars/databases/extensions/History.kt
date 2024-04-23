/*
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 4/22/24, 8:42 PM
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
import com.akellolcc.cigars.databases.HistoryTable
import com.akellolcc.cigars.theme.Images
import com.akellolcc.cigars.theme.Localize
import dev.icerock.moko.resources.ImageResource
import kotlinx.serialization.Serializable

@Stable
@Serializable
class History(
    override var rowid: Long,
    var count: Long,
    var date: Long,
    var left: Long,
    var price: Double?,
    var type: HistoryType,
    var cigarId: Long,
    var humidorFrom: Long,
    var humidorTo: Long?
) : BaseEntity() {

    constructor(history: HistoryTable) : this(
        history.rowid,
        history.count,
        history.date,
        history.left,
        history.price,
        HistoryType.fromLong(history.type),
        history.cigarId,
        history.humidorTo,
        history.humidorFrom
    )
}

enum class HistoryType(val type: Long) {
    Addition(0L),
    Deletion(1L),
    Move(2L),
    MoveFrom(3L),
    MoveTo(4L);

    override fun toString(): String {
        return localized(this)
    }

    companion object {
        fun localized(value: HistoryType): String {
            return when (value) {
                Addition -> Localize.history_type_addition
                Deletion -> Localize.history_type_deletion
                Move -> Localize.history_type_move
                MoveFrom -> Localize.history_type_move
                MoveTo -> Localize.history_type_move
            }
        }

        fun icon(value: Long): ImageResource {
            return icon(fromLong(value))
        }

        fun icon(value: HistoryType): ImageResource {
            return when (value) {
                Addition -> Images.icon_arrow_right
                Deletion -> Images.icon_arrow_left
                Move -> Images.icon_tab
                MoveFrom -> Images.icon_tab
                MoveTo -> Images.icon_tab
            }
        }

        fun fromLong(value: Long): HistoryType = when (value) {
            0L -> Addition
            1L -> Deletion
            2L -> Move
            3L -> MoveFrom
            4L -> MoveTo
            else -> Addition
        }
    }
}