plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.vanniktech.maven.publish)
}

android {
    namespace = "io.github.iabanoubsamir.dialogkit"
    compileSdk = 36

    defaultConfig {
        minSdk = 24
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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

    buildFeatures {
        compose = true
    }
}

mavenPublishing {
    publishToMavenCentral()
    signAllPublications()

    coordinates("io.github.iabanoubsamir", "compose-dialog-kit", "1.0.0")

    pom {
        name = "compose-dialog-kit"
        description = "A collection of beautiful, ready-made Jetpack Compose dialog composables."
        url = "https://github.com/iabanoubsamir/compose-dialog-kit"
        licenses {
            license {
                name = "The Apache License, Version 2.0"
                url = "http://www.apache.org/licenses/LICENSE-2.0.txt"
            }
        }
        developers {
            developer {
                id = "iabanoubsamir"
                name = "Abanoub Samir"
                url = "https://github.com/iabanoubsamir"
            }
        }
        scm {
            url = "https://github.com/iabanoubsamir/compose-dialog-kit"
            connection = "scm:git:git://github.com/iabanoubsamir/compose-dialog-kit.git"
            developerConnection = "scm:git:ssh://git@github.com/iabanoubsamir/compose-dialog-kit.git"
        }
    }
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material.icons.extended)
    debugImplementation(libs.androidx.compose.ui.tooling)
}
