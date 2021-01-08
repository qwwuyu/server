@file:Suppress("ObjectPropertyName", "ObjectPropertyName", "unused")

import java.io.File
import java.io.FileInputStream
import java.util.*

private val properties: Properties = Properties().apply {
    val propertyFile = File("gradle.properties")
    if (propertyFile.exists()) {
        FileInputStream(propertyFile).use {
            load(it)
        }
    }
}

private fun getPropertyByName(key: String, defValue: String?): String? {
    var value = properties.getProperty(key)
    if (value == null) value = System.getenv(key)
    return value ?: defValue
}

object Apps {
    private const val major = 2
    private const val minor = 0
    private const val revision = 0

    const val group = "com.qwwuyu"
    const val version = "${major}.${minor}.${revision}"
}

object Versions {
    const val kotlin = "1.4.21"
    const val coroutines = "1.4.1"

    const val rxjava3 = "3.0.7"
    const val okhttp3 = "3.14.9"
    const val retrofit = "2.9.0"
}

fun kotlinx(id: String, version: String) = "org.jetbrains.kotlinx:kotlinx-$id:$version"

object Libs {
    val kotlinx_coroutines_core = kotlinx("coroutines-core", Versions.coroutines)
    const val junit = "junit:junit:4.13.1"
}
