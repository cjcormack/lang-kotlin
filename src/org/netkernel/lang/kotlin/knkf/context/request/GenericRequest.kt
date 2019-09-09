package org.netkernel.lang.kotlin.knkf.context.request

import org.netkernel.lang.kotlin.knkf.Identifier
import org.netkernel.lang.kotlin.knkf.Verb
import org.netkernel.lang.kotlin.knkf.context.RequestContext
import org.netkernel.layer0.nkf.INKFRequest

class GenericRequest<R, P> internal constructor(
        context: RequestContext,
        nkfRequest: INKFRequest,
        representationClass: Class<R>,
        override val primaryClass: Class<P>
) : RequestWithResponse<R>(
        context,
        nkfRequest,
        representationClass
), RequestWithPrimary<P> by RequestWithPrimaryImpl(context, nkfRequest, primaryClass) {
    internal constructor(
            context: RequestContext,
            identifier: Identifier,
            representationClass: Class<R>,
            primaryClass: Class<P>,
            nkfRequest: INKFRequest = context.nkfContext.createRequest(identifier.identifier)
    ) : this(context, nkfRequest, representationClass, primaryClass)

    fun verb(verb: Verb) {
        nkfRequest.setVerb(verb.nkfVerb)
    }
}

/**
 * Create a generic request. This is useful if you want to programmatically set the verb for the request (if you don't,
 * you'd be better off using a specific request function). Generally, you'd be better off using the inline version of
 * this function.
 */
fun <R, P> RequestContext.request(identifier: Identifier, representationClass: Class<R>, primaryClass: Class<P>, init: GenericRequest<R, P>.() -> Unit = {}): GenericRequest<R, P> {
    val request = GenericRequest(this, identifier, representationClass, primaryClass)
    request.init()

    return request
}

/**
 * Create a generic request. This is useful if you want to programmatically set the verb for the request (if you don't,
 * you'd be better off using a specific request function).
 */
inline fun <reified R, reified P> RequestContext.request(identifier: Identifier, noinline init: GenericRequest<R, P>.() -> Unit = {}): GenericRequest<R, P> {
    return request(identifier, R::class.java, P::class.java, init)
}

/**
 * Create a generic request. This is useful if you want to programmatically set the verb for the request (if you don't,
 * you'd be better off using a specific request function).
 */
inline fun <reified R, reified P> RequestContext.request(identifier: String, noinline init: GenericRequest<R, P>.() -> Unit = {}): GenericRequest<R, P> {
    return request(Identifier(identifier), R::class.java, P::class.java, init)
}
