package org.netkernel.lang.kotlin.knkf

import org.netkernel.mod.hds.HDSFactory
import org.netkernel.mod.hds.IHDSDocument
import org.netkernel.mod.hds.IHDSMutator

@DslMarker
annotation class HdsMarker

@HdsMarker
abstract class HdsNode(internal val builder: IHDSMutator) {
    fun node(name: String, init: HdsElement.() -> Any): HdsElement {
        val element = HdsElement(builder)

        this.builder.pushNode(name)
        val res = element.init()

        if (res !is HdsElement) {
            this.builder.setValue(res)
        }

        this.builder.popNode()

        return element
    }

    fun node(name: String, value: Any): HdsElement {
        builder.addNode(name, value)

        return HdsElement(builder)
    }
}

class Hds(): HdsNode(HDSFactory.newDocument()) {
    fun build(): IHDSDocument {
        return builder.toDocument(false)
    }
}

class HdsElement(builder: IHDSMutator): HdsNode(builder)

fun hds(init: Hds.() -> Unit): Hds {
    val hds = Hds()
    hds.init()
    return hds
}
