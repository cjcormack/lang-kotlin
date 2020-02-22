package org.netkernel.lang.kotlin.knkf.context.request

import org.netkernel.lang.kotlin.knkf.Identifier
import org.netkernel.lang.kotlin.knkf.Verb
import org.netkernel.lang.kotlin.knkf.context.RequestContext
import org.netkernel.layer0.nkf.INKFRequest

class NewRequest<P> internal constructor(
        context: RequestContext,
        identifier: Identifier,
        override val primaryClass: Class<P>,
        nkfRequest: INKFRequest = context.nkfContext.createRequest(identifier.identifier)
) : RequestWithResponse<Identifier>(
        context,
        nkfRequest,
        Identifier::class.java,
        Verb.NEW
), RequestWithPrimary<P> by RequestWithPrimaryImpl(context, nkfRequest, primaryClass)

/**
 * Create a NEW response. Generally, you'd be better off using the inline version of this function.
 */
fun <P> RequestContext.newRequest(identifier: Identifier, primaryClass: Class<P>, init: NewRequest<P>.() -> Unit = {}): NewRequest<P> {
    val request = NewRequest<P>(this, identifier, primaryClass)
    request.init()

    return request
}

/**
 * Issue a NEW request and return the response. Generally, you'd be better off using the inline version of this function.
 */
fun <P> RequestContext.new(identifier: Identifier, primaryClass: Class<P>, init: NewRequest<P>.() -> Unit = {}): Identifier {
    return newRequest(identifier, primaryClass, init).issue()
}

class NewRequestToEndpoint<P> internal constructor(
        context: RequestContext,
        endpointId: String,
        override val primaryClass: Class<P>,
        nkfRequest: INKFRequest = context.nkfContext.createRequestToEndpoint(endpointId)
) : RequestWithResponse<Identifier>(
        context,
        nkfRequest,
        Identifier::class.java,
        Verb.NEW
), RequestWithPrimary<P> by RequestWithPrimaryImpl(context, nkfRequest, primaryClass)

fun <P> RequestContext.newRequestToEndpoint(endpointId: String, primaryClass: Class<P>, init: NewRequestToEndpoint<P>.() -> Unit = {}): NewRequestToEndpoint<P> {
    val request = NewRequestToEndpoint<P>(this, endpointId, primaryClass)
    request.init()

    return request
}

fun <P> RequestContext.newToEndpoint(endpointId: String, primaryClass: Class<P>, init: NewRequestToEndpoint<P>.() -> Unit = {}): Identifier {
    return newRequestToEndpoint(endpointId, primaryClass, init).issue()
}
