package org.netkernel.lang.kotlin.knkf.context

import org.netkernel.lang.kotlin.knkf.Identifier
import org.netkernel.lang.kotlin.knkf.LogLevel
import org.netkernel.lang.kotlin.knkf.context.request.*
import org.netkernel.lang.kotlin.knkf.context.response.ReadOnlyResponse
import org.netkernel.layer0.nkf.INKFRequestContext
import java.io.PrintWriter
import java.io.StringWriter

@DslMarker
annotation class ContextBuilderMarker

abstract class RequestContext(override val nkfContext: INKFRequestContext): BaseRequestContext {

    fun argumentValue(argumentName: String): String {
        return nkfContext.thisRequest.getArgumentValue(argumentName)
    }

    /**
     * Issue a DELETE request and return the response.
     */
    fun delete(identifier: Identifier, init: DeleteRequest.() -> Unit): Boolean {
        return deleteRequest(identifier, init).issue()
    }

    /**
     * Issue a DELETE request and return the response.
     */
    fun delete(identifier: String, init: DeleteRequest.() -> Unit): Boolean {
        return deleteRequest(Identifier(identifier), init).issue()
    }

    /**
     * Create a new DELETE request.
     */
    fun deleteRequest(identifier: Identifier, init: DeleteRequest.() -> Unit): DeleteRequest {
        val request = DeleteRequest(this, identifier)
        request.init()

        return request
    }

    /**
     * Create a new DELETE request.
     */
    fun deleteRequest(identifier: String, init: DeleteRequest.() -> Unit): DeleteRequest {
        val request = DeleteRequest(this, Identifier(identifier))
        request.init()

        return request
    }

    /**
     * Issue an EXISTS request and return the response.
     */
    fun exists(identifier: Identifier, init: ExistsRequest.() -> Unit): Boolean {
        return existsRequest(identifier, init).issue()
    }

    /**
     * Issue an EXISTS request and return the response.
     */
    fun exists(identifier: String, init: ExistsRequest.() -> Unit): Boolean {
        return existsRequest(Identifier(identifier), init).issue()
    }

    /**
     * Create an EXISTS request.
     */
    fun existsRequest(identifier: Identifier, init: ExistsRequest.() -> Unit): ExistsRequest {
        val request = ExistsRequest(this, identifier)
        request.init()

        return request
    }

    /**
     * Create an EXISTS request.
     */
    fun existsRequest(identifier: String, init: ExistsRequest.() -> Unit): ExistsRequest {
        val request = ExistsRequest(this, Identifier(identifier))
        request.init()

        return request
    }

    /**
     * Issue a NEW request and return the response.
     */
    inline fun <reified P> new(identifier: Identifier, noinline init: NewRequest<P>.() -> Unit = {}): Identifier {
        return new(identifier, P::class.java, init)
    }

    /**
     * Issue a NEW request and return the response.
     */
    inline fun <reified P> new(identifier: String, noinline init: NewRequest<P>.() -> Unit = {}): Identifier {
        return new(Identifier(identifier), init)
    }

    /**
     * Create a NEW request.
     */
    inline fun <reified P> newRequest(identifier: Identifier, noinline init: NewRequest<P>.() -> Unit = {}): NewRequest<P> {
        return newRequest(identifier, P::class.java, init)
    }

    /**
     * Create a NEW request.
     */
    inline fun <reified P> newRequest(identifier: String, noinline init: NewRequest<P>.() -> Unit = {}): NewRequest<P> {
        return newRequest(Identifier(identifier), P::class.java, init)
    }

    /**
     * Issue a SINK request.
     */
    inline fun <reified P> sink(identifier: Identifier, noinline init: SinkRequest<P>.() -> Unit = {}) {
        return sink(identifier, P::class.java, init)
    }

    /**
     * Issue a SINK request.
     */
    inline fun <reified P> sink(identifier: String, noinline init: SinkRequest<P>.() -> Unit = {}) {
        return sink(Identifier(identifier), P::class.java, init)
    }

    /**
     * Create a SINK request.
     */
    inline fun <reified P> sinkRequest(identifier: Identifier, noinline init: SinkRequest<P>.() -> Unit = {}): SinkRequest<P> {
        return sinkRequest(identifier, P::class.java, init)
    }

    /**
     * Create a SINK request.
     */
    inline fun <reified P> sinkRequest(identifier: String, noinline init: SinkRequest<P>.() -> Unit = {}): SinkRequest<P> {
        return sinkRequest(Identifier(identifier), P::class.java, init)
    }

    /**
     * Issue a SOURCE request and return the response.
     */
    inline fun <reified R> source(identifier: Identifier, noinline init: SourceRequest<R>.() -> Unit = {}): R {
        return source(identifier, R::class.java, init)
    }

    /**
     * Create a SOURCE request.
     */
    inline fun <reified R> sourceRequest(identifier: Identifier, noinline init: SourceRequest<R>.() -> Unit = {}): SourceRequest<R> {
        return sourceRequest(identifier, R::class.java, init)
    }

    /**
     * Create a SOURCE request.
     */
    inline fun <reified R> sourceRequest(identifier: String, noinline init: SourceRequest<R>.() -> Unit = {}): SourceRequest<R> {
        return sourceRequest(Identifier(identifier), R::class.java, init)
    }

    /**
     * Issue a SOURCE request and return the response.
     */
    inline fun <reified R> source(identifier: String, noinline init: SourceRequest<R>.() -> Unit = {}): R {
        return source(Identifier(identifier), R::class.java, init)
    }

    /**
     * Transrept the supplied value.
     */
    inline fun <reified T> transrept(value: Any): T {
        return transrept(value, T::class.java)
    }

    /**
     * Transrept the result of the supplied lambda.
     */
    inline fun <reified T> transrept(value: () -> Any): T {
        return transrept(value(), T::class.java)
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

    /**
     * Issue the given request, and return the result.
     */
    fun <R> issue(request: Request<R>): R {
        return request.issue()
    }

    /**
     * Issue the given request, and return the result.
     */
    fun <R> issue(request: () -> Request<R>): R {
        return issue(request())
    }

    /**
     * Issue the given request, and return the result as a response.
     */
    fun <R> issueForResponse(request: RequestWithResponse<R>): ReadOnlyResponse<R> {
        return request.issueForResponse()
    }

    /**
     * Issue the given request, and return the result as a response.
     */
    fun <R> issueForResponse(request: () -> RequestWithResponse<R>): ReadOnlyResponse<R> {
        return issueForResponse(request())
    }

    fun log(message: String) {
        log(message = message)
    }

    fun log(level: LogLevel = LogLevel.INFO, message: String) {
        nkfContext.logRaw(level.nkLevel, message)
    }

    fun log(ex: Exception, message: String = ex.localizedMessage) {
        log(message = message, ex = ex)
    }

    fun log(level: LogLevel = LogLevel.SEVERE, ex: Exception, message: String = ex.localizedMessage) {
        val sw = StringWriter()
        ex.printStackTrace(PrintWriter(sw))

        nkfContext.logRaw(level.nkLevel, "$message\n$sw")
    }
}
