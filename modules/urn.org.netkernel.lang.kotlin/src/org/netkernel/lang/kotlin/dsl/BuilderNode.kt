package org.netkernel.lang.kotlin.dsl

import org.netkernel.mod.hds.IHDSMutator

@DslMarker
annotation class HdsBuilderMarker

@HdsBuilderMarker
abstract class BuilderNode(builderToClone: IHDSMutator, internal val hdsNames: List<String>) {
    val builder: IHDSMutator = if (builderToClone.cursorXPath == "") {
        builderToClone.getFirstNode("/")
    } else {
        builderToClone.getFirstNode(builderToClone.cursorXPath)
    }

    constructor(builderToClone: IHDSMutator, hdsName: String) : this(builderToClone, listOf(hdsName))
}

internal fun <T: BuilderNode> initNode(node: T, init: T.() -> Unit): T {
    node.hdsNames.forEach { node.builder.pushNode(it) }
    node.init()
    // as we clone the builder, we don't need to pop the added nodes.
    // we also don't want to—adding a node to this builder later on should also add it as a child

    return node
}
