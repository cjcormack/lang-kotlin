package org.netkernel.lang.kotlin.knkf.context.request

import org.netkernel.lang.kotlin.knkf.context.RequestContext
import org.netkernel.lang.kotlin.knkf.context.response.ReadOnlyResponse

/**
 * Issue the given request, and return the result.
 */
fun <R> RequestContext.issue(request: Request<R>): R {
    return request.issue()
}

/**
 * Issue the given request, and return the result.
 */
fun <R> RequestContext.issue(request: () -> Request<R>): R {
    return issue(request())
}

/**
 * Issue the given request, and return the result as a response.
 */
fun <R> RequestContext.issueForResponse(request: RequestWithResponse<R>): ReadOnlyResponse<R> {
    return request.issueForResponse()
}

/**
 * Issue the given request, and return the result as a response. Generally, you'd be better off using the inline version of this function.
 */
fun <R> RequestContext.issueForResponse(request: () -> RequestWithResponse<R>): ReadOnlyResponse<R> {
    return issueForResponse(request())
}
