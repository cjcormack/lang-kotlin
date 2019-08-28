package org.netkernel.lang.kotlin.dsl.grammar

import org.netkernel.lang.kotlin.dsl.BuilderNode
import org.netkernel.lang.kotlin.dsl.initNode
import org.netkernel.mod.hds.IHDSMutator

class ActiveGrammar(builder: IHDSMutator): BuilderNode(builder, listOf("grammar", "active")) {

    fun argument(name: String, desc: String? = null, min: Int? = null, max: Int? = null) {
        builder.pushNode("argument")
        builder.addNode("@name", name)

        if (desc != null) {
            builder.addNode("@desc", desc)
        }
        if (min != null) {
            builder.addNode("@min", min)
        }
        if (max != null) {
            builder.addNode("@max", max)
        }

        builder.popNode()
    }

    fun varargs() {
        builder.pushNode("varargs")
        builder.popNode()
    }
}
