/*
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 4/26/24, 4:56 PM
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.akellolcc.cigars.databases.repository.impl

import app.cash.sqldelight.async.coroutines.synchronous
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.akellolcc.cigars.databases.CigarsDatabase
import com.akellolcc.cigars.databases.repository.DatabaseDriverFactory
import com.akellolcc.cigars.utils.appContext
import kotlinx.coroutines.runBlocking

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class SqlDelightDatabaseDriverFactory : DatabaseDriverFactory<SqlDriver> {
    actual override fun createDriver(inMemory: Boolean): SqlDriver {
        if (inMemory) {
            val driver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
            runBlocking {
                val schema = CigarsDatabase.Schema
                schema.create(driver).await()
            }
            return driver
        }
        if (appContext == null) throw Exception("appContext is null")
        return AndroidSqliteDriver(CigarsDatabase.Schema.synchronous(), appContext!!, "cigars.db")
    }
}