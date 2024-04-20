/*
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 4/19/24, 11:45 PM
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
import com.akellolcc.cigars.databases.rapid.rest.GetCigarsRequest
import com.akellolcc.cigars.databases.repository.CigarsRepository
import com.akellolcc.cigars.logging.Log
import dev.icerock.moko.resources.desc.StringDesc


class SearchCigarScreenViewModel() :
    BaseListViewModel<Cigar, SearchCigarScreenViewModel.Actions>() {
    override val repository: CigarsRepository = database.getRepository(RepositoryType.Cigars)

    private var request: GetCigarsRequest? = null
    var name by mutableStateOf("")
    var brand by mutableStateOf("")
    var country by mutableStateOf("")
    var searchParams by mutableStateOf(listOf(CigarSearchFields.Name))
    override fun entitySelected(entity: Cigar) {
    }

    fun addSearchParameter(parameter: CigarSearchFields) {
        searchParams = searchParams + parameter
    }

    fun sortingMenu(): List<Pair<CigarSearchFields, String>> {
        val list = CigarSearchFields.enumValues().filter {
            !searchParams.contains(it.first)
        }
        return list
    }

    fun setFieldValue(parameter: CigarSearchFields, value: String) {
        when (parameter) {
            CigarSearchFields.Name -> name = value
            CigarSearchFields.Country -> country = value
            CigarSearchFields.Brand -> brand = value
        }
    }

    override fun sortingOrder(ascending: Boolean) {
        accenting = ascending
        sortEntities(entities)
    }

    private fun sortEntities(list: List<Cigar>) {
        entities = if (accenting) {
            list.sortedWith(compareBy { it.name })
        } else {
            list.sortedWith(compareBy { it.name }).reversed()
        }
    }

    private val canLoad: Boolean
        get() {
            var can = true
            searchParams.forEach {
                can = when (it) {
                    CigarSearchFields.Name -> can && name.isNotBlank()
                    CigarSearchFields.Brand -> can && brand.isNotBlank()
                    CigarSearchFields.Country -> can && country.isNotBlank()
                }
            }
            return can
        }

    override fun loadEntities(reload: Boolean) {
        if (canLoad) {
            if (request == null || (request != null && request?.name != name && request?.brand != brand && request?.country != country)) {
                request = GetCigarsRequest(name = name.ifBlank { null },
                    brand = brand.ifBlank { null },
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

    override fun loadMore() {
        loadEntities(false)
    }

    sealed interface Actions {
        data class ShowError(val error: StringDesc) : Actions
    }
}