pluginManagement {
    repositories {
        mavenLocal()
        gradlePluginPortal()
        mavenCentral()
        jcenter()
    }
}

rootProject.name = "server"
include(":lib", ":app")