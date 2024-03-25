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
                implementation(compose.material)
                implementation(compose.material3)

                implementation(libs.kotlin.datetime)
                implementation(libs.kotlin.serialization)

                implementation(libs.napier)
                implementation(libs.i18n)
                implementation(libs.locale)

                implementation(libs.moko.resources)
                implementation(libs.moko.resources.compose)

                // Navigator
                implementation(libs.voyager.navigator)
                // BottomSheetNavigator
                implementation(libs.voyager.bottom.sheet.navigator)
                // TabNavigator
                implementation(libs.voyager.tab.navigator)
                // Transitions
                implementation(libs.voyager.transitions)

                implementation(libs.mvvm.core) // only ViewModel, EventsDispatcher, Dispatchers.UI
                implementation(libs.mvvm.flow) // api mvvm-core, CFlow for native and binding extensions
                implementation(libs.mvvm.livedata) // api mvvm-core, LiveData and extensions
                implementation(libs.mvvm.state) // api mvvm-livedata, ResourceState class and extensions
                implementation(libs.mvvm.livedata.resources) // api mvvm-core, moko-resources, extensions for LiveData with moko-resources
                implementation(libs.mvvm.flow.resources) // api mvvm-core, moko-resources, extensions for Flow with moko-resources
                implementation(libs.mvvm.flow.compose) // api mvvm-flow, binding extensions for Compose Multiplatform
                implementation(libs.mvvm.livedata.compose) // api mvvm-livedata, binding extensions for Compose Multiplatform
                implementation(libs.moko.mvvm)

                implementation(libs.multiplatform.settings)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(libs.kotlin.test)
                implementation(libs.moko.resources.test)
            }
        }

        val androidMain by getting {
            dependsOn(commonMain)
            dependencies {
                api(libs.androidx.activity.compose)
                implementation(libs.sqldelight.android)
                implementation(libs.compose.ui.tooling)
                implementation(libs.compose.ui.tooling.preview)
                implementation(libs.androidx.foundation.android)
                implementation(libs.ui.tooling.preview.android)
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
    multiplatformResourcesPackage = "com.akellolcc.cigars"
}

i18n4k {
    sourceCodeLocales = listOf("en", "ru")
}

android {
    namespace = "com.akellolcc.cigars"
    compileSdk = 34
    defaultConfig {
        minSdk = 29
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlin {
        jvmToolchain(11)
    }
}
dependencies {

}

sqldelight {
    databases {
        create("CigarsDatabase") {
            packageName.set( "com.akellolcc.cigars.databases")
            generateAsync.set(true)
        }
    }
}