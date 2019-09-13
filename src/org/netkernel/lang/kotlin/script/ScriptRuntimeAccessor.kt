package org.netkernel.lang.kotlin.script

import kotlinx.coroutines.runBlocking
import org.netkernel.lang.kotlin.knkf.context.*
import org.netkernel.lang.kotlin.knkf.endpoints.KotlinAccessor
import org.netkernel.layer0.util.RequestScopeClassLoader
import kotlin.script.experimental.api.ScriptEvaluationConfiguration
import kotlin.script.experimental.api.providedProperties
import kotlin.script.experimental.jvmhost.BasicJvmScriptingHost

class ScriptRuntimeAccessor: KotlinAccessor() {
    init {
        this.declareThreadSafe()
    }

    override fun DeleteRequestContext.onDelete() = runScript()
    override fun ExistsRequestContext.onExists() = runScript()
    override fun NewRequestContext.onNew() = runScript()
    override fun SourceRequestContext.onSource() = runScript()
    override fun SinkRequestContext.onSink() = runScript()

    private fun RequestContext.runScript() {
        val cl = RequestScopeClassLoader(nkfContext.kernelContext.requestScope)
        Thread.currentThread().contextClassLoader = cl

        val script = source<ScriptRepresentation>("arg:operator")

        val evalConfig = ScriptEvaluationConfiguration {
            providedProperties(Pair("context", this@runScript))
        }

        val evalResult = runBlocking {
            BasicJvmScriptingHost().evaluator(script.script, evalConfig)
        }

        evalResult.reports.forEach {
            val e = it.exception
            if (e != null) {
                e.printStackTrace()
                throw e
            }

            log(it.severity.getLogLevel(), "${it.message} ${it.location}")
        }
    }
}
