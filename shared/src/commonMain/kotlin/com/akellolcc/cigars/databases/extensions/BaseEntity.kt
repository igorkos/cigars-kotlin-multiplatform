package com.akellolcc.cigars.databases.extensions

abstract class BaseEntity(open val rowid: Long) {
    override fun hashCode(): Int = rowid.hashCode()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as BaseEntity

        if (rowid != other.rowid) return false

        return true
    }
}
