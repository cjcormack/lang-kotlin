package org.netkernel.lang.kotlin.script

import org.netkernel.lang.kotlin.knkf.context.*
import kotlin.script.experimental.annotations.KotlinScript

@KotlinScript(fileExtension = "nk.kts", displayName = "NetKernel Kotlin Script", compilationConfiguration = NetKernelScriptConfiguration::class)
abstract class NetKernelKotlinScript(val context: RequestContext) {
    fun onDelete(lazyOnDelete: DeleteRequestContext.() -> Unit) {
        if (context is DeleteRequestContext) {
            context.lazyOnDelete()
        }
    }

    fun onExists(lazyOnExists: ExistsRequestContext.() -> Unit) {
        if (context is ExistsRequestContext) {
            context.lazyOnExists()
        }
    }

    fun onNew(lazyOnNew: NewRequestContext.() -> Unit) {
        if (context is NewRequestContext) {
            context.lazyOnNew()
        }
    }

    fun onSource(lazyOnSource: SourceRequestContext.() -> Unit) {
        if (context is SourceRequestContext) {
            context.lazyOnSource()
        }
    }

    fun onSink(lazyOnSink: SinkRequestContext.() -> Unit) {
        if (context is SinkRequestContext) {
            context.lazyOnSink()
        }
    }
}
