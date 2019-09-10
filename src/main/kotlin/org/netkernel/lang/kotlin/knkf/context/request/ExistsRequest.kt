package org.netkernel.lang.kotlin.knkf.context.request

import org.netkernel.lang.kotlin.knkf.Identifier
import org.netkernel.lang.kotlin.knkf.Verb
import org.netkernel.lang.kotlin.knkf.context.RequestContext
import org.netkernel.layer0.nkf.INKFRequest

class ExistsRequest internal constructor(
        context: RequestContext,
        identifier: Identifier,
        nkfRequest: INKFRequest = context.nkfContext.createRequest(identifier.identifier)
): RequestWithResponse<Boolean>(
        context,
        nkfRequest,
        Boolean::class.java,
        Verb.EXISTS
)

/**
 * Create an EXISTS request.
 */
fun RequestContext.existsRequest(identifier: Identifier, init: ExistsRequest.() -> Unit): ExistsRequest {
    val request = ExistsRequest(this, identifier)
    request.init()

    return request
}

/**
 * Create an EXISTS request.
 */
fun RequestContext.existsRequest(identifier: String, init: ExistsRequest.() -> Unit): ExistsRequest {
    val request = ExistsRequest(this, Identifier(identifier))
    request.init()

    return request
}
