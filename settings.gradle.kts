pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "kmpRickRollApp"

include(":androidApp")
include(":shared")
include(":shared:core")
include(":shared:auth_domain")
include(":shared:auth_data")
include(":shared:player_domain")
include(":shared:player_data")
