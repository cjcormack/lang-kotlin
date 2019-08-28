package org.netkernel.lang.kotlin.dsl.grammar

import org.netkernel.lang.kotlin.dsl.BuilderNode
import org.netkernel.lang.kotlin.dsl.initNode
import org.netkernel.mod.hds.IHDSMutator

class ActiveGrammar(builder: IHDSMutator): BuilderNode(builder, listOf("grammar", "active")) {
    fun argument(name: String) {
        argument(name) {
            name(name)
        }
    }

    fun argument(name: String, init: ActiveGrammarArgument.() -> Unit) = initNode(ActiveGrammarArgument(builder)) {
        name(name)
        init()
    }

    fun varargs() {
        builder.pushNode("varargs")
        builder.popNode()
    }
}

class ActiveGrammarArgument(builder: IHDSMutator): BuilderNode(builder, "argument") {
    internal fun name(name: String) {
        builder.addNode("@name", name)
    }
    fun desc(description: String) {
        builder.addNode("@desc", description)
    }
    fun min(minimum: Int) {
        builder.addNode("@min", minimum)
    }
    fun max(maximum: Int) {
        builder.addNode("@max", maximum)
    }
}
