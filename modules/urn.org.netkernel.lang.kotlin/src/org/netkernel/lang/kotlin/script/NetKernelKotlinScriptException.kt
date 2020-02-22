package org.netkernel.lang.kotlin.script

import kotlin.script.experimental.api.ScriptDiagnostic

class NetKernelKotlinScriptException(val report: List<ScriptDiagnostic>): Exception("Compilation error")
