package org.netkernel.lang.kotlin.knkf

import org.netkernel.layer0.nkf.INKFRequest

open class Request<T> internal constructor(protected val requestContext: BaseRequestContext<*>, val rawRequest: INKFRequest) {
    internal constructor(requestContext: BaseRequestContext<*>, representationClass: Class<T>, uri: String, verb: Verb) : this(requestContext, requestContext.context.createRequest(uri)) {
        rawRequest.setVerb(verb.nkfVerb)
        rawRequest.setRepresentationClass(representationClass)
    }

    fun argument(paramName: String, paramValue: Any) {
        rawRequest.addArgumentByValue(paramName, paramValue)
    }

    fun argument(paramName: String, paramIdentifier: Identifier) {
        rawRequest.addArgument(paramName, paramIdentifier.identifier)
    }

    fun argument(paramName: String, lambda: () -> Any) {
        val result = lambda()
        if (result is SourceRequest<*>) {
            rawRequest.addArgumentByRequest(paramName, result.rawRequest)
        } else {
            rawRequest.addArgumentByValue(paramName, result)
        }
    }

    fun argument(paramName: String, paramRequest: SourceRequest<*>) {
        rawRequest.addArgumentByRequest(paramName, paramRequest.rawRequest)
    }
}

fun identifier(identifier: String): Identifier {
    return Identifier(identifier)
}

data class Identifier(val identifier: String)

interface RequestWithPrimaryArgument {
    fun primaryArgument(value: Any)
    fun primaryArgument(response: ReadOnlyResponse<*>)
    fun primaryArgument(request: () -> SourceRequest<*>)
}

class GenericRequest<T>(requestContext: BaseRequestContext<*>, representationClass: Class<T>, uri: String, verb: Verb): Request<T>(requestContext, representationClass, uri, verb), RequestWithPrimaryArgument {
    override fun primaryArgument(value: Any) {
        rawRequest.addPrimaryArgument(value)
    }

    override fun primaryArgument(response: ReadOnlyResponse<*>) {
        rawRequest.addPrimaryArgumentFromResponse(response.nkResponse)
    }

    override fun primaryArgument(request: () -> SourceRequest<*>) {
        rawRequest.addPrimaryArgumentFromResponse(requestContext.context.issueRequestForResponse(request().rawRequest))
    }
}

class SourceRequest<T>(requestContext: BaseRequestContext<*>, representationClass: Class<T>, uri: String) : Request<T>(requestContext, representationClass, uri, Verb.SOURCE)

class ExistsRequest(requestContext: BaseRequestContext<*>, uri: String) : Request<Boolean>(requestContext, Boolean::class.java, uri, Verb.EXISTS)

class DeleteRequest(requestContext: BaseRequestContext<*>, uri: String) : Request<Boolean>(requestContext, Boolean::class.java, uri, Verb.DELETE)

class NewRequest(requestContext: BaseRequestContext<*>, uri: String) : Request<Boolean>(requestContext, Boolean::class.java, uri, Verb.NEW), RequestWithPrimaryArgument {
    override fun primaryArgument(value: Any) {
        rawRequest.addPrimaryArgument(value)
    }

    override fun primaryArgument(response: ReadOnlyResponse<*>) {
        rawRequest.addPrimaryArgumentFromResponse(response.nkResponse)
    }

    override fun primaryArgument(request: () -> SourceRequest<*>) {
        rawRequest.addPrimaryArgumentFromResponse(requestContext.context.issueRequestForResponse(request().rawRequest))
    }
}

class SinkRequest(requestContext: BaseRequestContext<*>, uri: String) : Request<Any>(requestContext, Any::class.java, uri, Verb.SINK), RequestWithPrimaryArgument {
    override fun primaryArgument(value: Any) {
        rawRequest.addPrimaryArgument(value)
    }

    override fun primaryArgument(response: ReadOnlyResponse<*>) {
        rawRequest.addPrimaryArgumentFromResponse(response.nkResponse)
    }

    override fun primaryArgument(request: () -> SourceRequest<*>) {
        rawRequest.addPrimaryArgumentFromResponse(requestContext.context.issueRequestForResponse(request().rawRequest))
    }
}
