package org.netkernel.lang.kotlin.knkf.context.request

import org.netkernel.lang.kotlin.knkf.Identifier
import org.netkernel.lang.kotlin.knkf.Verb
import org.netkernel.lang.kotlin.knkf.context.ContextBuilderMarker
import org.netkernel.lang.kotlin.knkf.context.RequestContext
import org.netkernel.lang.kotlin.knkf.context.response.ReadOnlyResponse
import org.netkernel.layer0.nkf.INKFRequest
import org.netkernel.layer0.nkf.INKFResponseReadOnly

@ContextBuilderMarker
abstract class Request<R> internal constructor(val context: RequestContext, val nkfRequest: INKFRequest, val representationClass: Class<R>, verb: Verb? = null) {
    init {
        if (verb != null) {
            nkfRequest.setVerb(verb.nkfVerb)
        }
    }

    /**
     * Adds an argument to the request using pass-by-value.
     */
    fun argumentByValue(paramName: String, paramValue: Any) {
        nkfRequest.addArgumentByValue(paramName, paramValue)
    }

    /**
     * Adds an argument to the request using pass-by-value of the result of the supplied lambda.
     */
    fun argumentByValue(paramName: String, init: () -> Any) {
        argumentByValue(paramName, init())
    }

    /**
     * Adds an argument to the request as an identifier.identifier.
     */
    fun argument(paramName: String, paramIdentifier: Identifier) {
        nkfRequest.addArgument(paramName, paramIdentifier.identifier)
    }

    /**
     * Adds an argument to the request as an identifier.identifier.
     */
    fun argument(paramName: String, init: () -> Identifier) {
        argument(paramName, init())
    }

    /**
     * Adds an argument to the request as an inner request.
     */
    fun argumentByRequest(paramName: String, paramRequest: RequestWithResponse<*>) {
        nkfRequest.addArgumentByRequest(paramName, paramRequest.nkfRequest)
    }

    /**
     * Adds an argument to the request as an inner request.
     */
    fun argumentByRequest(paramName: String, init: () -> RequestWithResponse<*>) {
        argumentByRequest(paramName, init())
    }

    /**
     * Adds an argument to the request as the response from another request.
     */
    fun argumentFromResponse(paramName: String, request: RequestWithResponse<Any>) {
        val response = context.nkfContext.issueRequestForResponse(request.nkfRequest)
        nkfRequest.addArgumentFromResponse(paramName, response)
    }

    /**
     * Adds an argument to the request as the response from another request.
     */
    fun argumentFromResponse(paramName: String, init: () -> RequestWithResponse<Any>) {
        argumentFromResponse(paramName, init())
    }

    /**
     * Issue this request, and return the response.
     */
    fun issue(): R {
        val response = context.nkfContext.issueRequest(nkfRequest)

        if (representationClass != Unit::class.java && representationClass != Any::class.java) {
            check(representationClass.isInstance(response))
        }

        @Suppress("UNCHECKED_CAST")
        return response as R
    }

    /**
     * Issue this request, and return the response.
     */
    fun issueAsync() {
        context.nkfContext.issueAsyncRequest(nkfRequest)
    }

    fun issueForResponse(): ReadOnlyResponse<R> {
        val response = context.nkfContext.issueRequestForResponse(nkfRequest)

        // TODO we need to handle nullable classifiers here
        if (representationClass != Unit::class.java && representationClass != Any::class.java) {
            check(representationClass.isInstance(response.representation)) {"Response is not a ${representationClass.canonicalName}, but is a ${response.representation?.javaClass}"}
        }

        @Suppress("UNCHECKED_CAST")
        return ReadOnlyResponse(response as INKFResponseReadOnly<R>)
    }
}
