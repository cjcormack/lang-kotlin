package org.netkernel.lang.kotlin.dsl.asserts.hds

import org.netkernel.lang.kotlin.dsl.BuilderNode
import org.netkernel.lang.kotlin.dsl.initNode
import org.netkernel.mod.hds.IHDSMutator
import org.netkernel.lang.kotlin.dsl.xunit.Assert as TestAssert

class HdsSchematron(builderToClone: IHDSMutator): BuilderNode(builderToClone, listOf("hdsSchematron", "literal")) {
    fun schema(id: String, init: Schema.() -> Unit) = initNode(Schema(builder)) {
        builder.addNode("@id", id)
        init()
    }
}

class Schema(builderToClone: IHDSMutator): BuilderNode(builderToClone, "schema") {
    fun pattern(init: Pattern.() -> Unit) = initNode(Pattern(builder), init)
}

class Pattern(builderToClone: IHDSMutator): BuilderNode(builderToClone, "pattern") {
    fun rule(context: String, init: Rule.() -> Unit) = initNode(Rule(builder)) {
        builder.addNode("@context", context)
        init()
    }
}

class Rule(builderToClone: IHDSMutator): BuilderNode(builderToClone, "rule") {
    fun assert(test: String, description: String) = initNode(Assert(builder)) {
        builder.addNode("@test", test)
        builder.setValue(description)
    }
}

class Assert(builderToClone: IHDSMutator): BuilderNode(builderToClone, "assert")

fun TestAssert.hdsSchematron(init: HdsSchematron.() -> Unit) = initNode(HdsSchematron(builder)) {
    builder.addNode("@type", "hds")
    init()
}
