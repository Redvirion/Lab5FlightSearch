pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
    plugins {
        id("com.android.application") version "9.2.0" apply false
        id("org.jetbrains.kotlin.android") version "2.3.21" apply false
        id("org.jetbrains.kotlin.kapt") version "2.3.21" apply false
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}
rootProject.name = "Lab5FlightSearch"
include(":app")