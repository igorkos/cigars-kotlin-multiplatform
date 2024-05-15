/*******************************************************************************************************************************************
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 5/15/24, 1:34 PM
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

package com.akellolcc.cigars.databases.rapid

import com.akellolcc.cigars.databases.RepositoryRegistry
import com.akellolcc.cigars.databases.repository.BrandsRepository
import com.akellolcc.cigars.databases.repository.CigarsSearchRepository
import com.akellolcc.cigars.databases.repository.DatabaseInterface

class RapidDatabase(override val inMemory: Boolean) : DatabaseInterface {

    companion object {
        private var _instance: RapidDatabase? = null
        val instance: RapidDatabase
            get() {
                if (_instance == null) {
                    throw IllegalStateException("Database not initialized")
                }
                return _instance!!
            }

        internal fun createInstance(inMemory: Boolean): RapidDatabase {
            if (_instance == null) {
                _instance = RapidDatabase(inMemory)
                _instance!!.register()
            }
            return _instance!!
        }
    }

    private fun register() {
        RepositoryRegistry.register(
            CigarsSearchRepository::class,
            RapidCigarsRepository.Factory
        )
        RepositoryRegistry.register(
            BrandsRepository::class,
            RapidBrandsRepository.Factory
        )
    }

    override fun reset() {}
}