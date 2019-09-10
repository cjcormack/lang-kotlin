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
 * Create a SINK request.
 */
inline fun <reified P> RequestContext.sinkRequest(identifier: Identifier, noinline init: SinkRequest<P>.() -> Unit = {}): SinkRequest<P> {
    return sinkRequest(identifier, P::class.java, init)
}

/**
 * Create a SINK request.
 */
inline fun <reified P> RequestContext.sinkRequest(identifier: String, noinline init: SinkRequest<P>.() -> Unit = {}): SinkRequest<P> {
    return sinkRequest(Identifier(identifier), P::class.java, init)
}

/**
 * Issue a SINK request. Generally, you'd be better off using the inline version of this function.
 */
fun <P> RequestContext.sink(identifier: Identifier, primaryClass: Class<P>, init: SinkRequest<P>.() -> Unit = {}) {
    return sinkRequest(identifier, primaryClass, init).issue()
}
