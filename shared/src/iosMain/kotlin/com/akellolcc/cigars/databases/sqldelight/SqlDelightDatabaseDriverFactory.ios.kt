/*******************************************************************************************************************************************
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 5/17/24, 5:53 PM
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
 ******************************************************************************************************************************************/

package com.akellolcc.cigars.databases.sqldelight

import app.cash.sqldelight.async.coroutines.synchronous
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import app.cash.sqldelight.driver.native.wrapConnection
import co.touchlab.sqliter.DatabaseConfiguration
import com.akellolcc.cigars.databases.CigarsDatabase
import com.akellolcc.cigars.databases.repository.DatabaseDriverFactory

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class SqlDelightDatabaseDriverFactory : DatabaseDriverFactory<SqlDriver> {
    actual override fun createDriver(inMemory: Boolean): SqlDriver {
        if (inMemory) {
            val schema = CigarsDatabase.Schema
            return NativeSqliteDriver(
                DatabaseConfiguration(
                    name = "cigars.db",
                    version = schema.version.toInt(),
                    create = { connection ->
                        wrapConnection(connection) { schema.create(it) }
                    },
                    upgrade = { connection, oldVersion, newVersion ->
                        wrapConnection(connection) {
                            schema.migrate(it, oldVersion.toLong(), newVersion.toLong())
                        }
                    },
                    inMemory = true
                )
            )
        }
        return NativeSqliteDriver(CigarsDatabase.Schema.synchronous(), "cigars.db")
    }
}