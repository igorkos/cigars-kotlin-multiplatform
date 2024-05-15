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
import io.ktor.util.collections.ConcurrentMap
import io.ktor.util.collections.ConcurrentSet
import io.ktor.util.date.GMTDate

class MemoryCacheStorage : CacheStorage {

    private val store = ConcurrentMap<Url, MutableSet<CachedResponseData>>()

    override suspend fun store(url: Url, data: CachedResponseData) {
        val cache = store.computeIfAbsent(url) { ConcurrentSet() }
        val entry = CachedResponseData(
            url = data.url,
            statusCode = data.statusCode,
            requestTime = data.requestTime,
            responseTime = data.responseTime,
            version = data.version,
            expires = GMTDate(32513272164000),
            headers = data.headers,
            varyKeys = data.varyKeys,
            body = data.body
        )
        if (!cache.add(entry)) {
            cache.remove(entry)
            cache.add(entry)
        }
    }

    override suspend fun find(url: Url, varyKeys: Map<String, String>): CachedResponseData? {
        val data = store.computeIfAbsent(url) { ConcurrentSet() }
        return data.find {
            varyKeys.all { (key, value) -> it.varyKeys[key] == value }
        }
    }

    override suspend fun findAll(url: Url): Set<CachedResponseData> = store[url] ?: emptySet()
}