pluginManagement {
    repositories {
        mavenLocal()
        gradlePluginPortal()
        mavenCentral()
        maven(url = "https://mirrors.huaweicloud.com/repository/maven")
    }
}

rootProject.name = "server"
include(":lib", ":app")