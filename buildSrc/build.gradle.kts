import org.gradle.kotlin.dsl.`kotlin-dsl`

plugins {
    `kotlin-dsl`
}

repositories {
    mavenLocal()
    maven(url = "https://mirrors.huaweicloud.com/repository/maven")
}