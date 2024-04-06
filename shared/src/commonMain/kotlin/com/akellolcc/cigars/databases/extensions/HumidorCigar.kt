package com.akellolcc.cigars.databases.extensions

data class HumidorCigar(
    var count: Long,
    val humidor: Humidor?,
    val cigar: Cigar?,
) : BaseEntity(-1)