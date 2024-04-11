package com.akellolcc.cigars.databases.extensions

import androidx.compose.runtime.Stable
import kotlinx.serialization.Serializable
@Stable
@Serializable
data class HumidorCigar(
    var count: Long,
    val humidor: Humidor?,
    val cigar: Cigar?,
) : BaseEntity() {
    override var rowid: Long = -1
}