/*******************************************************************************************************************************************
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 5/8/24, 3:08 PM
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

package com.akellolcc.cigars.screens.components.search.data

import com.akellolcc.cigars.databases.extensions.CigarSortingFields
import com.akellolcc.cigars.screens.components.search.CigarsSearchBrandField
import com.akellolcc.cigars.screens.components.search.CigarsSearchParameterField
import com.akellolcc.cigars.screens.components.search.SearchParameterField
import com.akellolcc.cigars.theme.Images

class CigarFilterParameters : FilterCollection() {
    init {
        params = CigarSortingFields.enumValues().map {
            FilterParameter(it.first.value, it.first.value, it.second, Images.icon_menu_sort)
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun build(param: FilterParameter<*>): SearchParameterField<*> {
        return if (param.key == CigarSortingFields.Gauge.value) {
            CigarsSearchParameterField(param as FilterParameter<Long>)
        } else {
            CigarsSearchParameterField(param as FilterParameter<String>)
        }
    }
}

class CigarSortingParameters : FilterCollection() {
    init {
        params = CigarSortingFields.enumValues().map {
            FilterParameter(it.first.value, true, it.second, Images.icon_menu_sort)
        }
        selectedParams = params
    }

    override fun build(param: FilterParameter<*>): SearchParameterField<*> {
        TODO("Not yet implemented")
    }
}

class CigarSearchParameters : FilterCollection() {
    init {
        params = CigarSortingFields.enumValues().filter {
            it.first != CigarSortingFields.Shape
        }.map {
            if (it.first == CigarSortingFields.Brand || it.first == CigarSortingFields.Name || it.first == CigarSortingFields.Country) {
                FilterParameter(it.first.value, "", it.second, Images.icon_menu_sort)
            } else {
                FilterParameter(it.first.value, 0L, it.second, Images.icon_menu_sort)
            }
        }
        selectedParams = params.filter { it.key == CigarSortingFields.Brand.value }
        controls = selectedParams.map { build(it) }
    }

    @Suppress("UNCHECKED_CAST")
    override fun build(param: FilterParameter<*>): SearchParameterField<*> {
        return when (param.key) {
            CigarSortingFields.Brand.value -> {
                CigarsSearchBrandField(param as FilterParameter<String>)
            }

            CigarSortingFields.Name.value, CigarSortingFields.Country.value -> {
                CigarsSearchParameterField(param as FilterParameter<String>)
            }

            else -> {
                CigarsSearchParameterField(param as FilterParameter<Long>)
            }
        }
    }
}


