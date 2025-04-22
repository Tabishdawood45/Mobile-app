plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("kotlin-kapt")
    id("com.google.gms.google-services")
//    id("com.android.library")


}

android {
    namespace = "com.example.mobile_app"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.mobile_app"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
//    compileOptions {
//        sourceCompatibility = JavaVersion.VERSION_11
//        targetCompatibility = JavaVersion.VERSION_11
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
//        coreLibraryDesugaringEnabled = true
    }
//    kotlinOptions {
//        jvmTarget = "11"
//    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.espresso.core)
//    implementation(libs.androidx.animation.core.lint)
    implementation(libs.androidx.runtime.lint)
//    implementation(libs.firebase.dataconnect)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    kapt ("androidx.room:room-compiler:2.5.0")
    implementation ("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.2")
    implementation ("androidx.room:room-runtime:2.6.1")  // Room runtime
    implementation ("androidx.room:room-ktx:2.6.1" )
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0")// Room KTX for coroutines
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.0")
    implementation ("androidx.navigation:navigation-compose:2.5.1")
    implementation (libs.view)
//    implementation(libs.firebase.auth)
    implementation ("androidx.compose.material:material-icons-extended:1.6.1")
    implementation("androidx.compose.animation:animation")
    // Shadow and elevation helpers (included in Material3)
    // For enhanced previews and tooling (optional but useful for UI design)
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-tooling-preview")
    // Optional: Accompanist for more beautiful UI helpers like pager, swipe, etc.
    implementation("com.google.accompanist:accompanist-systemuicontroller:0.30.1")

    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.2")
    implementation("androidx.compose.runtime:runtime-livedata:1.5.0")

    implementation("io.coil-kt:coil-compose:2.1.0")
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.0.4")
//    implementation(libs.firebase.firestore.ktx)
    implementation(platform("com.google.firebase:firebase-bom:32.7.0"))
//    implementation("com.google.firebase:firebase-firestore-ktx")
//    implementation(platform("com.google.firebase:firebase-bom:32.7.0")) // or latest stable

// Use these without versions:
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.firebase:firebase-firestore-ktx")
    implementation("com.google.firebase:firebase-database-ktx")
//    implementation("com.google.firebase:firebase-X-ktx")
    implementation("com.google.firebase:firebase-analytics-ktx")

    implementation ("androidx.compose.animation:animation:1.4.0")
    implementation ("com.airbnb.android:lottie-compose:5.0.0")
    implementation ("androidx.compose.material3:material3:1.0.0")
    implementation ("androidx.compose.foundation:foundation:1.3.0")

}