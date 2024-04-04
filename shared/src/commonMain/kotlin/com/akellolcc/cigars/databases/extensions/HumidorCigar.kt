package com.akellolcc.cigars.databases.extensions

class HumidorCigar(
    public val count: Long?,
    public val humidor: Humidor?,
    public val cigar: Cigar?,
) : BaseEntity(-1)