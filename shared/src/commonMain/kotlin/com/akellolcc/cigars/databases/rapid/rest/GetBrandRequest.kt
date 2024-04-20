/*
 * Copyright (c) 2024.
 */

package com.akellolcc.cigars.databases.rapid.rest

import com.akellolcc.cigars.databases.rapid.rest.RestRequest.Companion.GET
import com.akellolcc.cigars.logging.Log
import com.badoo.reaktive.observable.ObservableWrapper
import com.badoo.reaktive.observable.map
import com.badoo.reaktive.observable.observable
import com.badoo.reaktive.observable.wrap
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class RapidCigarBrand(val brandId: Long, val name: String?)

@Serializable
class GetCigarsBrandResponse(val brand: RapidCigarBrand)

class GetCigarsBrand(val cigar: RapidCigar) {
    companion object {
        private const val BASE_URL = "https://cigars.p.rapidapi.com/brands/"
    }

    private val restRequest: RestRequest
        get() {
            val url = BASE_URL + cigar.brandId
            return RestRequest(GET, url)
        }

    fun execute(): ObservableWrapper<RapidCigar> {
        return observable { emitter ->
            restRequest.execute().map { restResponse ->
                if (restResponse.status == 200) {
                    val response = Json.decodeFromString<GetCigarsBrandResponse>(restResponse.body)
                    //Log.debug("Got brand=${response.brand.brandId} name=${response.brand.name}")
                    cigar.brand = response.brand.name
                    emitter.onNext(cigar)
                } else {
                    Log.error("GetCigarsBrand got response ${restResponse.status}")
                    emitter.onError(Exception("Got response ${restResponse.status}"))
                }
            }.wrap().subscribe {
                emitter.onComplete()
            }
        }.wrap()
    }

}