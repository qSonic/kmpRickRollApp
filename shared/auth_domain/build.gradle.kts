plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidKotlinMultiplatformLibrary)
}

kotlin {
    android {
        namespace = "com.rickroll.template.shared.auth.domain"
        compileSdk = 36
        minSdk = 24
    }
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        commonMain.dependencies {
            api(project(":shared:core"))
            api(libs.kotlinx.coroutines.core)
            implementation(libs.koin.core)
        }

        iosMain.dependencies {
            implementation(libs.koin.core)
        }
    }
}
