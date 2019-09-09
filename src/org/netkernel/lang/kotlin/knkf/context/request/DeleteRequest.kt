package org.netkernel.lang.kotlin.knkf.context.request

import org.netkernel.lang.kotlin.knkf.Identifier
import org.netkernel.lang.kotlin.knkf.Verb
import org.netkernel.lang.kotlin.knkf.context.RequestContext
import org.netkernel.layer0.nkf.INKFRequest

class DeleteRequest internal constructor(
        context: RequestContext,
        identifier: Identifier,
        nkfRequest: INKFRequest = context.nkfContext.createRequest(identifier.identifier)
): RequestWithResponse<Boolean>(
        context,
        nkfRequest,
        Boolean::class.java,
        Verb.DELETE
)

/**
 * Create a new DELETE request.
 */
fun RequestContext.deleteRequest(identifier: Identifier, init: DeleteRequest.() -> Unit): DeleteRequest {
    val request = DeleteRequest(this, identifier)
    request.init()

    return request
}

/**
 * Create a new DELETE request.
 */
fun RequestContext.deleteRequest(identifier: String, init: DeleteRequest.() -> Unit): DeleteRequest {
    val request = DeleteRequest(this, Identifier(identifier))
    request.init()

    return request
}
