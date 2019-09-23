package org.netkernel.lang.kotlin.knkf.context.request

import org.netkernel.lang.kotlin.knkf.Verb
import org.netkernel.lang.kotlin.knkf.context.RequestContext
import org.netkernel.layer0.nkf.INKFRequest

abstract class RequestWithResponse<R> internal constructor(context: RequestContext, nkfRequest: INKFRequest, representationClass: Class<R>, verb: Verb? = null): Request<R>(context, nkfRequest, representationClass, verb) {
    init {
        nkfRequest.setRepresentationClass(representationClass)
    }
}
