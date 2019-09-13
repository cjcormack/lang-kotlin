package org.netkernel.lang.kotlin.knkf.context

interface RequestContextWithPrimary: BaseRequestContext

/**
 * Get the Primary argument of the request.
 */
inline fun <reified P> RequestContextWithPrimary.sourcePrimary(): P {
    return sourcePrimary(P::class.java)
}

/**
 * Get the Primary argument of the request. Generally, you'd be better off using the inline version of this function.
 */
fun <P> RequestContextWithPrimary.sourcePrimary(primaryClass: Class<P>): P {
    return nkfContext.sourcePrimary(primaryClass)
}
