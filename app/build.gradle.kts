plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.apollographql.apollo3") version "3.8.2"
    id("com.google.dagger.hilt.android")
    kotlin("kapt")
}

android {
    namespace = "com.calvin.trawlbenstechtest"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.calvin.trawlbenstechtest"
        minSdk = 21
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
}

dependencies {

    implementation(Dependencies.coreKtx)
    implementation(Dependencies.appCompat)
    implementation(Dependencies.material)
    implementation(Dependencies.constraintLayout)
    implementation(Dependencies.kotlinCoroutines)
    implementation(Dependencies.apolloClient)
    implementation(Dependencies.okhttpClient)
    implementation(Dependencies.navigationFragment)
    implementation(Dependencies.navigationUiKtx)
    implementation(Dependencies.paging)
    implementation(Dependencies.glide)
    implementation(Dependencies.hilt)
    kapt(Dependencies.kaptHilt)
    implementation(Dependencies.roomKtx)
    implementation(Dependencies.roomRuntime)
    kapt(Dependencies.roomCompiler)
    annotationProcessor(Dependencies.roomCompiler)
    testImplementation(Dependencies.junit)
    androidTestImplementation(Dependencies.extJunit)
    androidTestImplementation(Dependencies.espresso)
    
}

apollo {
    service("service") {
        packageName.set("com.calvin")
    }
}

kapt {
    correctErrorTypes = true
}