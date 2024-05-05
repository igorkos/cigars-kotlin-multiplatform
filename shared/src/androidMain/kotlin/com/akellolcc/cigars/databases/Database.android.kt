/*******************************************************************************************************************************************
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 5/1/24, 12:55 AM
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

package com.akellolcc.cigars.databases

import com.akellolcc.cigars.databases.extensions.Cigar
import com.akellolcc.cigars.databases.extensions.CigarImage
import com.akellolcc.cigars.databases.extensions.Humidor
import com.akellolcc.cigars.databases.test.DemoTestSets
import com.akellolcc.cigars.theme.AssetFiles
import com.akellolcc.cigars.theme.readTextFile
import dev.icerock.moko.resources.FileResource
import kotlinx.serialization.json.Json

@Suppress("UNCHECKED_CAST")
actual fun <T> loadDemoSet(
    resource: FileResource,
    inMemory: Boolean
): List<T> {
    if (inMemory) {
        return when (resource) {
            AssetFiles.demo_humidors -> Json.decodeFromString<List<Humidor>>(DemoTestSets.humidorsSet) as List<T>
            AssetFiles.demo_cigars -> Json.decodeFromString<List<Cigar>>(DemoTestSets.cigarsSet) as List<T>
            else -> emptyList()
        }
    } else {
        val jsonString = readTextFile(resource) ?: ""
        return when (resource) {
            AssetFiles.demo_humidors -> Json.decodeFromString<List<Humidor>>(jsonString) as List<T>
            AssetFiles.demo_cigars -> Json.decodeFromString<List<Cigar>>(jsonString) as List<T>
            AssetFiles.demo_cigars_images -> Json.decodeFromString<List<CigarImage>>(
                jsonString
            ) as List<T>

            else -> emptyList()
        }
    }
}