plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)

    // Add Plugins
    alias(libs.plugins.dagger.hilt.android)
    alias(libs.plugins.devtools.ksp)
}

android {
    namespace = "org.delcom.pam_p4_ifs23031"
    compileSdk = 36

    defaultConfig {
        applicationId = "org.delcom.pam_p4_ifs23031"
        minSdk = 29
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // Menggunakan port 8080 dengan HTTPS sesuai konfigurasi server Anda
        buildConfigField("String", "BASE_URL_PLANT", "\"https://pam-2026-p4-ifs23031-be.tianpael.fun:8080/\"")
        buildConfigField("String", "BASE_URL_GENREMUSIK", "\"https://pam-2026-p4-ifs23031-be.tianpael.fun:8080/\"")
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.okhttp3.logging.interceptor)
    implementation(libs.retrofit2.converter.gson)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)


    // Add Library
    // ================================================
    // > Google Font
    implementation(libs.androidx.ui.text.google.fonts)
    // > Navhost
    implementation(libs.androidx.navigation.compose)
    // > Icon
    implementation(libs.androidx.material.icons.extended)

    // > Kotlin serialization
    implementation(libs.kotlinx.serialization.json)
    // > Coil
    implementation(libs.coil.compose)
    // > Retrofit
    implementation(libs.retrofit2)
    implementation(libs.retrofit2.kotlinx.serialization.converter)
    // > Okhttp3
    implementation(libs.okhttp3)
    // > Dagger
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    // ================================================
}