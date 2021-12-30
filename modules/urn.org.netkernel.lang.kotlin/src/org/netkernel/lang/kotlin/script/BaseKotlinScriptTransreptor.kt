package org.netkernel.lang.kotlin.script

import kotlinx.coroutines.runBlocking
import org.netkernel.lang.kotlin.knkf.context.RequestContext
import org.netkernel.lang.kotlin.knkf.context.TransreptorRequestContext
import org.netkernel.lang.kotlin.knkf.context.sourcePrimary
import org.netkernel.lang.kotlin.knkf.endpoints.KotlinTransreptor
import kotlin.script.experimental.api.CompiledScript
import kotlin.script.experimental.api.ScriptCompilationConfiguration
import kotlin.script.experimental.api.ScriptDiagnostic
import kotlin.script.experimental.api.valueOrThrow
import kotlin.script.experimental.host.toScriptSource
import kotlin.script.experimental.jvm.dependenciesFromClassloader
import kotlin.script.experimental.jvm.jvm
import kotlin.script.experimental.jvmhost.BasicJvmScriptingHost

internal fun RequestContext.compileKotlin(scriptCompilationConfiguration: ScriptCompilationConfiguration, script: String): Pair<CompiledScript, List<ScriptDiagnostic>> {
    val compilationConfiguration = ScriptCompilationConfiguration(listOf(scriptCompilationConfiguration)) {
        jvm {
            dependenciesFromClassloader(classLoader = nkfContext.getKotlinCompilerClassLoader(), wholeClasspath = true)
        }
    }

    val compiledScriptResult = runBlocking {
        BasicJvmScriptingHost().compiler(script.toScriptSource(), compilationConfiguration)
    }

    val report = compiledScriptResult.reports.filter {
        if (it.severity == ScriptDiagnostic.Severity.DEBUG) {
            false
        } else {
            log(it.severity.getLogLevel(), "${it.message} ${it.location}")
            true
        }
    }

    try {
        return Pair(compiledScriptResult.valueOrThrow(), report)
    } catch (e: Exception) {
        throw NetKernelKotlinScriptCompileException(report)
    }
}

abstract class BaseKotlinScriptTransreptor<T: BaseScriptRepresentation>: KotlinTransreptor<Any, T>() {
    init {
        this.declareThreadSafe()
    }

    protected fun TransreptorRequestContext<T>.performCompilation(scriptCompilationConfiguration: ScriptCompilationConfiguration): CompiledScript {
        val script = sourcePrimary<String>()

        return compileKotlin(scriptCompilationConfiguration, script).first
    }
}
