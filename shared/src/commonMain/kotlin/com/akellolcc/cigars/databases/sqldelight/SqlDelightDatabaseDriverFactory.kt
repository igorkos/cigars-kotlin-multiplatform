/*
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 4/26/24, 11:29 AM
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

package com.akellolcc.cigars.databases.sqldelight

import app.cash.sqldelight.db.SqlDriver
import com.akellolcc.cigars.databases.repository.DatabaseDriverFactory

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect class SqlDelightDatabaseDriverFactory() : DatabaseDriverFactory<SqlDriver> {
    override fun createDriver(inMemory: Boolean): SqlDriver
}
