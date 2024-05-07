/*******************************************************************************************************************************************
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 5/6/24, 12:33 PM
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
import androidx.compose.ui.text.TextRange
import cafe.adriel.voyager.core.model.screenModelScope
import com.akellolcc.cigars.databases.rapid.rest.GetCigarsBrands
import com.akellolcc.cigars.databases.rapid.rest.RapidCigarBrand
import com.akellolcc.cigars.logging.Log
import com.akellolcc.cigars.screens.search.data.FilterParameter
import com.akellolcc.cigars.utils.ObjectFactory
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class CigarsBrandsSearchViewModel(parameter: FilterParameter<Long>) :
    CigarsSearchFieldBaseViewModel(parameter) {

    //Brand request
    private var brandRequest: GetCigarsBrands? = null
    private var brandJob: Job? = null

    //Input flow
    var inputSelection by mutableStateOf<TextRange?>(null)

    // Brands list
    var brands by mutableStateOf(listOf<RapidCigarBrand>())

    //Selected brand
    private var selectedBrand by mutableStateOf<RapidCigarBrand?>(null)
    var expanded by mutableStateOf(false)

    override fun processInput(value: String) {
        getBrands(value)
    }

    override fun updateInput(value: String) {
        if (value != selectedBrand?.name) {
            sendEvent(Action.Input())
            brands = listOf()
            selectedBrand = null
            expanded = false
        }
        inputSelection = null
        loading = true
        super.updateInput(value)
    }

    override fun validate(): Boolean {
        isError = !loading && value.length > 2 && brands.isEmpty()
        return selectedBrand != null
    }

    override val annotation: String?
        get() {
            return when (state.value) {
                is Action.Input -> null
                is Action.DropDown -> null
                is Action.Loading -> "Loading..."
                is Action.Selected -> null
                is Action.Error -> "No brands match your search"
                is Action.Idle -> null
                is Action.Loaded -> "Select brand from list"
                else -> null
            }
        }

    /**
     * Get focus state
     */
    override fun onFocusChange(focused: Boolean) {
        if (focused) {
            sendEvent(Action.Input())
        }
    }

    /**
     * Drop down menu state
     */
    fun onDropDown(dismiss: Boolean) {
        expanded = !dismiss
        if (!dismiss) {
            sendEvent(Action.DropDown())
        }
    }


    /**
     * Brand selected
     */
    fun onBrandSelected(brand: RapidCigarBrand) {
        selectedBrand = brand
        value = brand.name ?: ""
        inputSelection = TextRange(value.length)
        expanded = false
        parameter.set(brand.brandId.toString())
        sendEvent(Action.Selected())
    }


    private fun getBrands(query: String) {
        if (selectedBrand == null) {
            brandJob?.cancel()
            brandJob = null
            if (query.isNotBlank()) {
                sendEvent(Action.Loading())
                brandJob = screenModelScope.launch {
                    if (this.isActive) {
                        loading = true
                        if (brandRequest == null || brandRequest?.brand != query) {
                            brandRequest = GetCigarsBrands(brand = query)
                        }
                        Log.debug("Brand request: $query")
                        //delay(100)
                        //callback(listOf())
                        brandRequest!!.next().cancellable().collect {
                            Log.debug("Get Brands: $it")
                            brands = it
                            loading = false
                            if (it.isEmpty()) {
                                sendEvent(Action.Error())
                            } else {
                                sendEvent(Action.Loaded())
                            }
                        }
                    } else {
                        sendEvent(Action.Input())
                        Log.debug("Brand request cancelled")
                    }
                }
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    companion object Factory : ObjectFactory<CigarsBrandsSearchViewModel>() {
        override fun factory(data: Any?): CigarsBrandsSearchViewModel {
            return CigarsBrandsSearchViewModel(data as FilterParameter<Long>)
        }

    }
}
