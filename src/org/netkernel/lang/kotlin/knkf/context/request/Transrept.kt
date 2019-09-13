package org.netkernel.lang.kotlin.knkf.context.request

import org.netkernel.lang.kotlin.knkf.context.RequestContext

/**
 * Transrept the supplied value. Generally, you'd be better off using the inline version of this function.
 */
fun <T> RequestContext.transrept(value: Any, type: Class<T>): T {
    return nkfContext.transrept(value, type)
}
