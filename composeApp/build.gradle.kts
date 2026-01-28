import org.gradle.kotlin.dsl.implementation
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.sqldelight)
    alias(libs.plugins.i18n)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.firebase.crashlytics)
    alias(libs.plugins.google.gms.services)
}

i18n4k {
    sourceCodeLocales = listOf("en", "ru")
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }
    
    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }
    
    sourceSets {
        androidMain.dependencies {
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.sqldelight.android)
            implementation(libs.accompanist.permissions)
            implementation(libs.androidx.exifinterface)
            implementation(libs.sqldelight.driver.jdbc)
            implementation(project.dependencies.platform(libs.android.firebase.bom))
            implementation(libs.bundles.android.firebase)
        }
        commonMain.dependencies {
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.material.icons)
            implementation(libs.compose.ui)
            implementation(libs.compose.components.resources)
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            implementation(libs.bundles.voyager.common)
            implementation(libs.peekaboo.image.picker)
            implementation(libs.kotlin.serialization)
            implementation(libs.bundles.ktor.common)
            implementation(libs.bundles.sqldelight.common)
            implementation(libs.napier)
            implementation(libs.kotlin.datetime)
            implementation(libs.multiplatform.settings)
            implementation(libs.i18n)
            implementation(libs.locale)
            api(libs.firebase.crashlytics)
        }
        iosMain.dependencies {
            implementation(libs.bundles.sqldelight.ios.bundle)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
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

}

android {
    namespace = "com.akellolcc.cigars.android"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.akellolcc.cigars.android"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("debug") {
            isMinifyEnabled = false
            versionNameSuffix = "-DEBUG"
        }
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    debugImplementation(libs.compose.uiTooling)
}
