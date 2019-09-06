package org.netkernel.lang.kotlin.util

import org.netkernel.lang.kotlin.knkf.BaseRequestContext
import org.netkernel.mod.hds.IHDSReader

inline fun <reified T> IHDSReader.firstValue(path: String, context: BaseRequestContext<*>? = null, default: T? = null): T {
    return firstValue(T::class.java, path, context, default)
}

fun <T> IHDSReader.firstValue(type: Class<T>, path: String, context: BaseRequestContext<*>? = null, default: T? = null): T {
    val value = getFirstValueOrNull(path)

    if (type.isInstance(value)) {
        @Suppress("UNCHECKED_CAST")
        return value as T
    }

    if (value == null) {
        if (default != null) {
            return default
        } else {
            throw Exception("Value is unexpectedly null")
        }
    }

    if (context == null) {
        throw Exception("Value is wrong type, and no context supplied for transreption (expected a ${type}, got a ${value.javaClass})")
    }

    return context.context.transrept(value, type)
}

inline fun <reified T> IHDSReader.values(path: String, context: BaseRequestContext<*>? = null): List<T> {
    return values(T::class.java, path, context)
}

fun <T> IHDSReader.values(type: Class<T>, path: String, context: BaseRequestContext<*>? = null): List<T> {
    val values = getValues(path)

    return values.map { value ->
        if (type.isInstance(value)) {
            @Suppress("UNCHECKED_CAST")
            value as T
        } else {
            if (context == null) {
                throw Exception("Value is wrong type, and no context supplied for transreption (expected a ${type}, got a ${value.javaClass})")
            }

            context.context.transrept(value, type)
        }
    }
}
