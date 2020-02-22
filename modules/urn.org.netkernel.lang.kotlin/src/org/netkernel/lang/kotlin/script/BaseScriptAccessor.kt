package org.netkernel.lang.kotlin.script

import org.netkernel.lang.kotlin.knkf.context.RequestContext
import org.netkernel.lang.kotlin.knkf.endpoints.KotlinAccessor
import kotlin.script.experimental.api.ScriptEvaluationConfiguration
import kotlin.script.experimental.api.providedProperties

abstract class BaseScriptAccessor: KotlinAccessor() {
    internal fun RequestContext.loadKotlinScriptConfig(): NetKernelScriptRuntimeSettings {
        val kotlinScriptConfig = if (exists("arg:scriptRuntimeSettings")) {
            source("arg:scriptRuntimeSettings")
        } else {
            // default settings
            NetKernelScriptRuntimeSettings(ScriptRepresentation::class, NetKernelScriptConfiguration) {
                ScriptEvaluationConfiguration {
                    providedProperties(Pair("context", it))
                }
            }
        }
        return kotlinScriptConfig
    }
}
