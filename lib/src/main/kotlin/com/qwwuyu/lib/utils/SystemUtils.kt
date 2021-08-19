/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.qwwuyu.lib.utils

import java.io.File

//org.apache.commons.lang
/**
 * Helpers for `java.lang.System`.
 *
 * If a system property cannot be read due to security restrictions,
 * the corresponding field in this class will be set to `null`
 * and a message will be written to `System.err`.
 *
 * #ThreadSafe#
 *
 * @author Apache Software Foundation
 * @author Based on code from Avalon Excalibur
 * @author Based on code from Lucene
 * @author [Steve Downey](mailto:sdowney@panix.com)
 * @author Gary Gregory
 * @author Michael Becke
 * @author Tetsuya Kaneuchi
 * @author Rafal Krupinski
 * @author Jason Gritman
 * @since 1.0
 * @version $Id: SystemUtils.java 1056988 2011-01-09 17:58:53Z niallp $
 */
@Suppress("unused")
object SystemUtils {
    private const val JAVA_VERSION_TRIM_SIZE = 3

    /**
     * The prefix String for all Windows OS.
     */
    private const val OS_NAME_WINDOWS_PREFIX = "Windows"
    // System property constants
    // -----------------------------------------------------------------------
    // These MUST be declared first. Other constants depend on this.
    /**
     * The System property key for the user home directory.
     */
    private const val USER_HOME_KEY = "user.home"

    /**
     * The System property key for the user directory.
     */
    private const val USER_DIR_KEY = "user.dir"

    /**
     * The System property key for the Java IO temporary directory.
     */
    private const val JAVA_IO_TMPDIR_KEY = "java.io.tmpdir"

    /**
     * The System property key for the Java home directory.
     */
    private const val JAVA_HOME_KEY = "java.home"

    /**
     * The `awt.toolkit` System Property.
     * Holds a class name, on Windows XP this is `sun.awt.windows.WToolkit`.
     * **On platforms without a GUI, this value is `null`.**
     *
     * Defaults to `null` if the runtime does not have
     * security access to read this property or the property does not exist.
     *
     * This value is initialized when the class is loaded. If [System.setProperty] or
     * [System.setProperties] is called after this class is loaded, the value will
     * be out of sync with that System property.
     *
     * @since 2.1
     */
    @JvmStatic
    val AWT_TOOLKIT = getSystemProperty("awt.toolkit")

    /**
     * The `file.encoding` System Property.
     * File encoding, such as `Cp1252`.
     *
     * Defaults to `null` if the runtime does not have
     * security access to read this property or the property does not exist.
     *
     * This value is initialized when the class is loaded. If [System.setProperty] or
     * [System.setProperties] is called after this class is loaded, the value
     * will be out of sync with that System property.
     *
     * @since 2.0
     * @since Java 1.2
     */
    @JvmStatic
    val FILE_ENCODING = getSystemProperty("file.encoding")

    /**
     * The `file.separator` System Property. File separator (`"/"` on UNIX).
     *
     * Defaults to `null` if the runtime does not have
     * security access to read this property or the property does not exist.
     *
     * This value is initialized when the class is loaded. If [System.setProperty] or
     * [System.setProperties] is called after this class is loaded, the value
     * will be out of sync with that System property.
     *
     * @since Java 1.1
     */
    @JvmStatic
    val FILE_SEPARATOR = getSystemProperty("file.separator")

    /**
     * The `java.awt.fonts` System Property.
     *
     * Defaults to `null` if the runtime does not have
     * security access to read this property or the property does not exist.
     *
     * This value is initialized when the class is loaded. If [System.setProperty] or
     * [System.setProperties] is called after this class is loaded, the value
     * will be out of sync with that System property.
     *
     * @since 2.1
     */
    @JvmStatic
    val JAVA_AWT_FONTS = getSystemProperty("java.awt.fonts")

    /**
     * The `java.awt.graphicsenv` System Property.
     *
     * Defaults to `null` if the runtime does not have
     * security access to read this property or the property does not exist.
     *
     * This value is initialized when the class is loaded. If [System.setProperty] or
     * [System.setProperties] is called after this class is loaded, the value
     * will be out of sync with that System property.
     *
     * @since 2.1
     */
    @JvmStatic
    val JAVA_AWT_GRAPHICSENV = getSystemProperty("java.awt.graphicsenv")

    /**
     * The `java.awt.headless` System Property.
     * The value of this property is the String `"true"` or `"false"`.
     *
     * Defaults to `null` if the runtime does not have
     * security access to read this property or the property does not exist.
     *
     * This value is initialized when the class is loaded. If [System.setProperty] or
     * [System.setProperties] is called after this class is loaded, the value
     * will be out of sync with that System property.
     *
     * @see .isJavaAwtHeadless
     * @since 2.1
     * @since Java 1.4
     */
    @JvmStatic
    val JAVA_AWT_HEADLESS = getSystemProperty("java.awt.headless")

    /**
     * The `java.awt.printerjob` System Property.
     *
     * Defaults to `null` if the runtime does not have
     * security access to read this property or the property does not exist.
     *
     * This value is initialized when the class is loaded. If [System.setProperty] or
     * [System.setProperties] is called after this class is loaded, the value
     * will be out of sync with that System property.
     *
     * @since 2.1
     */
    @JvmStatic
    val JAVA_AWT_PRINTERJOB = getSystemProperty("java.awt.printerjob")

    /**
     * The `java.class.path` System Property. Java class path.
     *
     * Defaults to `null` if the runtime does not have
     * security access to read this property or the property does not exist.
     *
     * This value is initialized when the class is loaded. If [System.setProperty] or
     * [System.setProperties] is called after this class is loaded, the value
     * will be out of sync with that System property.
     *
     * @since Java 1.1
     */
    @JvmStatic
    val JAVA_CLASS_PATH = getSystemProperty("java.class.path")

    /**
     * The `java.class.version` System Property. Java class format version number.
     *
     * Defaults to `null` if the runtime does not have
     * security access to read this property or the property does not exist.
     *
     * This value is initialized when the class is loaded. If [System.setProperty] or
     * [System.setProperties] is called after this class is loaded, the value
     * will be out of sync with that System property.
     *
     * @since Java 1.1
     */
    @JvmStatic
    val JAVA_CLASS_VERSION = getSystemProperty("java.class.version")

    /**
     * The `java.compiler` System Property. Name of JIT compiler to use.
     * First in JDK version 1.2. Not used in Sun JDKs after 1.2.
     *
     * Defaults to `null` if the runtime does not have
     * security access to read this property or the property does not exist.
     *
     * This value is initialized when the class is loaded. If [System.setProperty] or
     * [System.setProperties] is called after this class is loaded, the value
     * will be out of sync with that System property.
     *
     * @since Java 1.2. Not used in Sun versions after 1.2.
     */
    @JvmStatic
    val JAVA_COMPILER = getSystemProperty("java.compiler")

    /**
     * The `java.endorsed.dirs` System Property. Path of endorsed directory or directories.
     *
     * Defaults to `null` if the runtime does not have
     * security access to read this property or the property does not exist.
     *
     * This value is initialized when the class is loaded. If [System.setProperty] or
     * [System.setProperties] is called after this class is loaded, the value
     * will be out of sync with that System property.
     *
     * @since Java 1.4
     */
    @JvmStatic
    val JAVA_ENDORSED_DIRS = getSystemProperty("java.endorsed.dirs")

    /**
     * The `java.ext.dirs` System Property. Path of extension directory or directories.
     *
     * Defaults to `null` if the runtime does not have
     * security access to read this property or the property does not exist.
     *
     * This value is initialized when the class is loaded. If [System.setProperty] or
     * [System.setProperties] is called after this class is loaded, the value
     * will be out of sync with that System property.
     *
     * @since Java 1.3
     */
    @JvmStatic
    val JAVA_EXT_DIRS = getSystemProperty("java.ext.dirs")

    /**
     * The `java.home` System Property. Java installation directory.
     *
     * Defaults to `null` if the runtime does not have
     * security access to read this property or the property does not exist.
     *
     * This value is initialized when the class is loaded. If [System.setProperty] or
     * [System.setProperties] is called after this class is loaded, the value
     * will be out of sync with that System property.
     *
     * @since Java 1.1
     */
    @JvmStatic
    val JAVA_HOME = getSystemProperty(JAVA_HOME_KEY)

    /**
     * The `java.io.tmpdir` System Property. Default temp file path.
     *
     * Defaults to `null` if the runtime does not have
     * security access to read this property or the property does not exist.
     *
     * This value is initialized when the class is loaded. If [System.setProperty] or
     * [System.setProperties] is called after this class is loaded, the value
     * will be out of sync with that System property.
     *
     * @since Java 1.2
     */
    @JvmStatic
    val JAVA_IO_TMPDIR = getSystemProperty(JAVA_IO_TMPDIR_KEY)

    /**
     * The `java.library.path` System Property. List of paths to search when loading libraries.
     *
     * Defaults to `null` if the runtime does not have
     * security access to read this property or the property does not exist.
     *
     * This value is initialized when the class is loaded. If [System.setProperty] or
     * [System.setProperties] is called after this class is loaded, the value
     * will be out of sync with that System property.
     *
     * @since Java 1.2
     */
    @JvmStatic
    val JAVA_LIBRARY_PATH = getSystemProperty("java.library.path")

    /**
     * The `java.runtime.name` System Property. Java Runtime Environment name.
     *
     * Defaults to `null` if the runtime does not have
     * security access to read this property or the property does not exist.
     *
     * This value is initialized when the class is loaded. If [System.setProperty] or
     * [System.setProperties] is called after this class is loaded, the value
     * will be out of sync with that System property.
     *
     * @since 2.0
     * @since Java 1.3
     */
    @JvmStatic
    val JAVA_RUNTIME_NAME = getSystemProperty("java.runtime.name")

    /**
     * The `java.runtime.version` System Property. Java Runtime Environment version.
     *
     * Defaults to `null` if the runtime does not have
     * security access to read this property or the property does not exist.
     *
     * This value is initialized when the class is loaded. If [System.setProperty] or
     * [System.setProperties] is called after this class is loaded, the value
     * will be out of sync with that System property.
     *
     * @since 2.0
     * @since Java 1.3
     */
    @JvmStatic
    val JAVA_RUNTIME_VERSION = getSystemProperty("java.runtime.version")

    /**
     * The `java.specification.name` System Property. Java Runtime Environment specification name.
     *
     * Defaults to `null` if the runtime does not have
     * security access to read this property or the property does not exist.
     *
     * This value is initialized when the class is loaded. If [System.setProperty] or
     * [System.setProperties] is called after this class is loaded, the value
     * will be out of sync with that System property.
     *
     * @since Java 1.2
     */
    @JvmStatic
    val JAVA_SPECIFICATION_NAME = getSystemProperty("java.specification.name")

    /**
     * The `java.specification.vendor` System Property. Java Runtime Environment specification vendor.
     *
     * Defaults to `null` if the runtime does not have
     * security access to read this property or the property does not exist.
     *
     * This value is initialized when the class is loaded. If [System.setProperty] or
     * [System.setProperties] is called after this class is loaded, the value
     * will be out of sync with that System property.
     *
     * @since Java 1.2
     */
    @JvmStatic
    val JAVA_SPECIFICATION_VENDOR = getSystemProperty("java.specification.vendor")

    /**
     * The `java.specification.version` System Property. Java Runtime Environment specification version.
     *
     * Defaults to `null` if the runtime does not have
     * security access to read this property or the property does not exist.
     *
     * This value is initialized when the class is loaded. If [System.setProperty] or
     * [System.setProperties] is called after this class is loaded, the value
     * will be out of sync with that System property.
     *
     * @since Java 1.3
     */
    @JvmStatic
    val JAVA_SPECIFICATION_VERSION = getSystemProperty("java.specification.version")

    /**
     * The `java.util.prefs.PreferencesFactory` System Property. A class name.
     *
     * Defaults to `null` if the runtime does not have
     * security access to read this property or the property does not exist.
     *
     * This value is initialized when the class is loaded. If [System.setProperty] or
     * [System.setProperties] is called after this class is loaded, the value
     * will be out of sync with that System property.
     *
     * @since 2.1
     * @since Java 1.4
     */
    @JvmStatic
    val JAVA_UTIL_PREFS_PREFERENCES_FACTORY = getSystemProperty("java.util.prefs.PreferencesFactory")

    /**
     * The `java.vendor` System Property. Java vendor-specific string.
     *
     * Defaults to `null` if the runtime does not have
     * security access to read this property or the property does not exist.
     *
     * This value is initialized when the class is loaded. If [System.setProperty] or
     * [System.setProperties] is called after this class is loaded, the value
     * will be out of sync with that System property.
     *
     * @since Java 1.1
     */
    @JvmStatic
    val JAVA_VENDOR = getSystemProperty("java.vendor")

    /**
     * The `java.vendor.url` System Property. Java vendor URL.
     *
     * Defaults to `null` if the runtime does not have
     * security access to read this property or the property does not exist.
     *
     * This value is initialized when the class is loaded. If [System.setProperty] or
     * [System.setProperties] is called after this class is loaded, the value
     * will be out of sync with that System property.
     *
     * @since Java 1.1
     */
    @JvmStatic
    val JAVA_VENDOR_URL = getSystemProperty("java.vendor.url")

    /**
     * The `java.version` System Property. Java version number.
     *
     * Defaults to `null` if the runtime does not have
     * security access to read this property or the property does not exist.
     *
     * This value is initialized when the class is loaded. If [System.setProperty] or
     * [System.setProperties] is called after this class is loaded, the value
     * will be out of sync with that System property.
     *
     * @since Java 1.1
     */
    @JvmStatic
    val JAVA_VERSION = getSystemProperty("java.version")

    /**
     * The `java.vm.info` System Property. Java Virtual Machine implementation info.
     *
     * Defaults to `null` if the runtime does not have
     * security access to read this property or the property does not exist.
     *
     * This value is initialized when the class is loaded. If [System.setProperty] or
     * [System.setProperties] is called after this class is loaded, the value
     * will be out of sync with that System property.
     *
     * @since 2.0
     * @since Java 1.2
     */
    @JvmStatic
    val JAVA_VM_INFO = getSystemProperty("java.vm.info")

    /**
     * The `java.vm.name` System Property. Java Virtual Machine implementation name.
     *
     * Defaults to `null` if the runtime does not have
     * security access to read this property or the property does not exist.
     *
     * This value is initialized when the class is loaded. If [System.setProperty] or
     * [System.setProperties] is called after this class is loaded, the value
     * will be out of sync with that System property.
     *
     * @since Java 1.2
     */
    @JvmStatic
    val JAVA_VM_NAME = getSystemProperty("java.vm.name")

    /**
     * The `java.vm.specification.name` System Property. Java Virtual Machine specification name.
     *
     * Defaults to `null` if the runtime does not have
     * security access to read this property or the property does not exist.
     *
     * This value is initialized when the class is loaded. If [System.setProperty] or
     * [System.setProperties] is called after this class is loaded, the value
     * will be out of sync with that System property.
     *
     * @since Java 1.2
     */
    @JvmStatic
    val JAVA_VM_SPECIFICATION_NAME = getSystemProperty("java.vm.specification.name")

    /**
     * The `java.vm.specification.vendor` System Property. Java Virtual Machine specification vendor.
     *
     * Defaults to `null` if the runtime does not have
     * security access to read this property or the property does not exist.
     *
     * This value is initialized when the class is loaded. If [System.setProperty] or
     * [System.setProperties] is called after this class is loaded, the value
     * will be out of sync with that System property.
     *
     * @since Java 1.2
     */
    @JvmStatic
    val JAVA_VM_SPECIFICATION_VENDOR = getSystemProperty("java.vm.specification.vendor")

    /**
     * The `java.vm.specification.version` System Property. Java Virtual Machine specification version.
     *
     * Defaults to `null` if the runtime does not have
     * security access to read this property or the property does not exist.
     *
     * This value is initialized when the class is loaded. If [System.setProperty] or
     * [System.setProperties] is called after this class is loaded, the value
     * will be out of sync with that System property.
     *
     * @since Java 1.2
     */
    @JvmStatic
    val JAVA_VM_SPECIFICATION_VERSION = getSystemProperty("java.vm.specification.version")

    /**
     * The `java.vm.vendor` System Property. Java Virtual Machine implementation vendor.
     *
     * Defaults to `null` if the runtime does not have
     * security access to read this property or the property does not exist.
     *
     * This value is initialized when the class is loaded. If [System.setProperty] or
     * [System.setProperties] is called after this class is loaded, the value
     * will be out of sync with that System property.
     *
     * @since Java 1.2
     */
    @JvmStatic
    val JAVA_VM_VENDOR = getSystemProperty("java.vm.vendor")

    /**
     * The `java.vm.version` System Property. Java Virtual Machine implementation version.
     *
     * Defaults to `null` if the runtime does not have
     * security access to read this property or the property does not exist.
     *
     * This value is initialized when the class is loaded. If [System.setProperty] or
     * [System.setProperties] is called after this class is loaded, the value
     * will be out of sync with that System property.
     *
     * @since Java 1.2
     */
    @JvmStatic
    val JAVA_VM_VERSION = getSystemProperty("java.vm.version")

    /**
     * The `line.separator` System Property. Line separator (`"\n"` on UNIX).
     *
     * Defaults to `null` if the runtime does not have
     * security access to read this property or the property does not exist.
     *
     * This value is initialized when the class is loaded. If [System.setProperty] or
     * [System.setProperties] is called after this class is loaded, the value
     * will be out of sync with that System property.
     *
     * @since Java 1.1
     */
    @JvmField
    val LINE_SEPARATOR = getSystemProperty("line.separator")

    /**
     * The `os.arch` System Property. Operating system architecture.
     *
     * Defaults to `null` if the runtime does not have
     * security access to read this property or the property does not exist.
     *
     * This value is initialized when the class is loaded. If [System.setProperty] or
     * [System.setProperties] is called after this class is loaded, the value
     * will be out of sync with that System property.
     *
     * @since Java 1.1
     */
    @JvmStatic
    val OS_ARCH = getSystemProperty("os.arch")

    /**
     * The `os.name` System Property. Operating system name.
     *
     * Defaults to `null` if the runtime does not have
     * security access to read this property or the property does not exist.
     *
     * This value is initialized when the class is loaded. If [System.setProperty] or
     * [System.setProperties] is called after this class is loaded, the value
     * will be out of sync with that System property.
     *
     * @since Java 1.1
     */
    @JvmStatic
    val OS_NAME = getSystemProperty("os.name")

    /**
     * The `os.version` System Property. Operating system version.
     *
     * Defaults to `null` if the runtime does not have
     * security access to read this property or the property does not exist.
     *
     * This value is initialized when the class is loaded. If [System.setProperty] or
     * [System.setProperties] is called after this class is loaded, the value
     * will be out of sync with that System property.
     *
     * @since Java 1.1
     */
    @JvmStatic
    val OS_VERSION = getSystemProperty("os.version")

    /**
     * The `path.separator` System Property. Path separator (`":"` on UNIX).
     *
     * Defaults to `null` if the runtime does not have
     * security access to read this property or the property does not exist.
     *
     * This value is initialized when the class is loaded. If [System.setProperty] or
     * [System.setProperties] is called after this class is loaded, the value
     * will be out of sync with that System property.
     *
     * @since Java 1.1
     */
    @JvmStatic
    val PATH_SEPARATOR = getSystemProperty("path.separator")

    /**
     * The `user.country` or `user.region` System Property.
     * User's country code, such as `GB`. First in
     * Java version 1.2 as `user.region`. Renamed to `user.country` in 1.4
     *
     * Defaults to `null` if the runtime does not have
     * security access to read this property or the property does not exist.
     *
     * This value is initialized when the class is loaded. If [System.setProperty] or
     * [System.setProperties] is called after this class is loaded, the value
     * will be out of sync with that System property.
     *
     * @since 2.0
     * @since Java 1.2
     */
    @JvmStatic
    val USER_COUNTRY =
        if (getSystemProperty("user.country") == null) getSystemProperty("user.region") else getSystemProperty("user.country")

    /**
     * The `user.dir` System Property. User's current working directory.
     *
     * Defaults to `null` if the runtime does not have
     * security access to read this property or the property does not exist.
     *
     * This value is initialized when the class is loaded. If [System.setProperty] or
     * [System.setProperties] is called after this class is loaded, the value
     * will be out of sync with that System property.
     *
     * @since Java 1.1
     */
    @JvmStatic
    val USER_DIR = getSystemProperty(USER_DIR_KEY)

    /**
     * The `user.home` System Property. User's home directory.
     *
     * Defaults to `null` if the runtime does not have
     * security access to read this property or the property does not exist.
     *
     * This value is initialized when the class is loaded. If [System.setProperty] or
     * [System.setProperties] is called after this class is loaded, the value
     * will be out of sync with that System property.
     *
     * @since Java 1.1
     */
    @JvmStatic
    val USER_HOME = getSystemProperty(USER_HOME_KEY)

    /**
     * The `user.language` System Property. User's language code, such as `"en"`.
     *
     * Defaults to `null` if the runtime does not have
     * security access to read this property or the property does not exist.
     *
     * This value is initialized when the class is loaded. If [System.setProperty] or
     * [System.setProperties] is called after this class is loaded, the value
     * will be out of sync with that System property.
     *
     * @since 2.0
     * @since Java 1.2
     */
    @JvmStatic
    val USER_LANGUAGE = getSystemProperty("user.language")

    /**
     * The `user.name` System Property. User's account name.
     *
     * Defaults to `null` if the runtime does not have
     * security access to read this property or the property does not exist.
     *
     * This value is initialized when the class is loaded. If [System.setProperty] or
     * [System.setProperties] is called after this class is loaded, the value
     * will be out of sync with that System property.
     *
     * @since Java 1.1
     */
    @JvmStatic
    val USER_NAME = getSystemProperty("user.name")

    /**
     * The `user.timezone` System Property. For example: `"America/Los_Angeles"`.
     *
     * Defaults to `null` if the runtime does not have
     * security access to read this property or the property does not exist.
     *
     * This value is initialized when the class is loaded. If [System.setProperty] or
     * [System.setProperties] is called after this class is loaded, the value
     * will be out of sync with that System property.
     *
     * @since 2.1
     */
    @JvmStatic
    val USER_TIMEZONE = getSystemProperty("user.timezone")
    // Java version
    // -----------------------------------------------------------------------
    // This MUST be declared after those above as it depends on the
    // values being set up
    /**
     * Gets the Java version as a `String` trimming leading letters.
     *
     * The field will return `null` if [.JAVA_VERSION] is `null`.
     *
     * @since 2.1
     */
    @JvmStatic
    val JAVA_VERSION_TRIMMED = javaVersionTrimmed
    // Java version values
    // -----------------------------------------------------------------------
    // These MUST be declared after the trim above as they depend on the
    // value being set up
    /**
     *
     * Gets the Java version number as a `float`.
     *
     *
     * Example return values:
     *
     *  * `1.2f` for JDK 1.2
     *  * `1.31f` for JDK 1.3.1
     *
     *
     * @return the version, for example 1.31f for JDK 1.3.1
     */
    /**
     * Gets the Java version as a `float`.
     *
     * Example return values:
     *
     *  * `1.2f` for Java 1.2
     *  * `1.31f` for Java 1.3.1
     *
     *
     * The field will return zero if [.JAVA_VERSION] is `null`.
     *
     * @since 2.0
     */
    @get:Deprecated(
        """Use {@link #JAVA_VERSION_FLOAT} instead.
                  Method will be removed in Commons Lang 3.0."""
    )
    @JvmStatic
    val javaVersion = javaVersionAsFloat

    /**
     * Gets the Java version as an `int`.
     *
     * Example return values:
     *
     *  * `120` for Java 1.2
     *  * `131` for Java 1.3.1
     *
     *
     * The field will return zero if [.JAVA_VERSION] is `null`.
     *
     * @since 2.0
     */
    @JvmStatic
    val JAVA_VERSION_INT = javaVersionAsInt
    // Java version checks
    // -----------------------------------------------------------------------
    // These MUST be declared after those above as they depend on the
    // values being set up
    /**
     * Is `true` if this is Java version 1.1 (also 1.1.x versions).
     *
     * The field will return `false` if [.JAVA_VERSION] is `null`.
     */
    @JvmStatic
    val IS_JAVA_1_1 = getJavaVersionMatches("1.1")

    /**
     * Is `true` if this is Java version 1.2 (also 1.2.x versions).
     *
     * The field will return `false` if [.JAVA_VERSION] is `null`.
     */
    @JvmStatic
    val IS_JAVA_1_2 = getJavaVersionMatches("1.2")

    /**
     * Is `true` if this is Java version 1.3 (also 1.3.x versions).
     *
     * The field will return `false` if [.JAVA_VERSION] is `null`.
     */
    @JvmStatic
    val IS_JAVA_1_3 = getJavaVersionMatches("1.3")

    /**
     * Is `true` if this is Java version 1.4 (also 1.4.x versions).
     *
     * The field will return `false` if [.JAVA_VERSION] is `null`.
     */
    @JvmStatic
    val IS_JAVA_1_4 = getJavaVersionMatches("1.4")

    /**
     * Is `true` if this is Java version 1.5 (also 1.5.x versions).
     *
     * The field will return `false` if [.JAVA_VERSION] is `null`.
     */
    @JvmStatic
    val IS_JAVA_1_5 = getJavaVersionMatches("1.5")

    /**
     * Is `true` if this is Java version 1.6 (also 1.6.x versions).
     *
     * The field will return `false` if [.JAVA_VERSION] is `null`.
     */
    @JvmStatic
    val IS_JAVA_1_6 = getJavaVersionMatches("1.6")

    /**
     * Is `true` if this is Java version 1.7 (also 1.7.x versions).
     *
     * The field will return `false` if [.JAVA_VERSION] is `null`.
     *
     * @since 2.5
     */
    @JvmStatic
    val IS_JAVA_1_7 = getJavaVersionMatches("1.7")
    // Operating system checks
    // -----------------------------------------------------------------------
    // These MUST be declared after those above as they depend on the
    // values being set up
    // OS names from http://www.vamphq.com/os.html
    // Selected ones included - please advise dev@commons.apache.org
    // if you want another added or a mistake corrected
    /**
     * Is `true` if this is AIX.
     *
     * The field will return `false` if `OS_NAME` is `null`.
     *
     * @since 2.0
     */
    @JvmStatic
    val IS_OS_AIX = getOSMatchesName("AIX")

    /**
     * Is `true` if this is HP-UX.
     *
     * The field will return `false` if `OS_NAME` is `null`.
     *
     * @since 2.0
     */
    @JvmStatic
    val IS_OS_HP_UX = getOSMatchesName("HP-UX")

    /**
     * Is `true` if this is Irix.
     *
     * The field will return `false` if `OS_NAME` is `null`.
     *
     * @since 2.0
     */
    @JvmStatic
    val IS_OS_IRIX = getOSMatchesName("Irix")

    /**
     * Is `true` if this is Linux.
     *
     * The field will return `false` if `OS_NAME` is `null`.
     *
     * @since 2.0
     */
    @JvmStatic
    val IS_OS_LINUX = getOSMatchesName("Linux") || getOSMatchesName("LINUX")

    /**
     * Is `true` if this is Mac.
     *
     * The field will return `false` if `OS_NAME` is `null`.
     *
     * @since 2.0
     */
    @JvmStatic
    val IS_OS_MAC = getOSMatchesName("Mac")

    /**
     * Is `true` if this is Mac.
     *
     * The field will return `false` if `OS_NAME` is `null`.
     *
     * @since 2.0
     */
    @JvmStatic
    val IS_OS_MAC_OSX = getOSMatchesName("Mac OS X")

    /**
     * Is `true` if this is OS/2.
     *
     * The field will return `false` if `OS_NAME` is `null`.
     *
     * @since 2.0
     */
    @JvmStatic
    val IS_OS_OS2 = getOSMatchesName("OS/2")

    /**
     * Is `true` if this is Solaris.
     *
     * The field will return `false` if `OS_NAME` is `null`.
     *
     * @since 2.0
     */
    @JvmStatic
    val IS_OS_SOLARIS = getOSMatchesName("Solaris")

    /**
     * Is `true` if this is SunOS.
     *
     * The field will return `false` if `OS_NAME` is `null`.
     *
     * @since 2.0
     */
    @JvmStatic
    val IS_OS_SUN_OS = getOSMatchesName("SunOS")

    /**
     * Is `true` if this is a UNIX like system,
     * as in any of AIX, HP-UX, Irix, Linux, MacOSX, Solaris or SUN OS.
     *
     * The field will return `false` if `OS_NAME` is `null`.
     *
     * @since 2.1
     */
    @JvmStatic
    val IS_OS_UNIX = IS_OS_AIX || IS_OS_HP_UX || IS_OS_IRIX || IS_OS_LINUX ||
            IS_OS_MAC_OSX || IS_OS_SOLARIS || IS_OS_SUN_OS

    /**
     * Is `true` if this is Windows.
     *
     * The field will return `false` if `OS_NAME` is `null`.
     *
     * @since 2.0
     */
    @JvmField
    val IS_OS_WINDOWS = getOSMatchesName(OS_NAME_WINDOWS_PREFIX)

    /**
     * Is `true` if this is Windows 2000.
     *
     * The field will return `false` if `OS_NAME` is `null`.
     *
     * @since 2.0
     */
    @JvmStatic
    val IS_OS_WINDOWS_2000 = getOSMatches(OS_NAME_WINDOWS_PREFIX, "5.0")

    /**
     * Is `true` if this is Windows 95.
     *
     * The field will return `false` if `OS_NAME` is `null`.
     *
     * @since 2.0
     */
    @JvmStatic
    val IS_OS_WINDOWS_95 = getOSMatches(OS_NAME_WINDOWS_PREFIX + " 9", "4.0")
    // Java 1.2 running on Windows98 returns 'Windows 95', hence the above
    /**
     * Is `true` if this is Windows 98.
     *
     * The field will return `false` if `OS_NAME` is `null`.
     *
     * @since 2.0
     */
    @JvmStatic
    val IS_OS_WINDOWS_98 = getOSMatches(OS_NAME_WINDOWS_PREFIX + " 9", "4.1")
    // Java 1.2 running on Windows98 returns 'Windows 95', hence the above
    /**
     * Is `true` if this is Windows ME.
     *
     * The field will return `false` if `OS_NAME` is `null`.
     *
     * @since 2.0
     */
    @JvmStatic
    val IS_OS_WINDOWS_ME = getOSMatches(OS_NAME_WINDOWS_PREFIX, "4.9")
    // Java 1.2 running on WindowsME may return 'Windows 95', hence the above
    /**
     * Is `true` if this is Windows NT.
     *
     * The field will return `false` if `OS_NAME` is `null`.
     *
     * @since 2.0
     */
    @JvmStatic
    val IS_OS_WINDOWS_NT = getOSMatchesName(OS_NAME_WINDOWS_PREFIX + " NT")
    // Windows 2000 returns 'Windows 2000' but may suffer from same Java1.2 problem
    /**
     * Is `true` if this is Windows XP.
     *
     * The field will return `false` if `OS_NAME` is `null`.
     *
     * @since 2.0
     */
    @JvmStatic
    val IS_OS_WINDOWS_XP = getOSMatches(OS_NAME_WINDOWS_PREFIX, "5.1")
    // -----------------------------------------------------------------------
    /**
     * Is `true` if this is Windows Vista.
     *
     * The field will return `false` if `OS_NAME` is `null`.
     *
     * @since 2.4
     */
    @JvmStatic
    val IS_OS_WINDOWS_VISTA = getOSMatches(OS_NAME_WINDOWS_PREFIX, "6.0")

    /**
     * Is `true` if this is Windows 7.
     *
     * The field will return `false` if `OS_NAME` is `null`.
     *
     * @since 2.5
     */
    @JvmStatic
    val IS_OS_WINDOWS_7 = getOSMatches(OS_NAME_WINDOWS_PREFIX, "6.1")

    /**
     * Gets the Java home directory as a `File`.
     *
     * @return a directory
     * @throws SecurityException if a security manager exists and its
     * `checkPropertyAccess` method doesn't allow access to the specified system property.
     * @see System.getProperty
     * @since 2.1
     */
    @JvmStatic
    val javaHome: File
        get() = File(System.getProperty(JAVA_HOME_KEY))

    /**
     * Gets the Java IO temporary directory as a `File`.
     *
     * @return a directory
     * @throws SecurityException if a security manager exists and its
     * `checkPropertyAccess` method doesn't allow access to the specified system
     * property.
     * @see System.getProperty
     * @since 2.1
     */
    @JvmStatic
    val javaIoTmpDir: File
        get() = File(System.getProperty(JAVA_IO_TMPDIR_KEY))

    /**
     * Gets the Java version number as a `float`.
     *
     * Example return values:
     *
     *  * `1.2f` for Java 1.2
     *  * `1.31f` for Java 1.3.1
     *  * `1.6f` for Java 1.6.0_20
     *
     *
     * Patch releases are not reported.
     *
     * @return the version, for example 1.31f for Java 1.3.1
     */
    @JvmStatic
    private val javaVersionAsFloat: Float
         get() = toVersionFloat(toJavaVersionIntArray(JAVA_VERSION, JAVA_VERSION_TRIM_SIZE))

    /**
     * Gets the Java version number as an `int`.
     *
     * Example return values:
     *
     *  * `120` for Java 1.2
     *  * `131` for Java 1.3.1
     *  * `160` for Java 1.6.0_20
     *
     *
     * Patch releases are not reported.
     *
     * @return the version, for example 131 for Java 1.3.1
     */
    @JvmStatic
    private val javaVersionAsInt: Int
         get() = toVersionInt(toJavaVersionIntArray(JAVA_VERSION, JAVA_VERSION_TRIM_SIZE))

    /**
     * Decides if the Java version matches.
     *
     * @param versionPrefix
     * the prefix for the java version
     * @return true if matches, or false if not or can't determine
     */
    @JvmStatic
    private fun getJavaVersionMatches(versionPrefix: String): Boolean {
        return isJavaVersionMatch(JAVA_VERSION_TRIMMED, versionPrefix)
    }

    /**
     * Trims the text of the java version to start with numbers.
     *
     * @return the trimmed java version
     */
    @JvmStatic
    private val javaVersionTrimmed: String?
         get() {
            if (JAVA_VERSION != null) {
                for (i in JAVA_VERSION.indices) {
                    val ch = JAVA_VERSION[i]
                    if (ch in '0'..'9') {
                        return JAVA_VERSION.substring(i)
                    }
                }
            }
            return null
        }

    /**
     * Decides if the operating system matches.
     *
     * @param osNamePrefix
     * the prefix for the os name
     * @param osVersionPrefix
     * the prefix for the version
     * @return true if matches, or false if not or can't determine
     */
    @JvmStatic
    private fun getOSMatches(osNamePrefix: String, osVersionPrefix: String): Boolean {
        return isOSMatch(OS_NAME, OS_VERSION, osNamePrefix, osVersionPrefix)
    }

    /**
     * Decides if the operating system matches.
     *
     * @param osNamePrefix
     * the prefix for the os name
     * @return true if matches, or false if not or can't determine
     */
    @JvmStatic
    private fun getOSMatchesName(osNamePrefix: String): Boolean {
        return isOSNameMatch(OS_NAME, osNamePrefix)
    }
    // -----------------------------------------------------------------------
    /**
     * Gets a System property, defaulting to `null` if the property cannot be read.
     *
     * If a `SecurityException` is caught, the return value is `null` and a message is written to
     * `System.err`.
     *
     * @param property
     * the system property name
     * @return the system property value or `null` if a security problem occurs
     */
    @JvmStatic
    private fun getSystemProperty(property: String): String? {
        return try {
            System.getProperty(property)
        } catch (ex: SecurityException) {
            // we are not allowed to look at this property
            System.err.println(
                "Caught a SecurityException reading the system property '" + property
                        + "'; the SystemUtils property value will default to null."
            )
            null
        }
    }

    /**
     * Gets the user directory as a `File`.
     *
     * @return a directory
     * @throws SecurityException if a security manager exists and its
     * `checkPropertyAccess` method doesn't allow access to the specified system property.
     * @see System.getProperty
     * @since 2.1
     */
    @JvmStatic
    val userDir: File
        get() = File(System.getProperty(USER_DIR_KEY))

    /**
     * Gets the user home directory as a `File`.
     *
     * @return a directory
     * @throws SecurityException if a security manager exists and its
     * `checkPropertyAccess` method doesn't allow access to the specified system property.
     * @see System.getProperty
     * @since 2.1
     */
    @JvmStatic
    val userHome: File
        get() = File(System.getProperty(USER_HOME_KEY))

    /**
     * Returns whether the [.JAVA_AWT_HEADLESS] value is `true`.
     *
     * @return `true` if `JAVA_AWT_HEADLESS` is `"true"`, `false` otherwise.
     *
     * @see .JAVA_AWT_HEADLESS
     *
     * @since 2.1
     * @since Java 1.4
     */
    @JvmStatic
    val isJavaAwtHeadless: Boolean
        get() = if (JAVA_AWT_HEADLESS != null) JAVA_AWT_HEADLESS == java.lang.Boolean.TRUE.toString() else false

    /**
     * Is the Java version at least the requested version.
     *
     * Example input:
     *
     *  * `1.2f` to test for Java 1.2
     *  * `1.31f` to test for Java 1.3.1
     *
     *
     * @param requiredVersion
     * the required version, for example 1.31f
     * @return `true` if the actual version is equal or greater than the required version
     */
    @JvmStatic
    fun isJavaVersionAtLeast(requiredVersion: Float): Boolean {
        return javaVersion >= requiredVersion
    }

    /**
     * Is the Java version at least the requested version.
     *
     * Example input:
     *
     *  * `120` to test for Java 1.2 or greater
     *  * `131` to test for Java 1.3.1 or greater
     *
     *
     * @param requiredVersion
     * the required version, for example 131
     * @return `true` if the actual version is equal or greater than the required version
     * @since 2.0
     */
    @JvmStatic
    fun isJavaVersionAtLeast(requiredVersion: Int): Boolean {
        return JAVA_VERSION_INT >= requiredVersion
    }

    /**
     * Decides if the Java version matches.
     * This method is package private instead of private to support unit test invocation.
     *
     * @param version
     * the actual Java version
     * @param versionPrefix
     * the prefix for the expected Java version
     * @return true if matches, or false if not or can't determine
     */
    @JvmStatic
    fun isJavaVersionMatch(version: String?, versionPrefix: String?): Boolean {
        return version?.startsWith(versionPrefix!!) ?: false
    }

    /**
     * Decides if the operating system matches.
     * This method is package private instead of private to support unit test invocation.
     *
     * @param osName
     * the actual OS name
     * @param osVersion
     * the actual OS version
     * @param osNamePrefix
     * the prefix for the expected OS name
     * @param osVersionPrefix
     * the prefix for the expected OS version
     * @return true if matches, or false if not or can't determine
     */
    @JvmStatic
    fun isOSMatch(osName: String?, osVersion: String?, osNamePrefix: String?, osVersionPrefix: String?): Boolean {
        return if (osName == null || osVersion == null) {
            false
        } else osName.startsWith(osNamePrefix!!) && osVersion.startsWith(osVersionPrefix!!)
    }

    /**
     * Decides if the operating system matches.
     * This method is package private instead of private to support unit test invocation.
     *
     * @param osName
     * the actual OS name
     * @param osNamePrefix
     * the prefix for the expected OS name
     * @return true if matches, or false if not or can't determine
     */
    @JvmStatic
    fun isOSNameMatch(osName: String?, osNamePrefix: String?): Boolean {
        return osName?.startsWith(osNamePrefix!!) ?: false
    }

    /**
     * Converts the given Java version string to a `float`.
     *
     * Example return values:
     *
     *  * `1.2f` for Java 1.2
     *  * `1.31f` for Java 1.3.1
     *  * `1.6f` for Java 1.6.0_20
     *
     *
     * Patch releases are not reported.
     * This method is package private instead of private to support unit test invocation.
     *
     * @param version The string version
     * @return the version, for example 1.31f for Java 1.3.1
     */
    @JvmStatic
    fun toJavaVersionFloat(version: String?): Float {
        return toVersionFloat(toJavaVersionIntArray(version, JAVA_VERSION_TRIM_SIZE))
    }

    /**
     * Converts the given Java version string to an `int`.
     *
     * Example return values:
     *
     *  * `120` for Java 1.2
     *  * `131` for Java 1.3.1
     *  * `160` for Java 1.6.0_20
     *
     *
     * Patch releases are not reported.
     * This method is package private instead of private to support unit test invocation.
     *
     * @param version The string version
     * @return the version, for example 131 for Java 1.3.1
     */
    @JvmStatic
    fun toJavaVersionInt(version: String?): Int {
        return toVersionInt(toJavaVersionIntArray(version, JAVA_VERSION_TRIM_SIZE))
    }

    /**
     * Converts the given Java version string to an `int[]` of maximum size `3`.
     *
     * Example return values:
     *
     *  * `[1, 2, 0]` for Java 1.2
     *  * `[1, 3, 1]` for Java 1.3.1
     *  * `[1, 5, 0]` for Java 1.5.0_21
     *
     * This method is package private instead of private to support unit test invocation.
     *
     * @param version The string version
     * @return the version, for example [1, 5, 0] for Java 1.5.0_21
     */
    @JvmStatic
    fun toJavaVersionIntArray(version: String?): IntArray {
        return toJavaVersionIntArray(version, Int.MAX_VALUE)
    }

    /**
     * Converts the given Java version string to an `int[]` of maximum size `limit`.
     *
     * Example return values:
     *
     *  * `[1, 2, 0]` for Java 1.2
     *  * `[1, 3, 1]` for Java 1.3.1
     *  * `[1, 5, 0, 21]` for Java 1.5.0_21
     *
     *
     * @param version The string version
     * @param limit version limit
     * @return the version, for example [1, 5, 0, 21] for Java 1.5.0_21
     */
    @JvmStatic
    private fun toJavaVersionIntArray(version: String?, limit: Int): IntArray {
        if (version == null) {
            return intArrayOf()
            //            return ArrayUtils.EMPTY_INT_ARRAY;
        }
        //        String[] strings = StringUtils.split(version, "._- ");
        val strings = version.split("._- ").toTypedArray()
        var ints = IntArray(Math.min(limit, strings.size))
        var j = 0
        var i = 0
        while (i < strings.size && j < limit) {
            val s = strings[i]
            if (s.length > 0) {
                try {
                    ints[j] = s.toInt()
                    j++
                } catch (e: Exception) {
                }
            }
            i++
        }
        if (ints.size > j) {
            val newInts = IntArray(j)
            System.arraycopy(ints, 0, newInts, 0, j)
            ints = newInts
        }
        return ints
    }

    /**
     * Converts given the Java version array to a `float`.
     *
     * Example return values:
     *
     *  * `1.2f` for Java 1.2
     *  * `1.31f` for Java 1.3.1
     *  * `1.6f` for Java 1.6.0_20
     *
     *
     * Patch releases are not reported.
     *
     * @param javaVersions The version numbers
     * @return the version, for example 1.31f for Java 1.3.1
     */
    @JvmStatic
    private fun toVersionFloat(javaVersions: IntArray?): Float {
        if (javaVersions == null || javaVersions.size == 0) {
            return 0f
        }
        if (javaVersions.size == 1) {
            return javaVersions[0].toFloat()
        }
        val builder = StringBuffer()
        builder.append(javaVersions[0])
        builder.append('.')
        for (i in 1 until javaVersions.size) {
            builder.append(javaVersions[i])
        }
        return try {
            builder.toString().toFloat()
        } catch (ex: Exception) {
            0f
        }
    }

    /**
     * Converts given the Java version array to an `int`.
     *
     * Example return values:
     *
     *  * `120` for Java 1.2
     *  * `131` for Java 1.3.1
     *  * `160` for Java 1.6.0_20
     *
     *
     * Patch releases are not reported.
     *
     * @param javaVersions The version numbers
     * @return the version, for example 1.31f for Java 1.3.1
     */
    @JvmStatic
    private fun toVersionInt(javaVersions: IntArray?): Int {
        if (javaVersions == null) {
            return 0
        }
        var intVersion = 0
        val len = javaVersions.size
        if (len >= 1) {
            intVersion = javaVersions[0] * 100
        }
        if (len >= 2) {
            intVersion += javaVersions[1] * 10
        }
        if (len >= 3) {
            intVersion += javaVersions[2]
        }
        return intVersion
    }
}