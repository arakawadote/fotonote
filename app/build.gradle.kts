import java.util.Properties
import org.gradle.api.GradleException

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
}

val keystorePropertiesFile = rootProject.file("keystore.properties")
val keystoreProperties = Properties().apply {
    if (keystorePropertiesFile.exists()) {
        keystorePropertiesFile.inputStream().use { load(it) }
    }
}
val hasReleaseKeystore = listOf(
    "storeFile",
    "storePassword",
    "keyAlias",
    "keyPassword"
).all { !keystoreProperties.getProperty(it).isNullOrBlank() }

tasks.register("validateReleaseKeystore") {
    doLast {
        if (!hasReleaseKeystore) {
            throw GradleException(
                "Release signing is not configured. Create keystore.properties from keystore.properties.example before building a release AAB."
            )
        }
    }
}

tasks.matching { it.name == "preReleaseBuild" }.configureEach {
    dependsOn("validateReleaseKeystore")
}

android {
    namespace = "com.arakawadote.fotonote"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.arakawadote.fotonote"
        minSdk = 26
        targetSdk = 35
        versionCode = 3
        versionName = "0.2.1"
    }

    signingConfigs {
        if (hasReleaseKeystore) {
            create("release") {
                storeFile = rootProject.file(keystoreProperties.getProperty("storeFile"))
                storePassword = keystoreProperties.getProperty("storePassword")
                keyAlias = keystoreProperties.getProperty("keyAlias")
                keyPassword = keystoreProperties.getProperty("keyPassword")
            }
        }
    }

    buildTypes {
        debug {
            manifestPlaceholders["adMobApplicationId"] =
                "ca-app-pub-3940256099942544~3347511713"
            buildConfigField(
                "String",
                "BANNER_AD_UNIT_ID",
                "\"ca-app-pub-3940256099942544/9214589741\""
            )
        }

        release {
            manifestPlaceholders["adMobApplicationId"] =
                "ca-app-pub-9719679577337445~2539005797"
            buildConfigField(
                "String",
                "BANNER_AD_UNIT_ID",
                "\"ca-app-pub-9719679577337445/7848996628\""
            )
            if (hasReleaseKeystore) {
                signingConfig = signingConfigs.getByName("release")
            }
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

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        buildConfig = true
        compose = true
    }
}

dependencies {
    val composeBom = platform("androidx.compose:compose-bom:2024.12.01")
    implementation(composeBom)
    androidTestImplementation(composeBom)

    implementation("androidx.activity:activity-compose:1.9.3")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.8.7")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.7")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.7")
    implementation("androidx.exifinterface:exifinterface:1.3.7")
    implementation("com.google.android.gms:play-services-ads:25.2.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.9.0")

    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}
