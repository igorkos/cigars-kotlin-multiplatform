/*
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 4/19/24, 10:48 PM
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

import com.akellolcc.cigars.databases.extensions.Cigar
import com.akellolcc.cigars.databases.extensions.CigarStrength
import com.akellolcc.cigars.databases.rapid.rest.RestRequest.Companion.GET
import com.akellolcc.cigars.logging.Log
import com.badoo.reaktive.observable.flatMap
import com.badoo.reaktive.observable.flatMapIterable
import com.badoo.reaktive.observable.map
import com.badoo.reaktive.observable.observable
import com.badoo.reaktive.observable.subscribe
import com.badoo.reaktive.observable.toList
import com.badoo.reaktive.single.SingleWrapper
import com.badoo.reaktive.single.singleOf
import com.badoo.reaktive.single.wrap
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class RapidCigar(
    val cigarId: Long,
    val brandId: Long,
    val name: String?,
    val length: Double?,
    val ringGauge: Long?,
    val country: String?,
    val filler: String?,
    val wrapper: String?,
    val color: String?,
    val strength: String?,
) {
    var brand: String? = null
    fun toCigar(): Cigar {
        return Cigar(
            cigarId,
            name ?: "Unknown",
            brand,
            country,
            null,
            "Toro",
            wrapper ?: "",
            wrapper ?: "",
            ringGauge ?: 0,
            length?.toString() ?: "0.0",
            CigarStrength.fromString(strength) ?: CigarStrength.Mild,
            null,
            null,
            null,
            filler ?: "",
            null,
            1,
            shopping = false,
            favorites = false,
            price = null
        )
    }
}

@Serializable
class GetCigarsResponse(val cigars: List<RapidCigar>, val page: Int, val count: Int)
class GetCigarsRequest(
    val name: String? = null, val brand: String? = null, val country: String? = null,
    val filler: String? = null, val wrapper: String? = null, val color: String? = null,
    val strength: CigarStrength? = null
) {
    private var page = 0
    private var totalItems = 0
    private var receivedItems = 0
    val isLastPage: Boolean
        get() = if (page == 0) false else receivedItems >= totalItems

    companion object {
        private const val BASE_URL = "https://cigars.p.rapidapi.com/cigars?"
    }

    private val restRequest: RestRequest
        get() {
            val url = BASE_URL + "page=$page" +
                    if (name != null) "&name=$name" else "" +
                            if (brand != null) "&brand=$brand" else "" +
                                    if (country != null) "&country=$country" else "" +
                                            if (filler != null) "&filler=$filler" else "" +
                                                    if (wrapper != null) "&wrapper=$wrapper" else "" +
                                                            if (color != null) "&color=$color" else "" +
                                                                    if (strength != null) "&strength=$strength" else ""
            Log.debug("GetCigarsRequest url=$url")
            return RestRequest(GET, url)
        }
    private val json = Json { ignoreUnknownKeys = true }

    fun next(): SingleWrapper<List<Cigar>> {
        if (isLastPage) {
            return singleOf(emptyList<Cigar>()).wrap()
        }
        page++
        return call()
    }

    private fun call(): SingleWrapper<List<Cigar>> {
        return observable { emitter ->
            restRequest.execute().map { restResponse ->
                if (restResponse.status == 200) {
                    val response = json.decodeFromString<GetCigarsResponse>(restResponse.body)
                    Log.debug("GetCigarsRequest got page=${response.page} count=${response.count} get=${response.cigars.size}")
                    totalItems = response.count
                    receivedItems += response.cigars.size
                    page = response.page
                    emitter.onNext(response.cigars)
                } else {
                    Log.error("GetCigarsRequest got response ${restResponse.status}")
                    emitter.onError(Exception("Got response ${restResponse.status}"))
                }
            }.subscribe {
                emitter.onComplete()
            }
        }.flatMapIterable {
            it
        }.flatMap {
            GetCigarsBrand(it).execute()
        }.map {
            it.toCigar()
        }.toList().wrap()
    }
}

