package com.akellolcc.cigars.databases.demo

import kotlinx.serialization.Serializable

@Serializable
data class Image(  val file: String?,
                   val type: Long?)

@Serializable
data class DemoCigar(
    public val name: String,
    public val brand: String,
    public val country: String,
    public val cigar: String,
    public val wrapper: String,
    public val binder: String,
    public val gauge: Double,
    public val length: String,
    public val strength: Long,
    public val rating: Long,
    public val myrating: Long,
    public val notes: String,
    public val filler: String,
    public val link: String,
    public val shopping: Boolean,
    public val favorites: Boolean,
    public val images: List<Image>
)
