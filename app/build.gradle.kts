plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.serialization)
    alias(libs.plugins.parcelize)
    alias(libs.plugins.googleServices)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.firebase.crashlytics)

    kotlin("kapt")
}

android {
    namespace = "com.example.foodboxapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.foodboxapp"
        minSdk = 29
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures{
        compose = true
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(platform(libs.firebase.bom))
    implementation(platform(libs.firebase.analytics))


    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.material)
    implementation(libs.material)

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.koin)
    implementation(libs.coil)

    implementation(libs.firebase.auth.ktx)
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.crashlytics)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(libs.composeUi)
    implementation(libs.composeMaterial3)
    implementation(libs.composeUiTooling)
    implementation(libs.lifecycle)
    implementation(libs.viewmodel)
    implementation(libs.activity)
    implementation(libs.navigation)

    implementation(libs.serialization)

    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.composeUiTooling)
    debugImplementation(libs.androidx.ui.test.manifest)

}