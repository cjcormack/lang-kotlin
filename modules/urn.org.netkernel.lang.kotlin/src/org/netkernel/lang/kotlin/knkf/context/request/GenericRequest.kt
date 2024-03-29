package org.netkernel.lang.kotlin.knkf.context.request

import org.netkernel.lang.kotlin.knkf.Identifier
import org.netkernel.lang.kotlin.knkf.Verb
import org.netkernel.lang.kotlin.knkf.context.RequestContext
import org.netkernel.layer0.nkf.INKFRequest

abstract class BaseGenericRequest<R, P> internal constructor(
        context: RequestContext,
        nkfRequest: INKFRequest,
        representationClass: Class<R>,
        override val primaryClass: Class<P>
) : RequestWithResponse<R>(
        context,
        nkfRequest,
        representationClass
), RequestWithPrimary<P> by RequestWithPrimaryImpl(context, nkfRequest, primaryClass) {
    fun verb(verb: Verb) {
        nkfRequest.setVerb(verb.nkfVerb)
    }
}

class GenericRequest<R, P> internal constructor(
        context: RequestContext,
        nkfRequest: INKFRequest,
        representationClass: Class<R>,
        override val primaryClass: Class<P>
) : BaseGenericRequest<R, P>(context, nkfRequest, representationClass, primaryClass) {
    internal constructor(
            context: RequestContext,
            identifier: Identifier,
            representationClass: Class<R>,
            primaryClass: Class<P>,
            nkfRequest: INKFRequest = context.nkfContext.createRequest(identifier.identifier)
    ) : this(context, nkfRequest, representationClass, primaryClass)
}

class GenericRequestToEndpoint<R, P> internal constructor(
        context: RequestContext,
        endpointId: String,
        representationClass: Class<R>,
        primaryClass: Class<P>,
        nkfRequest: INKFRequest = context.nkfContext.createRequestToEndpoint(endpointId)
) : BaseGenericRequest<R, P>(context, nkfRequest, representationClass, primaryClass)

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
 * Create a generic request to a specific enpdpoint. This is useful if you want to programmatically set the verb for the
 * request (if you don't, you'd be better off using a specific request function). Generally, you'd be better off using the
 * inline version of this function.
 */
fun <R, P> RequestContext.requestToEndpoint(endpointId: String, representationClass: Class<R>, primaryClass: Class<P>, init: GenericRequestToEndpoint<R, P>.() -> Unit = {}): GenericRequestToEndpoint<R, P> {
    val request = GenericRequestToEndpoint(this, endpointId, representationClass, primaryClass)
    request.init()

    return request
}
