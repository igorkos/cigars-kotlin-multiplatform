package com.akellolcc.cigars.common.databases.shared

import app.cash.sqldelight.SuspendingTransacterImpl
import app.cash.sqldelight.db.AfterVersion
import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlSchema
import com.akellolcc.cigars.common.databases.CigarsDatabase
import com.akellolcc.cigars.common.databases.CigarsDatabaseQueries
import kotlin.Long
import kotlin.Unit
import kotlin.reflect.KClass

internal val KClass<CigarsDatabase>.schema: SqlSchema<QueryResult.AsyncValue<Unit>>
  get() = CigarsDatabaseImpl.Schema

internal fun KClass<CigarsDatabase>.newInstance(driver: SqlDriver): CigarsDatabase =
    CigarsDatabaseImpl(driver)

private class CigarsDatabaseImpl(
  driver: SqlDriver,
) : SuspendingTransacterImpl(driver), CigarsDatabase {
  override val cigarsDatabaseQueries: CigarsDatabaseQueries = CigarsDatabaseQueries(driver)

  public object Schema : SqlSchema<QueryResult.AsyncValue<Unit>> {
    override val version: Long
      get() = 1

    override fun create(driver: SqlDriver): QueryResult.AsyncValue<Unit> = QueryResult.AsyncValue {
      driver.execute(null, """
          |CREATE TABLE Cigars (
          |    id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
          |    binder TEXT,
          |    brand TEXT,
          |    cigar TEXT,
          |    count INTEGER,
          |    country TEXT,
          |    date INTEGER,
          |    favorites INTEGER,
          |    filler TEXT,
          |    gauge REAL,
          |    lenght TEXT,
          |    link TEXT,
          |    myrating INTEGER,
          |    name TEXT,
          |    notes TEXT,
          |    rating INTEGER,
          |    shopping INTEGER,
          |    strenght INTEGER,
          |    wrapper TEXT,
          |    historyId INTEGER,
          |    humidorId  INTEGER,
          |    imagesId  INTEGER,
          |    FOREIGN KEY (historyId) REFERENCES History(id) ON DELETE CASCADE,
          |    FOREIGN KEY (humidorId) REFERENCES Humidor(id) ON DELETE CASCADE,
          |    FOREIGN KEY (imagesId) REFERENCES Images(id) ON DELETE CASCADE
          |)
          """.trimMargin(), 0).await()
      driver.execute(null, """
          |CREATE TABLE Humidor (
          |    id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
          |    auto_open INTEGER,
          |    brand TEXT,
          |    count INTEGER,
          |    holds INTEGER,
          |    humidity REAL,
          |    link TEXT,
          |    name TEXT,
          |    notes TEXT,
          |    sorting INTEGER,
          |    temperature INTEGER,
          |    type INTEGER,
          |    historyId INTEGER,
          |    cigarsId  INTEGER,
          |    imagesId  INTEGER,
          |    FOREIGN KEY (historyId) REFERENCES History(id) ON DELETE CASCADE,
          |    FOREIGN KEY (cigarsId) REFERENCES Cigars(id) ON DELETE CASCADE,
          |    FOREIGN KEY (imagesId) REFERENCES Images(id) ON DELETE CASCADE
          |)
          """.trimMargin(), 0).await()
      driver.execute(null, """
          |CREATE TABLE History (
          |    id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
          |    count INTEGER,
          |    date INTEGER,
          |    left INTEGER,
          |    price REAL,
          |    type INTEGER
          |)
          """.trimMargin(), 0).await()
      driver.execute(null, """
          |CREATE TABLE Images (
          |    id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
          |    data BLOB,
          |    image TEXT,
          |    notes TEXT,
          |    type INTEGER
          |  )
          """.trimMargin(), 0).await()
    }

    override fun migrate(
      driver: SqlDriver,
      oldVersion: Long,
      newVersion: Long,
      vararg callbacks: AfterVersion,
    ): QueryResult.AsyncValue<Unit> = QueryResult.AsyncValue {
    }
  }
}
