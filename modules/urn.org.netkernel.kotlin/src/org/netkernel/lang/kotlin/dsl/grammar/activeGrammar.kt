package org.netkernel.lang.kotlin.dsl.grammar

import org.netkernel.lang.kotlin.dsl.BuilderNode
import org.netkernel.lang.kotlin.dsl.initNode
import org.netkernel.mod.hds.IHDSMutator
import kotlin.reflect.KClass

class ActiveGrammar(builderToClone: IHDSMutator): BuilderNode(builderToClone, listOf("grammar", "active")) {

    fun argument(name: String, desc: String? = null, min: Int? = null, max: Int? = null, init: (Argument.() -> Unit)? = null) = initNode(Argument(builder)) {
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

        if (init != null) {
            init()
        }
    }

    fun varargs() {
        builder.pushNode("varargs")
        builder.popNode()
    }
}

class Argument(builderToClone: IHDSMutator): BuilderNode(builderToClone, "argument") {
    fun representation(representationClass: KClass<*>) {
        builder.addNode("representation", representationClass.qualifiedName)
    }

    fun representation(representationClass: Class<*>) {
        builder.addNode("representation", representationClass.canonicalName)
    }

    fun representation(representationClassName: String) {
        builder.addNode("representation", representationClassName)
    }
}
