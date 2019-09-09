package org.netkernel.lang.kotlin.knkf.context.request

import org.netkernel.lang.kotlin.knkf.Identifier
import org.netkernel.lang.kotlin.knkf.Verb
import org.netkernel.lang.kotlin.knkf.context.RequestContext
import org.netkernel.layer0.nkf.INKFRequest

class SourceRequest<R>(
        context: RequestContext,
        identifier: Identifier,
        representationClass: Class<R>,
        nkfRequest: INKFRequest = context.nkfContext.createRequest(identifier.identifier)
): RequestWithResponse<R>(context, nkfRequest, representationClass, Verb.SOURCE)

/**
 * Create a SOURCE request. Generally, you'd be better off using the inline version of this function.
 */
fun <R> RequestContext.sourceRequest(identifier: Identifier, representationClass: Class<R>, init: SourceRequest<R>.() -> Unit = {}): SourceRequest<R> {
    val request = SourceRequest(this, identifier, representationClass)
    request.init()

    return request
}

/**
 * Create a SOURCE request.
 */
inline fun <reified R> RequestContext.sourceRequest(identifier: Identifier, noinline init: SourceRequest<R>.() -> Unit = {}): SourceRequest<R> {
    return sourceRequest(identifier, R::class.java, init)
}

/**
 * Create a SOURCE request.
 */
inline fun <reified R> RequestContext.sourceRequest(identifier: String, noinline init: SourceRequest<R>.() -> Unit = {}): SourceRequest<R> {
    return sourceRequest(Identifier(identifier), R::class.java, init)
}

/**
 * Issue a SOURCE request and return the response. Generally, you'd be better off using the inline version of this function.
 */
fun <R> RequestContext.source(identifier: Identifier, representationClass: Class<R>, init: SourceRequest<R>.() -> Unit = {}): R {
    return sourceRequest(identifier, representationClass, init).issue()
}
