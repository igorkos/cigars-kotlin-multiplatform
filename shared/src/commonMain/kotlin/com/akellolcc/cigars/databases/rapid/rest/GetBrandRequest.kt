/*
 * Copyright (c) 2024.
 */

package com.akellolcc.cigars.databases.rapid.rest

import com.akellolcc.cigars.databases.extensions.Cigar
import com.akellolcc.cigars.databases.extensions.CigarStrength
import com.akellolcc.cigars.databases.extensions.Humidor
import com.akellolcc.cigars.databases.rapid.rest.RestRequest.Companion.GET
import com.akellolcc.cigars.logging.Log
import com.akellolcc.cigars.theme.AssetFiles
import com.akellolcc.cigars.theme.readTextFile
import com.badoo.reaktive.observable.ObservableWrapper
import com.badoo.reaktive.observable.flatMap
import com.badoo.reaktive.observable.map
import com.badoo.reaktive.observable.observable
import com.badoo.reaktive.observable.observableOf
import com.badoo.reaktive.observable.toObservable
import com.badoo.reaktive.observable.wrap
import com.badoo.reaktive.single.SingleWrapper
import com.badoo.reaktive.single.flatMap
import com.badoo.reaktive.single.flatMapIterable
import com.badoo.reaktive.single.map
import com.badoo.reaktive.single.singleOf
import com.badoo.reaktive.single.singleOfError
import com.badoo.reaktive.single.wrap
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

@Serializable
data class RapidCigarBrand(val brandId: Long, val name: String?)

@Serializable
class GetCigarsBrandResponse(val brand: RapidCigarBrand)

class GetCigarsBrand(val cigar: RapidCigar) {
    companion object{
        private const val BASE_URL = "https://cigars.p.rapidapi.com/brands/"
    }

    private val restRequest : RestRequest
    get() {
        val url = BASE_URL + cigar.brandId
        return RestRequest(GET, url)
    }
    fun execute(): ObservableWrapper<RapidCigar> {
        return observable { emitter ->
            restRequest.execute().map { restResponse ->
                Log.debug("Got response ${restResponse.status}")
                if (restResponse.status == 200) {
                    val response = Json.decodeFromString<GetCigarsBrandResponse>(restResponse.body)
                    Log.debug("Got brand=${response.brand.brandId} name=${response.brand.name}")
                    cigar.brand = response.brand.name
                    emitter.onNext(cigar)
                } else {
                    emitter.onError(Exception("Got response ${restResponse.status}"))
                }
            }.wrap().subscribe{
                emitter.onComplete()
            }
        }.wrap()
    }

}