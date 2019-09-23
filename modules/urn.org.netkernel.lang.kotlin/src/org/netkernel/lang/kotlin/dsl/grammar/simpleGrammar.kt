package org.netkernel.lang.kotlin.dsl.grammar

import org.netkernel.lang.kotlin.dsl.BuilderNode
import org.netkernel.mod.hds.IHDSMutator

class SimpleGrammar(builderToClone: IHDSMutator): BuilderNode(builderToClone, listOf("grammar", "simple")) {
    var currentValue = ""
        set(value) {
            field = value
            builder.setValue(value)
        }

    fun rawPattern(pattern: String) {
        currentValue += pattern
    }

    operator fun String.unaryPlus() {
        currentValue += this.replace("{", "\\{").replace("}", "\\}")
    }

    fun token(name: String, regex: Regex? = null) {
        val regexString = if (regex != null) {
            ":${regex.pattern}"
        } else {
            ""
        }

        currentValue += "{$name$regexString}"
    }
}
