package com.qwwuyu.gs.configs

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties("env")
data class EnvProperties(
    val privateKey: String,
    val publicKey: String
)
