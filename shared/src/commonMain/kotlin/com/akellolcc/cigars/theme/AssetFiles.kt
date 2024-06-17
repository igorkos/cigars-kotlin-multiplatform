/*******************************************************************************************************************************************
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 6/14/24, 11:59 PM
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

package com.akellolcc.cigars.theme

import com.akellolcc.cigars.MR
import dev.icerock.moko.resources.FileResource

expect fun readTextFile(file: FileResource): String?
class AssetFiles {
    companion object {
        val demo_humidors = MR.files.demo_humidors
        val demo_cigars_images = MR.files.demo_cigars_images
        val demo_cigars = MR.files.demo_cigars
        val test_cigars = MR.files.test_cigars
        val test_humidors = MR.files.test_humidors
    }
}