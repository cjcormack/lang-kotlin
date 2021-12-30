package org.netkernel.lang.kotlin.util

import org.intellij.lang.annotations.Language
import org.netkernel.layer1.representation.IDeterminateStringRepresentation

@JvmInline
value class SQL(@Language("SQL") val value: String): IDeterminateStringRepresentation {
    override fun getString(): String {
        return value
    }
}
