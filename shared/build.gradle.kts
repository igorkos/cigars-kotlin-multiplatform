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
                implementation(libs.kotlin.stdlib)

                implementation(libs.napier)
                implementation(libs.i18n)
                implementation(libs.locale)

                implementation(libs.bundles.moko.common)
                // Navigator
                implementation(libs.bundles.voyager.common)

                // peekaboo-ui
                implementation(libs.peekaboo.ui)
                implementation(libs.peekaboo.image.picker)

                implementation(libs.cloudinary)
                implementation(libs.multiplatform.settings)

                implementation(libs.bundles.sqldelight.common)

                implementation(libs.bundles.ktor.common)
                //implementation(libs.hyperdrive.multiplatformx.api)
                //implementation(libs.hyperdrive.multiplatformx.compose)
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
                implementation(libs.accompanist.permissions)
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
        minSdk = 31
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

    implementation(libs.ui.tooling.preview.android)
    implementation(libs.androidx.exifinterface)
}

sqldelight {
    databases {
        create("CigarsDatabase") {
            packageName.set("com.akellolcc.cigars.databases")
            generateAsync.set(true)
        }
    }
}