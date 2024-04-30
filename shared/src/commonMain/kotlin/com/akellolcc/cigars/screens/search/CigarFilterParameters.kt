/*******************************************************************************************************************************************
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 4/29/24, 3:38 PM
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

package com.akellolcc.cigars.screens.search

import com.akellolcc.cigars.databases.extensions.CigarSortingFields
import com.akellolcc.cigars.theme.Images

class CigarFilterParameters() : FilterCollection<String, SearchParameterField<String>>() {
    init {
        params = CigarSortingFields.enumValues().map {
            FilterParameter(it.first.value, it.first.value, it.second, Images.icon_menu_sort)
        }
        selectedParams = selectedParams + params[0]
    }

    override fun build(param: FilterParameter<String>): SearchParameterField<String> {
        return CigarsSearchParameterField(param, false, null)
    }
}

class CigarSortingParameters : FilterCollection<Boolean, FilterParameter<Boolean>>() {
    init {
        params = CigarSortingFields.enumValues().map {
            FilterParameter<Boolean>(it.first.value, true, it.second, Images.icon_menu_sort)
        }
        selectedParams = params
    }

    override fun build(param: FilterParameter<Boolean>): FilterParameter<Boolean> {
        return param
    }

}

class CigarSearchParameters() : FilterCollection<String, SearchParameterField<String>>() {
    init {
        params = CigarSortingFields.enumValues().filter {
            it.first != CigarSortingFields.Shape
        }.map {
            FilterParameter(it.first.value, "", it.second, Images.icon_menu_sort)
        }
        selectedParams = selectedParams + params[0]
    }

    override fun build(param: FilterParameter<String>): SearchParameterField<String> {
        return if (param.key == CigarSortingFields.Brand.value) {
            CigarsSearchBrandField(param, false, null)
        } else {
            CigarsSearchParameterField(param, false, null)
        }
    }
}


