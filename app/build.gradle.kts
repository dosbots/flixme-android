plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.dagger.hilt.android")
    id("kotlin-parcelize")
    id("kotlin-kapt")
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.dosbots.flixme"
    compileSdk = Versions.Config.compileSdk

    defaultConfig {
        applicationId = "com.dosbots.flixme"
        minSdk = Versions.Config.minSdk
        targetSdk = Versions.Config.targetSdk
        versionCode = Versions.Config.versionCode
        versionName = Versions.Config.versionName

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
                "proguard-rules.pro"
            )
        }
    }

    flavorDimensions += "version"
    productFlavors {
        create("developer") {
            applicationIdSuffix = ".dev"
            versionNameSuffix = "-dev"
        }
        create("production") {
            applicationIdSuffix = ".prod"
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    kotlin {
        jvmToolchain(11)
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(Dependencies.AndroidX.core)
    implementation(Dependencies.AndroidX.lifecycle)

    implementation(platform(Dependencies.Compose.bom))
    implementation(Dependencies.Compose.activity)
    implementation(Dependencies.Compose.ui)
    implementation(Dependencies.Compose.uiGraphics)
    implementation(Dependencies.Compose.uiToolingPreview)
    implementation(Dependencies.Compose.material3)
    implementation(Dependencies.Compose.navigation)
    implementation(Dependencies.Compose.viewModelLifecycle)
    implementation(Dependencies.Compose.runtimeLifecycle)
    implementation(Dependencies.Compose.hiltNavigation)

    implementation(platform(Dependencies.Firebase.bom))
    implementation(Dependencies.Firebase.analytics)
    implementation(Dependencies.Firebase.auth)
    implementation(Dependencies.PlayServices.auth)
    implementation(Dependencies.Firebase.firestore)

    implementation(Dependencies.Hilt.core)
    kapt(Dependencies.Hilt.compiler)

    implementation(Dependencies.Room.room)
    implementation(Dependencies.Room.roomKtx)
    ksp(Dependencies.Room.compiler)

    testImplementation(Dependencies.Testing.jUnit)
    testImplementation(Dependencies.Testing.mockk)
    testImplementation(Dependencies.Testing.truth)
    testImplementation(Dependencies.Testing.coroutines)

    androidTestImplementation(Dependencies.Testing.androidTestJUnitExt)
    androidTestImplementation(Dependencies.Testing.espressoCore)

    androidTestImplementation(platform(Dependencies.Compose.bom))
    androidTestImplementation(Dependencies.Compose.composeTestJUnit4)

    debugImplementation(Dependencies.Compose.uiTooling)
    debugImplementation(Dependencies.Compose.uiTestManifest)
}