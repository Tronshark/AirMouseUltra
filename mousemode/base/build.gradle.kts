plugins {
    alias(libs.plugins.module.android.library)
}

android {
    namespace = "pl.tronshark.airmouse.mousemode.base"
}

dependencies {
    implementation(project(":core:ui"))
    implementation(project(":core:common"))

    implementation(libs.androidx.core.ktx)
}