/*******************************************************************************************************************************************
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 5/21/24, 7:21 PM
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

import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSetTree

/*******************************************************************************************************************************************
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 5/21/24, 10:35 AM
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

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.kotlinCocoapods)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.i18n)
    alias(libs.plugins.mokoCompose)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.sqldelight)
}

kotlin {
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "11"
            }
        }
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        instrumentedTestVariant {
            sourceSetTree.set(KotlinSourceSetTree.test)
            dependencies {
                implementation(libs.bundles.test.android.bundle)
            }
        }
    }
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    cocoapods {
        summary = "Some description for the Shared Module"
        homepage = "Link to the Shared Module homepage"
        version = "1.0"
        ios.deploymentTarget = "16.0"
        podfile = project.file("../iosApp/Podfile")
        framework {
            baseName = "shared"
            isStatic = true
        }
        extraSpecAttributes["resources"] = "['src/commonMain/resources/**']"
        extraSpecAttributes["exclude_files"] = "['src/commonMain/resources/MR/**']"
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material3)

                implementation(libs.kotlin.datetime)
                implementation(libs.kotlin.serialization)
                implementation(libs.kotlin.stdlib)

                implementation(libs.napier)
                implementation(libs.i18n)
                implementation(libs.locale)

                implementation(libs.bundles.moko.common)
                // Navigator
                implementation(libs.bundles.voyager.common)

                // peekaboo-ui
                implementation(libs.peekaboo.image.picker)

                implementation(libs.cloudinary)
                implementation(libs.multiplatform.settings)

                implementation(libs.bundles.sqldelight.common)

                implementation(libs.bundles.ktor.common)

                implementation(libs.atomicfu)

                implementation(libs.bundles.multiplatform.firebase)
            }
        }
        val commonTest by getting {
            dependencies {
                @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
                implementation(compose.uiTest)
                implementation(libs.bundles.test.common.bundle)
            }
        }

        val androidInstrumentedTest by getting {
            dependencies {
                implementation(libs.bundles.test.android.bundle)
            }
        }

        val androidMain by getting {
            dependsOn(commonMain)
            dependencies {
                api(libs.androidx.activity.compose)
                implementation(libs.sqldelight.android)
                implementation(libs.androidx.foundation.android)
                implementation(libs.accompanist.permissions)
                implementation(libs.ktor.client.android.okhttp)
                implementation(libs.androidx.datastore.core.android)
            }
        }


        val iosMain by creating {
            dependsOn(commonMain)
            dependencies {
                implementation(libs.bundles.sqldelight.ios.bundle)
                implementation(libs.ktor.client.ios)
            }
        }

        val iosX64Main by getting {
            dependsOn(iosMain)
        }
        val iosArm64Main by getting {
            dependsOn(iosMain)
        }
        val iosSimulatorArm64Main by getting {
            dependsOn(iosMain)
        }
    }
}


multiplatformResources {
    multiplatformResourcesPackage = "com.akellolcc.cigars"
}

i18n4k {
    sourceCodeLocales = listOf("en", "ru")
}

android {
    namespace = "com.akellolcc.cigars"
    compileSdk = 34
    defaultConfig {
        minSdk = 31
        targetSdk = 34
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildToolsVersion = "34.0.0"
    ndkVersion = "26.1.10909125"
    kotlin {
        jvmToolchain(11)
    }
    dependencies {
        implementation(libs.androidx.exifinterface)
    }
}


sqldelight {
    databases {
        create("CigarsDatabase") {
            packageName.set("com.akellolcc.cigars.databases")
            generateAsync.set(true)
        }
    }
}