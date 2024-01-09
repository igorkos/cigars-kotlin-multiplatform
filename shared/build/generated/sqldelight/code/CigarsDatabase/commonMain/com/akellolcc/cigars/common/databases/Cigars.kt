package com.akellolcc.cigars.common.databases

import kotlin.Boolean
import kotlin.Double
import kotlin.Long
import kotlin.String

public data class Cigars(
  public val id: Long,
  public val binder: String?,
  public val brand: String?,
  public val cigar: String?,
  public val count: Long?,
  public val country: String?,
  public val date: Long?,
  public val favorites: Boolean?,
  public val filler: String?,
  public val gauge: Double?,
  public val lenght: String?,
  public val link: String?,
  public val myrating: Long?,
  public val name: String?,
  public val notes: String?,
  public val rating: Long?,
  public val shopping: Boolean?,
  public val strenght: Long?,
  public val wrapper: String?,
  public val historyId: Long?,
  public val humidorId: Long?,
  public val imagesId: Long?,
)
