package com.akellolcc.cigars.databases.extensions

import androidx.compose.runtime.Stable
import com.akellolcc.cigars.ui.randomString
import kotlinx.serialization.Serializable
@Stable
@Serializable
abstract class BaseEntity: Comparable<BaseEntity> {
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
