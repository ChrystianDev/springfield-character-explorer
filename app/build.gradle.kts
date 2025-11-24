plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.jetbrains.kotlin.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
    alias(libs.plugins.google.services)
}


android {
    val psVersionCode = 3
    val psVersionName = "1.0.2"
    val psVersionNameSuffix = "-demo"

    namespace = "com.mobilitydevai.sce"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.mobilitydevai.sce"
        minSdk = 24
        targetSdk = 36
        versionCode = psVersionCode
        versionName = psVersionName

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    // Specifies one flavor dimension.
    flavorDimensions += "version"
    productFlavors {
        create("prod") {
            dimension = "version"
            applicationIdSuffix = ".prod"
            versionNameSuffix = "$psVersionNameSuffix.prod"
            versionName = psVersionName
            buildConfigField(
                type = "String",
                name = "BASE_URL",
                value = "\"https://pro.thesimpsonsapi.com/api/\""
            )
        }
        create("qa") {
            dimension = "version"
            applicationIdSuffix = ".qa"
            versionNameSuffix = "$psVersionNameSuffix.qa"
            versionName = psVersionName
            buildConfigField(
                type = "String",
                name = "BASE_URL",
                value = "\"https://qa.thesimpsonsapi.com/api/\""
            )

        }
        create("dev") {
            // Assigns this product flavor to the "version" flavor dimension.
            // If you are using only one dimension, this property is optional,
            // and the plugin automatically assigns all the module's flavors to
            // that dimension.
            dimension = "version"
            applicationIdSuffix = ""
            versionNameSuffix = psVersionNameSuffix
            versionName = psVersionName
            buildConfigField(
                type = "String",
                name = "BASE_URL",
                value = "\"https://thesimpsonsapi.com/api/\""
            )
        }
    }

    lint {
        checkReleaseBuilds = false
        abortOnError = false
    }

    buildTypes {
        release {
//        getByName("release") {
            applicationIdSuffix = ""
            versionNameSuffix = ""
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }

        debug {
//        getByName("debug") {
            applicationIdSuffix = ""
            versionNameSuffix = "-debug"
            isDebuggable = true
            enableUnitTestCoverage = true
            enableAndroidTestCoverage = true
            manifestPlaceholders["hostName"] = "com.mobilitydevai.sce"
        }

        /**
         * The `initWith` property lets you copy configurations from other build types,
         * then configure only the settings you want to change. This one copies the debug build
         * type, and then changes the manifest placeholder and application ID.
         */
        create("staging") {
            initWith(getByName("debug"))
            manifestPlaceholders["hostName"] = "com.mobilitydevai.sce"
            applicationIdSuffix = ".debugStaging"
        }
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }


    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }


    kotlin {
        jvmToolchain {
            languageVersion.set(JavaLanguageVersion.of(11))
        }
    }
//    kotlinOptions {
//        jvmTarget = "11"
//    }

    buildFeatures {
        compose = true
    }

}

dependencies {
    // Core
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material3.adaptive.navigation.suite)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.androidx.core.common)
    implementation(libs.androidx.core.runtime)

    // Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.auth)
    implementation(libs.play.services.auth)

    // Navigation
    implementation(libs.androidx.navigation.compose)

    // DI: Dependencies
    implementation(libs.google.dagger.hilt.android)
    implementation(libs.hilt.navigation)
    ksp(libs.google.dagger.hilt.compiler)

    // API-Rest
    implementation(libs.squareup.retrofit)
    implementation(libs.squareup.retrofit.converter.gson)

    // Media
    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)
    implementation(libs.lottie.compose)

    // Local DB
    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)

    // Theme
    implementation(libs.accompanist.systemuicontroller)

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    testImplementation(libs.mockk)
    androidTestImplementation(libs.androidx.core.testing)
    testImplementation(libs.kotlinx.coroutines.test)

    // Others
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}

apply(from = "jacoco.gradle.kts")