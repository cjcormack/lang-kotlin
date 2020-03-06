package org.netkernel.lang.kotlin.script

import kotlin.script.experimental.api.ScriptDiagnostic

data class NetKernelKotlinScriptCompilationResult(val report: List<ScriptDiagnostic>)
