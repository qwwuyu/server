package com.qwwuyu.gs

import com.qwwuyu.gs.configs.Constant
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class GsApplication

fun main(args: Array<String>) {
    // log4j2.yml
    System.setProperty("log4j2Path", Constant.LOG_PATH)
    runApplication<GsApplication>(*args)
}
