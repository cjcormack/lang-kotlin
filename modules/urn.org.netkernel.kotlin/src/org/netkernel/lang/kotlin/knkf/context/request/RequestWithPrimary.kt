package org.netkernel.lang.kotlin.knkf.context.request

import org.netkernel.lang.kotlin.knkf.context.RequestContext
import org.netkernel.lang.kotlin.knkf.context.response.BaseResponse
import org.netkernel.lang.kotlin.knkf.context.response.ReadOnlyResponse
import org.netkernel.lang.kotlin.knkf.context.response.Response
import org.netkernel.layer0.nkf.INKFRequest

interface RequestWithPrimary<P> {
    val primaryClass: Class<P>

    /**
     * Set the primary argument of the request as a literal value.
     */
    fun primaryArgument(value: P)

    /**
     * Set the primary argument of the request as the literal value returned by the lambda.
     */
    fun primaryArgument(init: () -> P)

    /**
     * Set the primary argument of the request to the supplied response.
     */
    fun primaryArgumentFromResponse(response: BaseResponse<P>)

    /**
     * Set the primary argument of the request to the supplied response.
     */
    fun primaryArgumentFromResponse(init: () -> BaseResponse<P>)
}

internal class RequestWithPrimaryImpl<P>(val context: RequestContext, val nkfRequest: INKFRequest, override val primaryClass: Class<P>): RequestWithPrimary<P> {
    override fun primaryArgument(value: P) {
        nkfRequest.addPrimaryArgument(value)
    }

    override fun primaryArgument(init: () -> P) {
        primaryArgument(init())
    }

    override fun primaryArgumentFromResponse(response: BaseResponse<P>) {
        // deliberate return so that the 'when' is treated as an expression and so forces us to handle all of the cases.
        return when (response) {
            is Response -> nkfRequest.addPrimaryArgumentFromResponse(response.nkfResponse)
            is ReadOnlyResponse -> nkfRequest.addPrimaryArgumentFromResponse(response.nkfResponse)
        }
    }

    override fun primaryArgumentFromResponse(init: () -> BaseResponse<P>) {
        primaryArgumentFromResponse(init())
    }
}
