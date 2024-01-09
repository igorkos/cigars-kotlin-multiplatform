package com.akellolcc.cigars.common.databases

import app.cash.sqldelight.Query
import app.cash.sqldelight.SuspendingTransacterImpl
import app.cash.sqldelight.db.SqlDriver
import kotlin.Any
import kotlin.Boolean
import kotlin.Double
import kotlin.Long
import kotlin.String

public class CigarsDatabaseQueries(
  driver: SqlDriver,
) : SuspendingTransacterImpl(driver) {
  public fun <T : Any> allCigars(mapper: (
    id: Long,
    binder: String?,
    brand: String?,
    cigar: String?,
    count: Long?,
    country: String?,
    date: Long?,
    favorites: Boolean?,
    filler: String?,
    gauge: Double?,
    lenght: String?,
    link: String?,
    myrating: Long?,
    name: String?,
    notes: String?,
    rating: Long?,
    shopping: Boolean?,
    strenght: Long?,
    wrapper: String?,
    historyId: Long?,
    humidorId: Long?,
    imagesId: Long?,
  ) -> T): Query<T> = Query(-1_238_087_459, arrayOf("Cigars"), driver, "CigarsDatabase.sq",
      "allCigars", """
  |SELECT Cigars.*
  |FROM Cigars
  """.trimMargin()) { cursor ->
    mapper(
      cursor.getLong(0)!!,
      cursor.getString(1),
      cursor.getString(2),
      cursor.getString(3),
      cursor.getLong(4),
      cursor.getString(5),
      cursor.getLong(6),
      cursor.getBoolean(7),
      cursor.getString(8),
      cursor.getDouble(9),
      cursor.getString(10),
      cursor.getString(11),
      cursor.getLong(12),
      cursor.getString(13),
      cursor.getString(14),
      cursor.getLong(15),
      cursor.getBoolean(16),
      cursor.getLong(17),
      cursor.getString(18),
      cursor.getLong(19),
      cursor.getLong(20),
      cursor.getLong(21)
    )
  }

  public fun allCigars(): Query<Cigars> = allCigars { id, binder, brand, cigar, count, country,
      date, favorites, filler, gauge, lenght, link, myrating, name, notes, rating, shopping,
      strenght, wrapper, historyId, humidorId, imagesId ->
    Cigars(
      id,
      binder,
      brand,
      cigar,
      count,
      country,
      date,
      favorites,
      filler,
      gauge,
      lenght,
      link,
      myrating,
      name,
      notes,
      rating,
      shopping,
      strenght,
      wrapper,
      historyId,
      humidorId,
      imagesId
    )
  }

  public fun <T : Any> allHumidors(mapper: (
    id: Long,
    auto_open: Boolean?,
    brand: String?,
    count: Long?,
    holds: Long?,
    humidity: Double?,
    link: String?,
    name: String?,
    notes: String?,
    sorting: Long?,
    temperature: Long?,
    type: Long?,
    historyId: Long?,
    cigarsId: Long?,
    imagesId: Long?,
  ) -> T): Query<T> = Query(-2_028_101_935, arrayOf("Humidor"), driver, "CigarsDatabase.sq",
      "allHumidors", """
  |SELECT Humidor.*
  |FROM Humidor
  """.trimMargin()) { cursor ->
    mapper(
      cursor.getLong(0)!!,
      cursor.getBoolean(1),
      cursor.getString(2),
      cursor.getLong(3),
      cursor.getLong(4),
      cursor.getDouble(5),
      cursor.getString(6),
      cursor.getString(7),
      cursor.getString(8),
      cursor.getLong(9),
      cursor.getLong(10),
      cursor.getLong(11),
      cursor.getLong(12),
      cursor.getLong(13),
      cursor.getLong(14)
    )
  }

  public fun allHumidors(): Query<Humidor> = allHumidors { id, auto_open, brand, count, holds,
      humidity, link, name, notes, sorting, temperature, type, historyId, cigarsId, imagesId ->
    Humidor(
      id,
      auto_open,
      brand,
      count,
      holds,
      humidity,
      link,
      name,
      notes,
      sorting,
      temperature,
      type,
      historyId,
      cigarsId,
      imagesId
    )
  }

  public suspend fun addHumidor(
    name: String?,
    brand: String?,
    holds: Long?,
    notes: String?,
    humidity: Double?,
    temperature: Long?,
  ) {
    driver.execute(1_283_014_370, """
        |INSERT INTO Humidor (name, brand, holds, notes, humidity, temperature)
        |VALUES (?, ?, ?, ?, ?, ?)
        """.trimMargin(), 6) {
          bindString(0, name)
          bindString(1, brand)
          bindLong(2, holds)
          bindString(3, notes)
          bindDouble(4, humidity)
          bindLong(5, temperature)
        }.await()
    notifyQueries(1_283_014_370) { emit ->
      emit("Humidor")
    }
  }

  public suspend fun removeAllCigars() {
    driver.execute(1_801_630_841, """DELETE FROM Cigars""", 0).await()
    notifyQueries(1_801_630_841) { emit ->
      emit("Cigars")
      emit("Humidor")
    }
  }

  public suspend fun removeAllHumidors() {
    driver.execute(-1_436_576_915, """DELETE FROM Humidor""", 0).await()
    notifyQueries(-1_436_576_915) { emit ->
      emit("Cigars")
      emit("Humidor")
    }
  }
}
