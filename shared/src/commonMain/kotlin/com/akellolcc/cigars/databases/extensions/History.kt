package com.akellolcc.cigars.databases.extensions

import com.akellolcc.cigars.databases.HistoryTable
import com.akellolcc.cigars.theme.Localize
import kotlinx.serialization.Serializable

@Serializable
class History(
    override var rowid: Long,
    var count: Long,
    var date: Long,
    var left: Long,
    var price: Double?,
    var type: HistoryType,
    var cigarId: Long,
    var humidorId: Long
) : BaseEntity() {

    constructor(history: HistoryTable) : this(
        history.rowid,
        history.count,
        history.date,
        history.left,
        history.price,
        HistoryType.fromLong(history.type),
        history.cigarId,
        history.humidorId,
    )
}

enum class HistoryType(val type: Long) {
    Addition(0L),
    Deletion(1L);

    override fun toString(): String {
        return localized(this)
    }

    companion object {
        fun localized(value: HistoryType): String {
            return when (value) {
                Addition -> Localize.history_type_addition
                Deletion -> Localize.history_type_deletion
            }
        }

        inline fun fromLong(value: Long): HistoryType = when (value) {
            0L -> Addition
            else -> Deletion
        }
    }
}