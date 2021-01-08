plugins {
    kotlin("jvm")
}

dependencies {
    testImplementation(Libs.junit)

    compileOnly("org.apache.tomcat.embed:tomcat-embed-core:9.0.27")
    implementation(kotlin("stdlib-jdk8"))
    implementation(Libs.kotlinx_coroutines_core)
    api("com.google.code.gson:gson:2.8.6")
    api("commons-codec:commons-codec:1.9")
    api("commons-io:commons-io:2.4")
}