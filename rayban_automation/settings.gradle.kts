pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        // Add Meta specific repo if known, otherwise assume local or standard
        maven { url = uri("https://maven.pkg.github.com/facebook/meta-wearables-dat-android") } // speculative
    }
}

rootProject.name = "RayBanAutomation"
include(":app")
