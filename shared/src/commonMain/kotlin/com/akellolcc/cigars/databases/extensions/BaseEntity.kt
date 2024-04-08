package com.akellolcc.cigars.databases.extensions

import com.akellolcc.cigars.ui.randomString
import kotlinx.serialization.Serializable

@Serializable
abstract class BaseEntity {
    abstract var rowid: Long
    val key: String get() = rowid.toString() + randomString()

    val isStored: Boolean
        get() {
            return rowid >= 0
        }

    override fun hashCode(): Int = rowid.hashCode()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as BaseEntity

        if (rowid != other.rowid) return false

        return true
    }
}
