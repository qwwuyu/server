import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    repositories {
        maven(url = "https://mirrors.huaweicloud.com/repository/maven")
        mavenLocal()
        jcenter()
        mavenCentral()
    }

    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-allopen:${Versions.kotlin}")
        //classpath("org.jetbrains.kotlin:kotlin-noarg:${Versions.kotlin}")
    }
}

plugins {
    id("org.springframework.boot") version "2.2.0.RELEASE" apply false
    id("io.spring.dependency-management") version "1.0.10.RELEASE" apply false
    kotlin("jvm") version Versions.kotlin apply false
    kotlin("plugin.spring") version Versions.kotlin apply false
}

allprojects {
    group = Apps.group
    version = Apps.version

    tasks.withType<JavaCompile> {
        sourceCompatibility = "1.8"
        targetCompatibility = "1.8"
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = "1.8"
        }
    }
}

subprojects {
    repositories {
        maven(url = "https://mirrors.huaweicloud.com/repository/maven")
        mavenCentral()
    }

    apply {
        plugin("io.spring.dependency-management")
    }

    configurations.all {
        exclude("org.springframework.boot", "spring-boot-starter-logging")
        exclude(null, "logback-classic")
        exclude(null, "logback-core")
    }
}