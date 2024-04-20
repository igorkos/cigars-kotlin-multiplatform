/*
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 4/15/24, 10:04 PM
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

import com.akellolcc.cigars.databases.RepositoryType
import com.akellolcc.cigars.databases.extensions.Cigar
import com.akellolcc.cigars.databases.repository.CigarsRepository
import dev.icerock.moko.resources.desc.StringDesc


open class CigarsScreenViewModel : BaseListViewModel<Cigar, CigarsScreenViewModel.CigarsAction>() {
    override val repository: CigarsRepository = database.getRepository(RepositoryType.Cigars)

    override fun entitySelected(cigar: Cigar) {
        sendEvent(CigarsAction.RouteToCigar(cigar))
    }

    sealed interface CigarsAction {
        data class RouteToCigar(val cigar: Cigar) : CigarsAction
        data class ShowError(val error: StringDesc) : CigarsAction
    }
}