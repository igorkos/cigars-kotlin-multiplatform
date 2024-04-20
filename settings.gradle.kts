/*
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 4/17/24, 12:39 PM
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

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
        maven(url = "https://oss.sonatype.org/content/repositories/snapshots")
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        maven { url = uri("https://jitpack.io") } // for moko-media android picker
        maven{url = uri("https://oss.sonatype.org/content/repositories/snapshots")}
        maven { url = uri("https://maven.pkg.jetbrains.space/public/p/ktor/eap") }
        maven {url = uri("https://maven.pkg.jetbrains.space/kotlin/p/kotlin/dev") }
    }
}

rootProject.name = "Cigars"
include(":androidApp")
include(":shared")