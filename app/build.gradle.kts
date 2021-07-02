import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("com.android.application")
    kotlin("android")
}

tasks.withType<KotlinCompile>().all {
    kotlinOptions.freeCompilerArgs += listOf(
        "-Xopt-in=androidx.compose.material.ExperimentalMaterialApi",
        "-Xopt-in=kotlin.time.ExperimentalTime"
    )
    kotlinOptions.jvmTarget = "1.8"
}

android {
    compileSdk = AndroidSdk.COMPILE
    buildToolsVersion = AndroidSdk.BUILD_TOOLS

    defaultConfig {
        applicationId = AppInfo.APPLICATION_ID
        minSdk = AndroidSdk.MIN
        targetSdk = AndroidSdk.TARGET
        versionCode = AppInfo.MIN_VERSION_CODE
        versionName = "AppInfo.VERSION_CODE_APP_NAME"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                File("proguard-rules.pro")
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = Libs.AndroidX.Compose.VERSION
    }
}

dependencies {

    with(Libs.Kotlin.Bom) {
        implementation(
            group = GROUP,
            name = NAME,
            ext = EXT,
            version = Libs.Kotlin.VERSION
        )
    }

    with(Libs.Kotlin.Coroutines.Bom) {
        implementation(
            group = GROUP,
            name = NAME,
            ext = EXT,
            version = Libs.Kotlin.Coroutines.VERSION
        )
    }
    implementation(Libs.Kotlin.Coroutines.ANDROID)
    implementation(Libs.AndroidX.CORE_KTX)
    implementation(Libs.AndroidX.APP_COMPAT)
    implementation(Libs.Google.Material.MATERIAL)
    implementation(Libs.AndroidX.Compose.UI)
    implementation(Libs.AndroidX.Compose.MATERIAL)
    implementation(Libs.AndroidX.Compose.UI_TOOLING)
    implementation(Libs.AndroidX.Lifecycle.RUNTIME_KTX)
    implementation(Libs.AndroidX.Compose.ACTIVITY)
    implementation(Libs.AndroidX.Compose.LIFECYCLE_VIEWMODEL)


    testImplementation(Libs.Test.JUnit.JUNIT)
    androidTestImplementation(Libs.Test.AndroidX.JUNIT)
    androidTestImplementation(Libs.Test.AndroidX.ESPRESSO)
    androidTestImplementation(Libs.Test.AndroidX.COMPOSE_JUNIT)
}