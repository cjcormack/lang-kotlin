package org.netkernel.lang.kotlin.knkf.context.request

import org.netkernel.lang.kotlin.knkf.context.RequestContext

/**
 * Get an issuable clone of the context's request.
 */
fun RequestContext.issuableClone(init: Request<Any>.() -> Unit = {}): Request<Any> {
    val request = GenericRequest(this, nkfContext.thisRequest.issuableClone, Any::class.java, Any::class.java)
    request.init()
    return request
}
