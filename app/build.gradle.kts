plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.nickdev.mensajesapsti"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.nickdev.mensajesapsti"
        minSdk = 24
        targetSdk = 36
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
    buildFeatures{
        viewBinding = true
    }
    buildTypes.getByName("release") {
        isMinifyEnabled = false
        proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
    }
}

dependencies {
    implementation (libs.circleimageview)

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.firebase.crashlytics.buildtools)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)


    implementation(libs.cardview)
    implementation(libs.recyclerview)


    implementation("com.squareup.retrofit2:retrofit:3.0.0")

    // Gson (Conversor JSON para Retrofit)
    implementation("com.squareup.retrofit2:converter-gson:3.0.0")

    // OkHttp (Para interceptores, como el de Logging y Auth)
    implementation("com.squareup.okhttp3:logging-interceptor:5.3.1")

    // EncryptedSharedPreferences (Para guardar el token de forma segura)
    implementation("androidx.security:security-crypto:1.1.0")
}