// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    // main
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    alias(libs.plugins.compose.compiler) apply false
    id("com.google.devtools.ksp") version "2.1.20-1.0.32" apply false
    // firebase
    id("com.google.gms.google-services") version "4.4.2" apply false
    id("com.google.firebase.crashlytics") version "3.0.3" apply false
    id("com.google.firebase.firebase-perf") version "1.4.2" apply false
}