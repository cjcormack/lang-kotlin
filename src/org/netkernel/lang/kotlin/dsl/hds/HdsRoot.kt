package org.netkernel.lang.kotlin.dsl.hds

import org.netkernel.lang.kotlin.dsl.BuilderNode
import org.netkernel.lang.kotlin.dsl.initNode
import org.netkernel.mod.hds.HDSFactory
import org.netkernel.mod.hds.IHDSDocument
import org.netkernel.mod.hds.IHDSMutator

abstract class HdsNode(builder: IHDSMutator, hdsName: String): BuilderNode(builder, hdsName) {
    fun el(name: String, init: HdsElement.() -> Any) = initNode(HdsElement(builder, name)) {
        val res = init()

        if (res !is BuilderNode) {
            this.builder.setValue(res)
        }
    }

    fun el(name: String, value: Any): HdsElement {
        builder.addNode(name, value)

        return HdsElement(builder, name)
    }
}

class HdsRoot(): HdsNode(HDSFactory.newDocument(), "")

class HdsElement(builder: IHDSMutator, hdsName: String): HdsNode(builder, hdsName) {
    fun attr(name: String, value: String) {
        builder.addNode("@$name", value)
    }
}

fun hds(init: HdsRoot.() -> Unit): IHDSDocument {
    val hds = HdsRoot()
    hds.init()
    return hds.builder.toDocument(false)
}
