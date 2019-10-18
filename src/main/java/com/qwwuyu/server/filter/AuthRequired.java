package com.qwwuyu.server.filter;

import com.qwwuyu.server.configs.Constant;

import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthRequired {
    /** 是否检验 */
    boolean anth() default true;

    /** 检验权限值 */
    int permit() default Constant.PERMIT_BASIC;

    /** 检验过期 */
    boolean expire() default true;

    /** 是否跳转管理员登录 */
    boolean toAdmin() default false;

    /** 检验失败返回的http状态码 */
    int code() default HttpServletResponse.SC_OK;
}
