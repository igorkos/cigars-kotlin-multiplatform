/*******************************************************************************************************************************************
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 5/8/24, 3:56 PM
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

package com.akellolcc.cigars.mvvm.search

import com.akellolcc.cigars.screens.components.search.data.FilterParameter
import com.akellolcc.cigars.utils.ObjectFactory

class CigarsSearchFieldViewModel(parameter: FilterParameter<*>) :
    CigarsSearchFieldBaseViewModel(parameter) {

    override fun validate(): Boolean {
        val valid = value.isNotEmpty() && value.length >= 3
        annotation = if (valid) {
            null
        } else {
            "Please enter at least 3 characters to narrow down your search"
        }
        isError = !valid
        return valid
    }

    @Suppress("UNCHECKED_CAST")
    companion object Factory : ObjectFactory<CigarsSearchFieldViewModel>() {
        override fun factory(data: Any?): CigarsSearchFieldViewModel {
            return CigarsSearchFieldViewModel(data as FilterParameter<String>)
        }
    }

}
