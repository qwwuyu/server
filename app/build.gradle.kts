plugins {
    id("org.springframework.boot")
    kotlin("jvm")
    kotlin("plugin.spring")
}

dependencies {
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation(Libs.junit)

    implementation(project(":lib"))
    implementation(kotlin("reflect"))
    implementation(kotlin("stdlib-jdk8"))
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-log4j2")
    implementation("org.springframework.boot:spring-boot-starter-websocket")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("commons-fileupload:commons-fileupload:1.4")
    implementation("org.mybatis.spring.boot:mybatis-spring-boot-starter:1.3.0")
    implementation("mysql:mysql-connector-java:8.0.22")

    implementation(Libs.kotlinx_coroutines_core)

}