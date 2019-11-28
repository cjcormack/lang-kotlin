package org.netkernel.lang.kotlin.knkf.context

import org.netkernel.lang.kotlin.knkf.context.request.RequestWithResponse
import org.netkernel.lang.kotlin.knkf.context.response.BaseResponse
import org.netkernel.lang.kotlin.knkf.context.response.ReadOnlyResponse
import org.netkernel.lang.kotlin.knkf.context.response.Response
import org.netkernel.layer0.nkf.INKFRequestContext

abstract class RequestContextWithResponse<R>(nkfContext: INKFRequestContext): RequestContext(nkfContext) {
    fun response(response: R): Response<R> {
        if (response is BaseResponse<*>) {
            // this isn't perfectly generic like the 'responseFromOtherResponse' is, but we don't need to be (the kernel
            // will handle this properly for us and transrept if necessary).
            val nkfResponse = when (response) {
                is Response<*> -> response.nkfResponse
                is ReadOnlyResponse<*> -> response.nkfResponse
            }
            return Response(nkfContext.createResponseFrom(nkfResponse))
        }

        return Response(nkfContext.createResponseFrom(response))
    }

    fun response(init: () -> R): Response<R> {
        return response(init())
    }

    fun responseFromRequest(request: RequestWithResponse<R>): Response<R> {
        return Response(nkfContext.createResponseFrom(request.issueForResponse().nkfResponse))
    }

    fun responseFromRequest(init: () -> RequestWithResponse<R>): Response<R> {
        return responseFromRequest(init())
    }

    fun setResponse(response: ReadOnlyResponse<R>): Response<R> {
        return Response(nkfContext.createResponseFrom(response.nkfResponse))
    }

    fun setResponse(init: () -> ReadOnlyResponse<R>): Response<R> {
        return setResponse(init())
    }
}
