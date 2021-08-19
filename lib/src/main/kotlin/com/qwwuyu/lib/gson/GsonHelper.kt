package com.qwwuyu.lib.gson

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.util.*

@Suppress("unused")
object GsonHelper {
    private var gson: Gson? = null

    @JvmStatic
    fun init(gsonBuilder: GsonBuilder) {
        if (gson == null) {
            synchronized(GsonHelper::class.java) {
                if (gson == null) {
                    gson = gsonBuilder
                        .registerTypeAdapter(Ignore::class.java, IgnoreTypeAdapter())
                        .registerTypeAdapter(Date::class.java, DateTypeAdapter())
                        //.registerTypeAdapter(float.class, new FloatTypeAdapter(0f))//处理float返回空字符串
                        //.registerTypeAdapter(Float.class, new FloatTypeAdapter(null))//处理Float返回空字符串
                        //.registerTypeAdapter(JsonDeserializer.class, deserializer)
                        //.addSerializationExclusionStrategy(new Exclusion())
                        //.addDeserializationExclusionStrategy(new Exclusion())
                        .create()
                }
            }
        }
    }

    @JvmStatic
    fun getGson(): Gson {
        if (gson == null) {
            init(GsonBuilder())
        }
        return gson!!
    }

    /**
     * 使用gson解析json,异常则返回空
     */
    @JvmStatic
    fun <T> fromJson(result: String, clazz: Class<T>): T? {
        return try {
            getGson().fromJson(result, clazz)
        } catch (e: Exception) {
            null
        }
    }

    @JvmStatic
    fun <T> fromType(result: String, type: Type): T? {
        return try {
            getGson().fromJson(result, type)
        } catch (e: Exception) {
            null
        }
    }

    /**
     * 将对象转化为json
     */
    @JvmStatic
    fun toJson(obj: Any): String {
        return getGson().toJson(obj)
    }

    /**
     * 获取类泛型第一个参数
     */
    @JvmStatic
    fun getActualTypeArgument0(subclass: Class<*>): Type? {
        val superclass = subclass.genericSuperclass
        if (superclass is ParameterizedType) {
            val types = superclass.actualTypeArguments
            return if (types.isNotEmpty()) types[0] else null
        }
        return null
    }

    /**
     * 判断type是否是String类型
     */
    @JvmStatic
    fun isTypeString(type: Type): Boolean {
        return if (type is Class<*>) {
            String::class.java.isAssignableFrom(type)
        } else false
    }

    /**
     * 判断type是否继承List
     */
    @JvmStatic
    fun isTypeList(type: Type?): Boolean {
        if (type is ParameterizedType) {
            val rawType = type.rawType
            if (rawType is Class<*>) {
                return MutableList::class.java.isAssignableFrom(rawType)
            }
        }
        return false
    }

    /**
     * 判断type是否是Ignore类型
     */
    @JvmStatic
    fun isTypeIgnore(type: Type?): Boolean {
        return if (type is Class<*>) {
            Ignore::class.java.isAssignableFrom(type as Class<*>?)
        } else false
    }

    /**
     * 判断type是否是Unit类型
     */
    @JvmStatic
    fun isTypeUnit(type: Type?): Boolean {
        return if (type is Class<*>) {
            Unit::class.java.isAssignableFrom(type as Class<*>?)
        } else false
    }
}