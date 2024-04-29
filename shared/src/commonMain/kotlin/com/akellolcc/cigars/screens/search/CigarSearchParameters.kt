/*******************************************************************************************************************************************
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 4/28/24, 6:45 PM
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

class CigarSearchParameters() : SearchParameters<String, SearchParameterField<String>>() {
    init {
        params = CigarSortingFields.enumValues().map {
            SearchParam(it.first.name, it.first.value, it.second, Images.icon_menu_sort)
        }
        selectedParams = selectedParams + params[0]
    }

    override fun build(param: SearchParam<String>): SearchParameterField<String> {
        return if (param.key == CigarSortingFields.Brand.value) {
            CigarsSearchBrandField(param, false, null)
        } else {
            CigarsSearchParameterField(param, false, null)
        }
    }
}

class CigarSortingParameters() : SearchParameters<String, SearchParam<String>>() {
    init {
        params = CigarSortingFields.enumValues().map {
            SearchParam(it.first.value, it.first.value, it.second, Images.icon_menu_sort)
        }
        selectedParams = params
    }

    override fun build(param: SearchParam<String>): SearchParam<String> {
        return param
    }

}

