/*
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 4/21/24, 2:09 PM
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

package com.akellolcc.cigars.mvvm

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.akellolcc.cigars.databases.RepositoryType
import com.akellolcc.cigars.databases.extensions.Cigar
import com.akellolcc.cigars.databases.extensions.CigarSearchFields
import com.akellolcc.cigars.databases.rapid.rest.GetCigarsBrands
import com.akellolcc.cigars.databases.rapid.rest.GetCigarsRequest
import com.akellolcc.cigars.databases.rapid.rest.RapidCigarBrand
import com.akellolcc.cigars.databases.repository.CigarsRepository
import com.akellolcc.cigars.logging.Log
import com.akellolcc.cigars.screens.search.CigarsSearchBrandField
import com.akellolcc.cigars.screens.search.CigarsSearchParameterField
import com.akellolcc.cigars.screens.search.SearchParameterField
import com.badoo.reaktive.observable.ObservableWrapper
import com.badoo.reaktive.observable.observable
import com.badoo.reaktive.observable.wrap
import dev.icerock.moko.resources.desc.StringDesc


class SearchCigarScreenViewModel() :
    BaseListViewModel<Cigar, SearchCigarScreenViewModel.Actions>() {
    override val repository: CigarsRepository = database.getRepository(RepositoryType.Cigars)

    private var request: GetCigarsRequest? = null
    private var brandRequest: GetCigarsBrands? = null

    var name by mutableStateOf("")
    var brand by mutableStateOf<RapidCigarBrand?>(null)
    var country by mutableStateOf("")
    var searchParams by mutableStateOf(listOf<SearchParameterField<CigarSearchFields>>())

    init {
        addSearchParameter(CigarSearchFields.Name)
    }

    override fun entitySelected(entity: Cigar) {
    }

    fun addSearchParameter(parameter: CigarSearchFields) {
        val field = when (parameter) {
            CigarSearchFields.Name -> CigarsSearchParameterField(
                CigarSearchFields.Name,
                CigarSearchFields.localized(CigarSearchFields.Name),
                false
            )

            CigarSearchFields.Brand -> CigarsSearchBrandField(
                CigarSearchFields.Brand,
                CigarSearchFields.localized(CigarSearchFields.Brand),
                false,
                this
            )

            CigarSearchFields.Country -> CigarsSearchParameterField(
                CigarSearchFields.Country,
                CigarSearchFields.localized(CigarSearchFields.Country),
                false
            )
        }
        searchParams = searchParams + field
    }

    fun removeSearchParameter(parameter: SearchParameterField<CigarSearchFields>) {
        searchParams = searchParams - parameter
    }

    val hasMoreSearchParameters: Boolean
        get() = sortingMenu().isNotEmpty()

    fun sortingMenu(): List<Pair<CigarSearchFields, String>> {
        val list = CigarSearchFields.enumValues().filter { field ->
            searchParams.firstOrNull { it.type == field.first } == null
        }
        return list
    }

    fun setFieldValue(parameter: CigarSearchFields, value: Any?) {
        when (parameter) {
            CigarSearchFields.Name -> name = value as String
            CigarSearchFields.Country -> country = value as String
            CigarSearchFields.Brand -> brand = value as RapidCigarBrand
        }
    }

    override fun sortingOrder(ascending: Boolean) {
        accenting = ascending
        sortEntities(entities)
    }

    override fun sorting(sorting: String) {
        sortField = sorting
        sortEntities(entities)
    }

    private fun sortEntities(list: List<Cigar>) {
        val field = when (sortField) {
            CigarSearchFields.Name.name -> CigarSearchFields.Name
            CigarSearchFields.Brand.name -> CigarSearchFields.Brand
            CigarSearchFields.Country.name -> CigarSearchFields.Country
            else -> CigarSearchFields.Name
        }

        entities = list.sortedWith(compareBy {
            when (field) {
                CigarSearchFields.Name -> it.name
                CigarSearchFields.Brand -> it.brand
                CigarSearchFields.Country -> it.country
            }
        }).also {
            if (!accenting) {
                it.reversed()
            }
        }
    }

    private val canLoad: Boolean
        get() {
            var can = true
            searchParams.forEach {
                can = when (it.type) {
                    CigarSearchFields.Name -> can && name.isNotBlank()
                    CigarSearchFields.Brand -> can && brand != null
                    CigarSearchFields.Country -> can && country.isNotBlank()
                }
            }
            return can
        }

    override fun loadEntities(reload: Boolean) {
        if (canLoad) {
            if (request == null || (request != null && request?.name != name && brand != null && request?.brand != brand?.brandId && request?.country != country)) {
                request = GetCigarsRequest(name = name.ifBlank { null },
                    brand = brand?.brandId,
                    country = country.ifBlank { null })
            }
            loading = true
            request?.next()?.subscribe {
                Log.debug("Cigars: ${it.size}")
                loading = false
                if (it.isNotEmpty()) {
                    val list = entities + it
                    sortEntities(list)
                }
            }
        }
    }

    fun getBrands(query: String): ObservableWrapper<List<RapidCigarBrand>> {
        return observable { emitter ->
            if (query.isNotBlank()) {
                if (brandRequest == null || brandRequest?.brand != query) {
                    brandRequest = GetCigarsBrands(brand = query)
                }
                loading = true
                brandRequest?.next()?.subscribe {
                    Log.debug("Brands: ${it.size}")
                    loading = false
                    emitter.onNext(it)
                }
            }
        }.wrap()
    }

    override fun loadMore() {
        Log.debug("loadMore")
        loadEntities(false)
    }

    sealed interface Actions {
        data class ShowError(val error: StringDesc) : Actions
    }
}