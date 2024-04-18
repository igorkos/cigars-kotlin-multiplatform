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
import com.badoo.reaktive.observable.collect
import com.badoo.reaktive.observable.flatMap
import com.badoo.reaktive.observable.flatMapIterable
import com.badoo.reaktive.observable.map
import com.badoo.reaktive.observable.observable
import com.badoo.reaktive.observable.observableOf
import com.badoo.reaktive.observable.take
import com.badoo.reaktive.observable.takeWhile
import com.badoo.reaktive.observable.toList
import com.badoo.reaktive.observable.wrap
import com.badoo.reaktive.single.SingleWrapper
import com.badoo.reaktive.single.flatMap
import com.badoo.reaktive.single.flatMapIterable
import com.badoo.reaktive.single.map
import com.badoo.reaktive.single.wrap
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlin.properties.Delegates.observable

@Serializable
data class RapidCigar(val cigarId: Long,
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
        return Cigar( cigarId,
            name ?: "Unknown",
            brand,
            country,
            null,
            "Toro",
            wrapper ?:"",
            wrapper ?:"",
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

class GetCigarsRequest(val name: String? = null, val brand: String? = null, val country: String? = null,
                       val filler: String? = null, val wrapper: String? = null, val color: String? = null,
                       val strength: CigarStrength? = null) {
    private var request: RestRequest? = null
    private val cigars = mutableListOf<Cigar>()
    private var page = 0
    companion object{
        private const val BASE_URL = "https://cigars.p.rapidapi.com/cigars?"
    }

    private val restRequest : RestRequest
    get() {
        val url = BASE_URL + "page=$page" +
                if (name != null) "&name=$name" else "" +
                if (brand != null) "&brand=$brand" else "" +
                if (country != null) "&country=$country" else "" +
                if (filler != null) "&filler=$filler" else "" +
                if (wrapper != null) "&wrapper=$wrapper" else "" +
                if (color != null) "&color=$color" else "" +
                if (strength != null) "&strength=$strength" else ""
        return RestRequest(GET, url)
    }
    private val json = Json { ignoreUnknownKeys = true }

    fun next(): ObservableWrapper<List<Cigar>> {
        page++
        return call()
    }

   fun call(): ObservableWrapper<List<Cigar>> {
       var count = 0
        return restRequest.execute().flatMapIterable { restResponse ->
            Log.debug("Got response ${restResponse.status}")
            if (restResponse.status == 200) {
                val response = json.decodeFromString<GetCigarsResponse>(restResponse.body)
                Log.debug("Got page=${response.page} count=${response.count} get=${response.cigars.size}")
                count = response.cigars.size
                return@flatMapIterable response.cigars
            }
            throw Exception("Got response ${restResponse.status}")
        }.flatMap  {
            Log.debug("Next rapid cigar $it")
            GetCigarsBrand(it).execute()
        }.map {
            Log.debug("Add to list $it")
            cigars.add(it.toCigar())
            it
        }.flatMap{
            observable {emitter ->
                Log.debug("Got ${cigars.size} cigars")
                if (cigars.size == count) {
                    emitter.onNext(cigars.toList())
                    emitter.onComplete()
                }
            }
        }.wrap()
    }
}