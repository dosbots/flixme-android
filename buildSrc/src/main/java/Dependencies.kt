object Dependencies {
    object AndroidX {
        const val core = "androidx.core:core-ktx:${Versions.AndroidX.core}"
        const val lifecycle = "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.AndroidX.lifecycle}"
    }
    object Firebase {
        const val bom = "com.google.firebase:firebase-bom:${Versions.Firebase.bom}"
        const val analytics = "com.google.firebase:firebase-analytics-ktx"
        const val auth = "com.google.firebase:firebase-auth-ktx"
        const val firestore = "com.google.firebase:firebase-firestore"
    }
    object Compose {
        const val bom = "androidx.compose:compose-bom:${Versions.Compose.bom}"
        const val activity = "androidx.activity:activity-compose:${Versions.Compose.activity}"
        const val ui = "androidx.compose.ui:ui"
        const val uiGraphics = "androidx.compose.ui:ui-graphics"
        const val uiToolingPreview = "androidx.compose.ui:ui-tooling-preview"
        const val uiTooling = "androidx.compose.ui:ui-tooling"
        const val material3 = "androidx.compose.material3:material3"
        const val navigation = "androidx.navigation:navigation-compose:${Versions.Compose.navigation}"
        const val viewModelLifecycle = "androidx.lifecycle:lifecycle-viewmodel-compose:${Versions.Compose.viewModelLifecycle}"
        const val runtimeLifecycle = "androidx.lifecycle:lifecycle-runtime-compose:${Versions.Compose.runtimeLifecycle}"
        const val hiltNavigation = "androidx.hilt:hilt-navigation-compose:${Versions.Compose.hiltNavigation}"

        const val uiTestManifest = "androidx.compose.ui:ui-test-manifest"
        const val composeTestJUnit4 = "androidx.compose.ui:ui-test-junit4"
    }
    object Hilt {
        const val core = "com.google.dagger:hilt-android:${Versions.Hilt.core}"
        const val compiler = "com.google.dagger:hilt-compiler:${Versions.Hilt.core}"
    }
    object Testing {
        const val jUnit = "junit:junit:${Versions.Testing.jUnit}"
        const val androidTestJUnitExt = "androidx.test.ext:junit:${Versions.Testing.androidTestJUnitExt}"
        const val espressoCore = "androidx.test.espresso:espresso-core:${Versions.Testing.espressoCore}"
        const val mockk = "io.mockk:mockk:${Versions.Testing.mockk}"
        const val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.Testing.coroutines}"
        const val truth = "com.google.truth:truth:${Versions.Testing.truth}"
    }
    object Moshi {
        const val core = "com.squareup.moshi:moshi:${Versions.Moshi.core}"
        const val kotlin = "com.squareup.moshi:moshi-kotlin:${Versions.Moshi.core}"
        const val adapters = "com.squareup.moshi:moshi-adapters:${Versions.Moshi.core}"
    }
    object PlayServices {
        const val auth = "com.google.android.gms:play-services-auth:${Versions.PlayServices.auth}"
    }
    object Room {
        const val room = "androidx.room:room-runtime:${Versions.Room.room}"
        const val roomKtx = "androidx.room:room-ktx:${Versions.Room.room}"
        const val compiler = "androidx.room:room-compiler:${Versions.Room.room}"
    }
}