plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidKotlinMultiplatformLibrary)
}

kotlin {
    android {
        namespace = "com.rickroll.template.shared"
        compileSdk = 36
        minSdk = 24
    }
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        commonMain.dependencies {
            api(project(":shared:core"))
            api(project(":shared:auth_domain"))
            api(project(":shared:player_domain"))

            implementation(project(":shared:auth_data"))
            implementation(project(":shared:player_data"))
            implementation(libs.koin.core)
        }

        androidMain.dependencies {
            implementation(libs.koin.android)
        }
    }

    targets.withType(org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget::class.java).configureEach {
        binaries.framework {
            baseName = "SharedKit"
            isStatic = true
            export(project(":shared:core"))
            export(project(":shared:auth_domain"))
            export(project(":shared:player_domain"))
        }
    }
}
