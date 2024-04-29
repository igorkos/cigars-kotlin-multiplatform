/*******************************************************************************************************************************************
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 4/28/24, 12:59 PM
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

package com.akellolcc.cigars.mvvm

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.akellolcc.cigars.databases.createRepository
import com.akellolcc.cigars.databases.extensions.Cigar
import com.akellolcc.cigars.databases.extensions.CigarSortingFields
import com.akellolcc.cigars.databases.rapid.rest.GetCigarsBrands
import com.akellolcc.cigars.databases.rapid.rest.GetCigarsRequest
import com.akellolcc.cigars.databases.rapid.rest.RapidCigarBrand
import com.akellolcc.cigars.databases.repository.CigarsRepository
import com.akellolcc.cigars.logging.Log
import dev.icerock.moko.resources.desc.StringDesc


class SearchCigarScreenViewModel() :
    BaseListViewModel<Cigar, SearchCigarScreenViewModel.Actions>() {
    override val repository: CigarsRepository = createRepository(CigarsRepository::class)

    private var request: GetCigarsRequest? = null
    private var brandRequest: GetCigarsBrands? = null

    var name by mutableStateOf("")
    var country by mutableStateOf("")

    //CigarsSearchBrandField values
    var brand by mutableStateOf<RapidCigarBrand?>(null)
    var brands by mutableStateOf(listOf<RapidCigarBrand>())
    var expanded by mutableStateOf(false)

    init {
        //addSearchParameter(CigarSearchFields.Name)
        //sortField = CigarSearchFields.Name.value
    }

    override fun entitySelected(entity: Cigar) {
    }


    fun setFieldValue(parameter: CigarSortingFields, value: Any?) {
        when (parameter) {
            CigarSortingFields.Name -> name = value as String
            CigarSortingFields.Country -> country = value as String
            CigarSortingFields.Brand -> brand = value as RapidCigarBrand
            CigarSortingFields.Shape -> TODO()
            CigarSortingFields.Gauge -> TODO()
            CigarSortingFields.Length -> TODO()
        }
    }

    override fun sortingOrder(ascending: Boolean) {
        accenting = ascending
        sortEntities(entities)
    }

    private fun sortEntities(list: List<Cigar>) {
        val field = when (sortField) {
            CigarSortingFields.Name.name -> CigarSortingFields.Name
            CigarSortingFields.Brand.name -> CigarSortingFields.Brand
            CigarSortingFields.Country.name -> CigarSortingFields.Country
            else -> CigarSortingFields.Name
        }

        entities = list.sortedWith(compareBy {
            when (field) {
                CigarSortingFields.Name -> it.name
                CigarSortingFields.Brand -> it.brand
                CigarSortingFields.Country -> it.country
                else -> CigarSortingFields.Name
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
            /*searchParams.forEach {
                can = when (it.type) {
                    CigarSearchFields.Name -> can && name.isNotBlank()
                    CigarSearchFields.Brand -> can && brand != null
                    CigarSearchFields.Country -> can && country.isNotBlank()
                }
            }*/
            return can
        }

    override fun loadEntities(reload: Boolean) {
        if (canLoad) {
            if (request == null || (request != null && request?.name != name && brand != null && request?.brand != brand?.brandId && request?.country != country)) {
                request = GetCigarsRequest(name = name.ifBlank { null },
                    brand = brand?.brandId,
                    country = country.ifBlank { null })
            }
            request?.let {
                execute(it.next()) { cigars ->
                    Log.debug("Cigars: ${cigars.size}")
                    if (cigars.isNotEmpty()) {
                        val list = entities + it
                        sortEntities(cigars)
                    }
                }
            }
        }
    }

    fun getBrands(query: String) {
        if (query.isNotBlank()) {
            if (brandRequest == null || brandRequest?.brand != query) {
                brandRequest = GetCigarsBrands(brand = query)
            }
            execute(brandRequest!!.next()) {
                brands = it
                expanded = true
            }
        }
    }

    fun selectBrand(brand: RapidCigarBrand) {
        this.brand = brand
        expanded = false
        sendEvent(Actions.BrandSelected(brand))
    }

    override fun loadMore() {
        Log.debug("loadMore")
        loadEntities(false)
    }

    companion object Factory : ViewModelsFactory<SearchCigarScreenViewModel>() {
        override fun factory(data: Any?): SearchCigarScreenViewModel {
            return SearchCigarScreenViewModel()
        }
    }

    sealed interface Actions {
        data class BrandSelected(val brand: RapidCigarBrand) : Actions
        data class ShowError(val error: StringDesc) : Actions
    }
}