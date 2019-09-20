package org.netkernel.lang.kotlin.dsl.xunit

import org.netkernel.lang.kotlin.dsl.BuilderNode
import org.netkernel.mod.hds.IHDSMutator

class Assert(builderToClone: IHDSMutator): BuilderNode(builderToClone, "assert") {
    fun isTrue() {
        builder.addNode("true", null)
    }
    fun isFalse() {
        builder.addNode("false", null)
    }
    fun isNull() {
        builder.addNode("null", null)
    }
    fun isNotNull() {
        builder.addNode("notNull", null)
    }
    fun stringEquals(value: String) {
        builder.addNode("stringEquals", value)
    }
}
