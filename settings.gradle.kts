pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)
    repositories {
        google()
        mavenCentral()
        // JitPack دیگر نیاز نیست اما برای دیگر کتابخانه‌ها نگه می‌داریم
        maven { url = uri("https://jitpack.io") }
    }
}
rootProject.name = "V2raySTK"
include(":app")
