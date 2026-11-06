plugins {
    alias(libs.plugins.module.android.library)
}

android {
    namespace = "pl.tronshark.airmouse.mousemode.basecursor"
}

dependencies {
    implementation(project(":core:ui"))
    implementation(project(":core:common"))
	implementation(project(":mousemode:base"))

    implementation(platform(libs.firebase.bom))
    implementation(libs.bundles.firebase)

    implementation(libs.androidx.core.ktx)
}