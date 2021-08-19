package com.qwwuyu.gs

import com.qwwuyu.gs.configs.Constant
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.context.ServletContextAware
import java.util.*
import javax.servlet.ServletContext


@SpringBootApplication
class GsApplication : ServletContextAware {
    companion object {
        lateinit var context: ServletContext
    }

    override fun setServletContext(servletContext: ServletContext) {
        context = servletContext
    }
}

fun main(args: Array<String>) {
    TimeZone.setDefault(TimeZone.getTimeZone("Asia/Shanghai"))
    // log4j2.yml
    System.setProperty("log4j2Path", Constant.LOG_PATH)
    runApplication<GsApplication>(*args)
}
