package org.netkernel.lang.kotlin.knkf.context.request

import org.netkernel.lang.kotlin.knkf.Identifier
import org.netkernel.lang.kotlin.knkf.Verb
import org.netkernel.lang.kotlin.knkf.context.RequestContext
import org.netkernel.layer0.nkf.INKFRequest

class SinkRequest<P> internal constructor(
        context: RequestContext,
        identifier: Identifier,
        override val primaryClass: Class<P>,
        nkfRequest: INKFRequest = context.nkfContext.createRequest(identifier.identifier)
): Request<Unit>(
        context,
        nkfRequest,
        Unit::class.java,
        Verb.SINK
), RequestWithPrimary<P> by RequestWithPrimaryImpl(context, nkfRequest, primaryClass)

/**
 * Create a SINK request. Generally, you'd be better off using the inline version of this function.
 */
fun <P> RequestContext.sinkRequest(identifier: Identifier, primaryClass: Class<P>, init: SinkRequest<P>.() -> Unit = {}): SinkRequest<P> {
    val request = SinkRequest(this, identifier, primaryClass)
    request.init()

    return request
}

/**
 * Issue a SINK request. Generally, you'd be better off using the inline version of this function.
 */
fun <P> RequestContext.sink(identifier: Identifier, primaryClass: Class<P>, init: SinkRequest<P>.() -> Unit = {}) {
    return sinkRequest(identifier, primaryClass, init).issue()
}

class SinkRequestToEndpoint<P> internal constructor(
        context: RequestContext,
        endpointId: String,
        override val primaryClass: Class<P>,
        nkfRequest: INKFRequest = context.nkfContext.createRequestToEndpoint(endpointId)
): Request<Unit>(
        context,
        nkfRequest,
        Unit::class.java,
        Verb.SINK
), RequestWithPrimary<P> by RequestWithPrimaryImpl(context, nkfRequest, primaryClass)

fun <P> RequestContext.sinkRequestToEndpoint(endpointId: String, primaryClass: Class<P>, init: SinkRequestToEndpoint<P>.() -> Unit = {}): SinkRequestToEndpoint<P> {
    val request = SinkRequestToEndpoint(this, endpointId, primaryClass)
    request.init()

    return request
}

fun <P> RequestContext.sinkToEndpoint(endpointId: String, primaryClass: Class<P>, init: SinkRequestToEndpoint<P>.() -> Unit = {}) {
    return sinkRequestToEndpoint(endpointId, primaryClass, init).issue()
}
