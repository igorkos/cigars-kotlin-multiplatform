plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.i18n)
    alias(libs.plugins.mokoCompose)
    kotlin("plugin.serialization").version(libs.versions.kotlin)
    id("app.cash.sqldelight").version(libs.versions.sqldelight.plugin.version)
}



kotlin {
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "11"
            }
        }
    }

    iosX64()
    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material)
                implementation(compose.material3)

                @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
                implementation(compose.components.resources)

                implementation(libs.moko.resources)
                implementation(libs.moko.resources.compose)

                implementation(libs.napier)
                implementation(libs.i18n)
                implementation(libs.locale)

                implementation(libs.mvvm.core) // only ViewModel, EventsDispatcher, Dispatchers.UI
                implementation(libs.mvvm.flow) // api mvvm-core, CFlow for native and binding extensions
                implementation(libs.mvvm.livedata) // api mvvm-core, LiveData and extensions
                implementation(libs.mvvm.state) // api mvvm-livedata, ResourceState class and extensions
                implementation(libs.mvvm.livedata.resources) // api mvvm-core, moko-resources, extensions for LiveData with moko-resources
                implementation(libs.mvvm.flow.resources) // api mvvm-core, moko-resources, extensions for Flow with moko-resources
                implementation(libs.mvvm.flow.compose) // api mvvm-flow, binding extensions for Compose Multiplatform
                implementation(libs.mvvm.livedata.compose) // api mvvm-livedata, binding extensions for Compose Multiplatform
                implementation(libs.moko.mvvm)

                // Navigator
                implementation(libs.voyager.navigator)

                // BottomSheetNavigator
                implementation(libs.voyager.bottom.sheet.navigator)

                // TabNavigator
                implementation(libs.voyager.tab.navigator)

                // Transitions
                implementation(libs.voyager.transitions)

                //Database
                implementation(libs.sqldelight.driver)
            }
        }

        val androidMain by getting {
            dependsOn(commonMain)
            dependencies {
                api(libs.androidx.activity.compose)
                implementation(libs.sqldelight.android)
            }
        }

        val iosMain by creating {
            dependsOn(commonMain)
            dependencies {
                implementation(libs.sqldelight.ios)
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
    multiplatformResourcesPackage = "com.akellolcc.cigars.common"
}

android {
    namespace = "com.akellolcc.cigars.common"
    compileSdk = 34
    defaultConfig {
        minSdk = 26
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlin {
        jvmToolchain(11)
    }
}

sqldelight {
    databases {
        create("CigarsDatabase") {
            packageName.set( "com.akellolcc.cigars.common.databases")
            generateAsync.set(true)
        }
    }
}