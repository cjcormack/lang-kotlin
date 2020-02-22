package org.netkernel.lang.kotlin.dsl.hds

import org.netkernel.lang.kotlin.dsl.BuilderNode
import org.netkernel.lang.kotlin.dsl.initNode
import org.netkernel.mod.hds.HDSFactory
import org.netkernel.mod.hds.IHDSDocument
import org.netkernel.mod.hds.IHDSMutator

abstract class HdsNode(builderToClone: IHDSMutator, hdsName: String): BuilderNode(builderToClone, hdsName) {
    fun node(name: String, init: HdsElement.() -> Any) = initNode(HdsElement(builder, name)) {
        val res = init()

        if (res !is BuilderNode && res !is IHDSMutator && res !is Unit) {
            this.builder.setValue(res)
        }
    }

    fun node(name: String, value: Any?): HdsElement {
        builder.addNode(name, value)

        return HdsElement(builder, name)
    }
}

class HdsRoot(): HdsNode(HDSFactory.newDocument(), "")

class HdsElement(builderToClone: IHDSMutator, hdsName: String): HdsNode(builderToClone, hdsName) {}

fun hds(init: HdsRoot.() -> Unit): IHDSDocument {
    val hds = HdsRoot()
    hds.init()
    return hds.builder.toDocument(false)
}
