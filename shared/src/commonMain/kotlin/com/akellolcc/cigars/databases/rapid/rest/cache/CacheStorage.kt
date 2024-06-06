/*******************************************************************************************************************************************
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 5/15/24, 1:28 PM
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

package com.akellolcc.cigars.databases.rapid.rest.cache

import io.ktor.client.plugins.cache.storage.CacheStorage
import io.ktor.client.plugins.cache.storage.CachedResponseData
import io.ktor.http.Url

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect class PersistentCacheStorage() : CacheStorage

class HttpCacheStorage(temporary: Boolean) : CacheStorage {
    private val storage: CacheStorage = if (temporary) {
        MemoryCacheStorage()
    } else {
        PersistentCacheStorage()
    }

    override suspend fun find(url: Url, varyKeys: Map<String, String>): CachedResponseData? {
        return storage.find(url, varyKeys)
    }

    override suspend fun findAll(url: Url): Set<CachedResponseData> {
        return storage.findAll(url)
    }

    override suspend fun store(url: Url, data: CachedResponseData) {
        return storage.store(url, data)
    }


}