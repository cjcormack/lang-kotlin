package org.netkernel.lang.kotlin.script

import kotlinx.coroutines.runBlocking
import org.netkernel.lang.kotlin.knkf.KotlinTransreptor
import org.netkernel.lang.kotlin.knkf.context.TransreptorRequestContext
import org.netkernel.lang.kotlin.knkf.context.sourcePrimary
import kotlin.script.experimental.api.ScriptCompilationConfiguration
import kotlin.script.experimental.api.valueOrNull
import kotlin.script.experimental.host.toScriptSource
import kotlin.script.experimental.jvm.dependenciesFromClassloader
import kotlin.script.experimental.jvm.jvm
import kotlin.script.experimental.jvmhost.BasicJvmScriptingHost

class ScriptTransreptor: KotlinTransreptor<Any, ScriptRepresentation>() {
    init {
        this.declareThreadSafe()
        this.toRepresentation(ScriptRepresentation::class.java)
    }

    override fun TransreptorRequestContext<ScriptRepresentation>.onTransrept() {
        val compilationConfiguration = ScriptCompilationConfiguration(listOf(NetKernelScriptConfiguration)){
            jvm {
                dependenciesFromClassloader(classLoader = nkfContext.getKotlinCompilerClassLoader(), wholeClasspath = true)
            }
        }

        val script = sourcePrimary<String>()

        val compiledScriptResult = runBlocking {
            BasicJvmScriptingHost().compiler(script.toScriptSource(), compilationConfiguration)
        }

        compiledScriptResult.reports.forEach {
            val e = it.exception
            if (e != null) {
                throw e
            }

            log(it.severity.getLogLevel(), "${it.message} ${it.location}")
        }

        val compiledScript = compiledScriptResult.valueOrNull()
        checkNotNull(compiledScript) // should not be null if we compiled without error

        response {
            ScriptRepresentation(compiledScript)
        }
    }
}
