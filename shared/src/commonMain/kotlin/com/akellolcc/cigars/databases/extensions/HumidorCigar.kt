package com.akellolcc.cigars.databases.extensions

import kotlinx.serialization.Serializable

@Serializable
data class HumidorCigar(
    override var rowid: Long,
    var count: Long,
    val humidor: Humidor?,
    val cigar: Cigar?,
) : BaseEntity()