package org.netkernel.lang.kotlin.knkf

import org.netkernel.layer0.nkf.INKFRequestContext
import org.netkernel.layer0.nkf.INKFResponseReadOnly
import java.io.PrintWriter
import java.io.StringWriter

abstract class BaseRequestContext<R>(val context: INKFRequestContext) {

    inline fun <reified T> issue(request: () -> Request<T>): T {
        val response = context.issueRequest(request().rawRequest)
        check(response is T)
        return response
    }

    inline fun <reified T> issue(request: Request<T>): T {
        val response = context.issueRequest(request.rawRequest)

        check(response is T)
        return response
    }

    fun <T> issueForResponse(request: Request<T>): ReadOnlyResponse<T> {
        val response = context.issueRequestForResponse(request.rawRequest)

        // sadly we can't properly check this cast
        @Suppress("UNCHECKED_CAST")
        return ReadOnlyResponse(response as INKFResponseReadOnly<T>)
    }

    fun response(response: ReadOnlyResponse<R>): Response<R> {
        return Response(context.createResponseFrom(response.nkResponse))
    }

    fun response(response: R): Response<R> {
        return Response(context.createResponseFrom(response))
    }

    fun response(init: () -> R): Response<R> {
        return response(init())
    }

    fun responseFromRequest(init: () -> Request<*>): Response<R> {
        return Response(context.createResponseFrom(init()))
    }

    fun responseFromOtherResponse(init: () -> ReadOnlyResponse<R>): Response<*> {
        return response(init())
    }

    inline fun <reified T> request(uri: String, verb: Verb, representationClass: Class<T> = T::class.java, init: GenericRequest<T>.() -> Unit): GenericRequest<T> {
        val request = GenericRequest(this, representationClass, uri, verb)
        request.init()
        return request
    }

    inline fun <reified T> sourceRequest(
            uri: String,
            init: SourceRequest<T>.() -> Unit
    ): SourceRequest<T> {
        val request = SourceRequest(this, T::class.java, uri)
        request.init()
        return request
    }

    inline fun <reified T> sourceRequest(uri: String): SourceRequest<T> {
        return SourceRequest(this, T::class.java, uri)
    }

    inline fun <reified T> source(uri: String): T {
        return source(uri) {}
    }

    inline fun <reified T> source(uri: String, init: SourceRequest<T>.() -> Unit): T {
        return issue(sourceRequest(uri, init))
    }

    inline fun <reified T> sourceForResponse(uri: String): ReadOnlyResponse<T> {
        return sourceForResponse(uri) {}
    }

    inline fun <reified T> sourceForResponse(uri: String, init: SourceRequest<T>.() -> Unit): ReadOnlyResponse<T> {
        return issueForResponse(sourceRequest(uri, init))
    }

    fun sinkRequest(
            uri: String,
            init: SinkRequest.() -> Unit
    ): SinkRequest {
        val request = SinkRequest(this, uri)
        request.init()
        return request
    }


    fun sink(uri: String, value: Any) {
        sink(uri) {
            primaryArgument(value)
        }
    }

    fun sink(uri: String, init: SinkRequest.() -> Unit) {
        issue(sinkRequest(uri, init))
    }

    fun newRequest(
            uri: String,
            init: NewRequest.() -> Unit
    ): NewRequest {
        val request = NewRequest(this, uri)
        request.init()
        return request
    }

    fun new(uri: String): Boolean {
        return new(uri) {}
    }

    fun new(uri: String, init: NewRequest.() -> Unit): Boolean {
        return issue(newRequest(uri, init))
    }

    fun existsRequest(
            uri: String,
            init: ExistsRequest.() -> Unit
    ): ExistsRequest {
        val request = ExistsRequest(this, uri)
        request.init()
        return request
    }

    fun existsRequest(uri: String): ExistsRequest {
        return ExistsRequest(this, uri)
    }

    fun exists(uri: String): Boolean {
        return exists(uri) {}
    }

    fun exists(uri: String, init: ExistsRequest.() -> Unit): Boolean {
        return issue(existsRequest(uri, init))
    }

    fun deleteRequest(
            uri: String,
            init: DeleteRequest.() -> Unit
    ): DeleteRequest {
        val request = DeleteRequest(this, uri)
        request.init()
        return request
    }

    fun deleteRequest(uri: String): ExistsRequest {
        return ExistsRequest(this, uri)
    }

    fun delete(uri: String): Boolean {
        return delete(uri) {}
    }

    fun delete(uri: String, init: DeleteRequest.() -> Unit): Boolean {
        return issue(deleteRequest(uri, init))
    }

    fun <T> transrept(value: Any, type: Class<T>): T {
        return context.transrept(value, type)
    }

    inline fun <reified T> transrept(value: Any): T {
        return context.transrept(value, T::class.java)
    }

    inline fun <reified T> transrept(value: () -> Any): T {
        return context.transrept(value(), T::class.java)
    }

    inline fun <reified T> sourcePrimary(): T {
        return context.sourcePrimary(T::class.java)
    }

    fun log(message: String) {
        log(message = message)
    }

    fun log(level: LogLevel = LogLevel.INFO, message: String) {
        context.logRaw(level.nkLevel, message)
    }

    fun log(ex: Exception, message: String = ex.localizedMessage) {
        log(message = message, ex = ex)
    }

    fun log(level: LogLevel = LogLevel.SEVERE, ex: Exception, message: String = ex.localizedMessage) {
        val sw = StringWriter()
        ex.printStackTrace(PrintWriter(sw))

        context.logRaw(level.nkLevel, "$message\n$sw")
    }
}
