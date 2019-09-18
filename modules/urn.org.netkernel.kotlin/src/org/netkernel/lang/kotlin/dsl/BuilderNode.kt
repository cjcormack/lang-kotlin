package org.netkernel.lang.kotlin.dsl

import org.netkernel.mod.hds.IHDSMutator

@DslMarker
annotation class HdsBuilderMarker

@HdsBuilderMarker
abstract class BuilderNode(internal val builder: IHDSMutator, internal val hdsNames: List<String>) {
    constructor(builder: IHDSMutator, hdsName: String) : this(builder, listOf(hdsName))
}

internal fun <T: BuilderNode> initNode(node: T, init: T.() -> Unit): T {
    node.hdsNames.forEach { node.builder.pushNode(it) }
    node.init()
    node.hdsNames.forEach { _ -> node.builder.popNode() }

    return node
}
