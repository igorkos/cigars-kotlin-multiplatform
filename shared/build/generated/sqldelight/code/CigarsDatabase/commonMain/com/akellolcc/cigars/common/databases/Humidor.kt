package com.akellolcc.cigars.common.databases

import kotlin.Boolean
import kotlin.Double
import kotlin.Long
import kotlin.String

public data class Humidor(
  public val id: Long,
  public val auto_open: Boolean?,
  public val brand: String?,
  public val count: Long?,
  public val holds: Long?,
  public val humidity: Double?,
  public val link: String?,
  public val name: String?,
  public val notes: String?,
  public val sorting: Long?,
  public val temperature: Long?,
  public val type: Long?,
  public val historyId: Long?,
  public val cigarsId: Long?,
  public val imagesId: Long?,
)
