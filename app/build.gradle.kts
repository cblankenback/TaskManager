plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id ("kotlin-kapt") // For annotation processing
    //id ("dagger.hilt.android.plugin") // For Hilt
}

android {
    namespace = "com.cst3115.enterprise.taskmanager"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.cst3115.enterprise.taskmanager"
        minSdk = 26
        targetSdk = 34
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
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
    //implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation.runtime.ktx)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.play.services.location)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

   // implementation (libs.material3)
    // If not included yet
         implementation (libs.androidx.material)
   // implementation (libs.androidx.runtime)
    implementation ("androidx.compose.material3:material3:1.4.0-alpha04")

    // Retrofit for network calls
  //  implementation (libs.retrofit)

    // Gson converter for JSON parsing
   // implementation (libs.converter.gson)

    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")

    implementation ("com.squareup.okhttp3:logging-interceptor:4.9.3")
    implementation ("io.coil-kt:coil-compose:2.7.0")

    // Jetpack Security for EncryptedSharedPreferences
    implementation ("androidx.security:security-crypto:1.1.0-alpha05")
    implementation("com.google.accompanist:accompanist-permissions:0.34.0")
    // Optional: For DataStore with Encryption (Alternative to EncryptedSharedPreferences)
    implementation ("androidx.datastore:datastore-preferences:1.0.0")
    implementation ("androidx.datastore:datastore-preferences-core:1.0.0")

    implementation ("com.google.android.gms:play-services-location:21.0.1")


    implementation ("androidx.room:room-runtime:2.6.1")
    kapt ("androidx.room:room-compiler:2.6.1") // For annotation processing
    implementation ("androidx.room:room-ktx:2.6.1") // Optional - Kotlin extensions
}