package org.netkernel.lang.kotlin.knkf.context

import org.netkernel.lang.kotlin.knkf.Identifier
import org.netkernel.lang.kotlin.knkf.LogLevel
import org.netkernel.lang.kotlin.knkf.context.request.*
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
     * Issue a SOURCE request and return the response.
     */
    inline fun <reified R> source(identifier: Identifier, noinline init: SourceRequest<R>.() -> Unit = {}): R {
        return source(identifier, R::class.java, init)
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
