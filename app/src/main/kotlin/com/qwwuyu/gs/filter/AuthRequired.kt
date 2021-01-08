package com.qwwuyu.gs.filter

import com.qwwuyu.gs.configs.Constant
import javax.servlet.http.HttpServletResponse

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER, AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.CLASS)
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
annotation class AuthRequired(
        /** 是否检验  */
        val anth: Boolean = true,
        /** 检验权限值  */
        val permit: Int = Constant.PERMIT_BASIC,
        /** 检验过期  */
        val expire: Boolean = true,
        /** 是否跳转管理员登录  */
        val toAdmin: Boolean = false,
        /** 检验失败返回的http状态码  */
        val code: Int = HttpServletResponse.SC_OK
)