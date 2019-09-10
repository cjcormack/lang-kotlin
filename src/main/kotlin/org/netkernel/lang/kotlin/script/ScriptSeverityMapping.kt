package org.netkernel.lang.kotlin.script

import org.netkernel.lang.kotlin.knkf.LogLevel
import kotlin.script.experimental.api.ScriptDiagnostic

internal fun ScriptDiagnostic.Severity.getLogLevel(): LogLevel {
    return when(this) {
        ScriptDiagnostic.Severity.FATAL -> LogLevel.SEVERE
        ScriptDiagnostic.Severity.ERROR -> LogLevel.SEVERE
        ScriptDiagnostic.Severity.WARNING -> LogLevel.WARNING
        ScriptDiagnostic.Severity.INFO -> LogLevel.INFO
        ScriptDiagnostic.Severity.DEBUG -> LogLevel.DEBUG
    }
}
