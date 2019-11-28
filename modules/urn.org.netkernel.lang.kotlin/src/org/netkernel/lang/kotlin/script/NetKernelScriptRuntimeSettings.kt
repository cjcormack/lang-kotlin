package org.netkernel.lang.kotlin.script

import org.netkernel.lang.kotlin.knkf.context.RequestContext
import kotlin.reflect.KClass
import kotlin.script.experimental.api.ScriptCompilationConfiguration
import kotlin.script.experimental.api.ScriptEvaluationConfiguration

class NetKernelScriptRuntimeSettings(
        val scriptRepresentation: KClass<out BaseScriptRepresentation>,
        val scriptConfiguration: ScriptCompilationConfiguration,
        val configBuilder: (context: RequestContext) -> ScriptEvaluationConfiguration
)
