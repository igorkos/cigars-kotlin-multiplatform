/*
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 4/21/24, 7:52 PM
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

package com.akellolcc.cigars.databases.rapid.rest

import com.akellolcc.cigars.logging.Log
import com.akellolcc.cigars.utils.appContext
import io.ktor.client.plugins.cache.storage.CacheStorage
import io.ktor.client.plugins.cache.storage.CachedResponseData
import io.ktor.client.plugins.cache.storage.FileStorage
import io.ktor.http.Url
import io.ktor.util.date.GMTDate
import java.io.File
import java.io.IOException

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class PersistentCacheStorage actual constructor() : CacheStorage {
    private var storage: CacheStorage? = null

    init {
        try {
            val cacheDir =
                appContext?.cacheDir ?: throw IOException("Failed to get cache directory")
            val cacheFile = File(cacheDir, "http-cache")
            storage = FileStorage(cacheFile)
        } catch (e: IOException) {
            Log.error("Failed to create HTTP cache file")
        }
    }


    override suspend fun find(url: Url, varyKeys: Map<String, String>): CachedResponseData? {
        return storage?.find(url, varyKeys)
    }

    override suspend fun findAll(url: Url): Set<CachedResponseData> {
        val res = storage?.findAll(url)
        return res ?: emptySet()
    }

    override suspend fun store(url: Url, data: CachedResponseData) {
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

        storage?.store(url, entry)
    }
}