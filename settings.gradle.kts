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