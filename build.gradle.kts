// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    // main
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.version.catalog.update)
    alias(libs.plugins.kotlin.ksp) apply false
    // firebase
    alias(libs.plugins.google.services) apply false
    alias(libs.plugins.firebase.crashlytics.plugin) apply false
    alias(libs.plugins.firebase.perf.plugin) apply false
}