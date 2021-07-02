@file:Suppress("MemberVisibilityCanBePrivate")

object Libs {
    object Kotlin {
        const val VERSION = "1.5.10"

        object Bom {
            const val GROUP = "org.jetbrains.kotlin"
            const val NAME = "kotlin-bom"
            const val EXT = "pom"
        }

        const val GRADLE_PLUGIN = "org.jetbrains.kotlin:kotlin-gradle-plugin:$VERSION"
        const val ANDROID_EXTENSIONS_GRADLE_PLUGIN = "org.jetbrains.kotlin:kotlin-android-extensions:$VERSION"

        object Serialization {
            const val GRADLE_PLUGIN = "org.jetbrains.kotlin:kotlin-serialization:$VERSION"
            const val CORE = "org.jetbrains.kotlinx:kotlinx-serialization-core"
        }

        object Coroutines {
            const val VERSION = "1.5.0"

            object Bom {
                const val GROUP = "org.jetbrains.kotlinx"
                const val NAME = "kotlinx-coroutines-bom"
                const val EXT = "pom"
            }

            const val ANDROID = "org.jetbrains.kotlinx:kotlinx-coroutines-android:$VERSION"
            const val PLAY_SERVICES = "org.jetbrains.kotlinx:kotlinx-coroutines-play-services:$VERSION"
            const val TEST = "org.jetbrains.kotlinx:kotlinx-coroutines-test:$VERSION"
        }

        const val STDLIB = "org.jetbrains.kotlin:kotlin-stdlib"
    }
    object Android {
        const val TOOLS_GRADLE_PLUGIN = "com.android.tools.build:gradle:7.1.0-alpha02"
    }
    object AndroidX {
        const val APP_COMPAT = "androidx.appcompat:appcompat:1.3.0"
        const val CORE_KTX = "androidx.core:core-ktx:1.5.0"
        object Compose {
            const val VERSION = "1.0.0-beta08"

            const val UI = "androidx.compose.ui:ui:$VERSION"
            const val MATERIAL = "androidx.compose.material:material:$VERSION"
            const val UI_TOOLING = "androidx.compose.ui:ui-tooling:$VERSION"

            const val ACTIVITY = "androidx.activity:activity-compose:1.3.0-beta01"
            const val LIFECYCLE_VIEWMODEL = "androidx.lifecycle:lifecycle-viewmodel-compose:1.0.0-alpha06"
        }
        object Lifecycle {
            const val RUNTIME_KTX = "androidx.lifecycle:lifecycle-runtime-ktx:2.3.1"
        }
    }

    object Google {
        object Material {
            const val VERSION = "1.3.0"

            const val MATERIAL = "com.google.android.material:material:1.3.0"
        }
    }

    object Test {
        object AndroidX {
            const val JUNIT = "androidx.test.ext:junit:1.1.2"
            const val ESPRESSO = "androidx.test.espresso:espresso-core:3.3.0"
            const val COMPOSE_JUNIT = "androidx.compose.ui:ui-test-junit4:${Libs.AndroidX.Compose.VERSION}"
        }
        object JUnit {
            const val JUNIT = "junit:junit:4.+"
        }
    }
}