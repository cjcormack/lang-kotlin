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
        Boolean::class.javaObjectType,
        Verb.EXISTS
)

class ExistsRequestToEndpoint internal constructor(
        context: RequestContext,
        endpointId: String,
        nkfRequest: INKFRequest = context.nkfContext.createRequestToEndpoint(endpointId)
): RequestWithResponse<Boolean>(
        context,
        nkfRequest,
        Boolean::class.javaObjectType,
        Verb.EXISTS
)
