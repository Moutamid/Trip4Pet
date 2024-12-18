plugins {
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
    alias(libs.plugins.androidApplication)
}

android {
    namespace = "com.moutamid.trip4pet"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.moutamid.trip4pet"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0.0"
        setProperty("archivesBaseName", "Trip4Pet-$versionName")
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures { viewBinding = true }

    buildTypes {
        release {
//            isDebuggable = true
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation(libs.gson)
    implementation("com.github.smarteist:Android-Image-Slider:1.4.0")
    implementation(libs.simpleratingbar)
    implementation(libs.translateapi)

    implementation(libs.play.services.maps)
    implementation(libs.play.services.location)

    implementation(libs.glide)
    implementation(libs.imagepicker)

    implementation(platform("com.google.firebase:firebase-bom:33.1.2"))
    implementation("com.google.firebase:firebase-database")
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-crashlytics")
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-storage")

    implementation(libs.volley)

    implementation(libs.play.services.auth)

    implementation(libs.jsoup)


    implementation(libs.billing)
    implementation(libs.preference)
    implementation(libs.library)

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}