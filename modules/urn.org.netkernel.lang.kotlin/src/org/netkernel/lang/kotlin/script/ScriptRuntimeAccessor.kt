package org.netkernel.lang.kotlin.script

import kotlinx.coroutines.runBlocking
import org.netkernel.lang.kotlin.knkf.context.*
import org.netkernel.layer0.util.RequestScopeClassLoader
import kotlin.script.experimental.api.ResultValue
import kotlin.script.experimental.api.valueOrThrow
import kotlin.script.experimental.jvmhost.BasicJvmScriptingHost

class ScriptRuntimeAccessor: BaseScriptAccessor() {
    override fun DeleteRequestContext.onDelete() = runScript()
    override fun ExistsRequestContext.onExists() = runScript()
    override fun NewRequestContext.onNew() = runScript()
    override fun SourceRequestContext.onSource() = runScript()
    override fun SinkRequestContext.onSink() = runScript()

    private fun RequestContext.runScript() {
        val cl = RequestScopeClassLoader(nkfContext.kernelContext.requestScope)
        Thread.currentThread().contextClassLoader = cl

        val kotlinScriptConfig = loadKotlinScriptConfig()

        // we partially escape type-safety here to get the correct type
        val scriptRequest = sourceRequest<BaseScriptRepresentation>("arg:operator")
        scriptRequest.nkfRequest.setRepresentationClass(kotlinScriptConfig.scriptRepresentation.java)

        val script = scriptRequest.issue()

        val evalConfig = kotlinScriptConfig.configBuilder(this@runScript)

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

        val result = evalResult.valueOrThrow()

        val returnValue = result.returnValue
        if (returnValue is ResultValue.Error) {
            throw returnValue.error
        }
    }
}
