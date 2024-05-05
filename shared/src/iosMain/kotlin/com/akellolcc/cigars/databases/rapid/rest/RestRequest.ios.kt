/*******************************************************************************************************************************************
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 5/1/24, 1:16 AM
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

package com.akellolcc.cigars.databases.rapid.rest

import io.ktor.client.plugins.cache.storage.CacheStorage
import io.ktor.client.plugins.cache.storage.CachedResponseData
import io.ktor.http.Headers
import io.ktor.http.HttpProtocolVersion
import io.ktor.http.HttpStatusCode
import io.ktor.http.Url
import io.ktor.util.date.GMTDate
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.allocArrayOf
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.usePinned
import platform.Foundation.NSCachedURLResponse
import platform.Foundation.NSData
import platform.Foundation.NSURL
import platform.Foundation.NSURLCache
import platform.Foundation.NSURLCacheStoragePolicy
import platform.Foundation.NSURLRequest
import platform.Foundation.NSURLResponse
import platform.Foundation.create
import platform.posix.memcpy

fun Url.nsUrl() = NSURL(string = this.toString())

typealias DataBytes = NSData

@OptIn(ExperimentalForeignApi::class)
fun DataBytes.toByteArray(): ByteArray = ByteArray(this@toByteArray.length.toInt()).apply {
    usePinned {
        memcpy(it.addressOf(0), this@toByteArray.bytes, this@toByteArray.length)
    }
}

@OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
fun ByteArray.toNSData(): DataBytes = memScoped {
    NSData.create(bytes = allocArrayOf(this@toNSData), length = this@toNSData.size.toULong())
}

fun CachedResponseData.toCachedURLResponse(): NSCachedURLResponse {
    val userInfo = mutableMapOf<String, Any>()
    this.headers.entries().forEach {
        userInfo[it.key] = it.value
    }
    userInfo["Expires"] = "32513272164000"
    userInfo["Response-Code"] = this.statusCode.value.toString()
    userInfo["Request-Time"] = this.requestTime.timestamp.toString()
    userInfo["Response-Time"] = this.responseTime.timestamp.toString()
    userInfo["Version"] = this.version.toString()
    userInfo["URL"] = this.url.toString()

    val urlResponse =
        NSURLResponse(url.nsUrl(), "application/json", this.body.size.toLong(), "UTF8")
    return NSCachedURLResponse(
        urlResponse,
        this.body.toNSData(),
        userInfo.toMap(),
        NSURLCacheStoragePolicy.NSURLCacheStorageAllowed
    )
}

fun NSCachedURLResponse.toCachedResponseData(): CachedResponseData {
    var url = ""
    var statusCode: HttpStatusCode? = null
    var requestTime: GMTDate? = null
    var responseTime: GMTDate? = null
    var version: HttpProtocolVersion? = null
    var expires: GMTDate? = null
    val headersMap = mutableMapOf<String, String>()
    this.userInfo?.forEach {
        when (it.key) {
            "Response-Code" -> statusCode = HttpStatusCode(it.value.toString().toInt(), "")
            "Request-Time" -> requestTime = GMTDate(it.value.toString().toLong())
            "Response-Time" -> responseTime = GMTDate(it.value.toString().toLong())
            "Version" -> version = HttpProtocolVersion.parse(it.value.toString())
            "Expires" -> expires = GMTDate(it.value.toString().toLong())
            "URL" -> url = it.value.toString()
            else -> {
                headersMap[it.key as String] = it.value.toString()
            }
        }
    }

    val headers = Headers.build {
        headersMap.forEach {
            append(it.key, it.value)
        }
    }
    return CachedResponseData(
        url = Url(url),
        statusCode = statusCode!!,
        requestTime = requestTime!!,
        responseTime = responseTime!!,
        version = version!!,
        expires = expires!!,
        headers = headers,
        varyKeys = emptyMap(),
        body = this.data.toByteArray()
    )
}

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class PersistentCacheStorage actual constructor() : CacheStorage {
    private var storage = NSURLCache.sharedURLCache

    override suspend fun find(url: Url, varyKeys: Map<String, String>): CachedResponseData? {
        val request = NSURLRequest(url.nsUrl())
        val cached = storage.cachedResponseForRequest(request) ?: return null
        return cached.toCachedResponseData()
    }

    override suspend fun findAll(url: Url): Set<CachedResponseData> {
        val request = NSURLRequest(url.nsUrl())
        val cached = storage.cachedResponseForRequest(request) ?: return emptySet()
        return setOf(cached.toCachedResponseData())
    }

    override suspend fun store(url: Url, data: CachedResponseData) {
        storage.storeCachedResponse(data.toCachedURLResponse(), NSURLRequest(url.nsUrl()))
    }
}