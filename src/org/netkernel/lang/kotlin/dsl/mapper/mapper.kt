package org.netkernel.lang.kotlin.dsl.mapper

import org.netkernel.lang.kotlin.dsl.BuilderNode
import org.netkernel.lang.kotlin.dsl.declarativeRequest.Request
import org.netkernel.lang.kotlin.dsl.grammar.ActiveGrammar
import org.netkernel.lang.kotlin.dsl.grammar.SimpleGrammar
import org.netkernel.lang.kotlin.dsl.grammar.StandardGrammar
import org.netkernel.lang.kotlin.dsl.hds.HdsRoot
import org.netkernel.lang.kotlin.dsl.initNode
import org.netkernel.mod.hds.HDSFactory
import org.netkernel.mod.hds.IHDSDocument
import org.netkernel.mod.hds.IHDSMutator

fun mapper(init: MapperConfig.() -> Unit): IHDSDocument {
    val mapperConfig = initNode(MapperConfig(), init)

    return mapperConfig.builder.toDocument(false)
}

class MapperConfig(): BuilderNode(HDSFactory.newDocument(), "config") {
    fun endpoint(init: Endpoint.() -> Unit) = initNode(Endpoint(builder), init)

    fun meta(init: Meta.() -> Unit) = initNode(Meta(builder), init)
}

class Endpoint(builder: IHDSMutator): BuilderNode(builder, "endpoint") {
    fun id(id: String) {
        builder.addNode("id", id)
    }

    fun activeGrammar(identifier: String) = activeGrammar(identifier) {}

    fun activeGrammar(identifier: String, init: ActiveGrammar.() -> Unit) = initNode(ActiveGrammar(builder)) {
        builder.addNode("identifier", identifier)
        init()
    }

    fun simpleGrammar(init: SimpleGrammar.() -> Unit) = initNode(SimpleGrammar(builder), init)

    fun simpleGrammar(pattern: String) = initNode(SimpleGrammar(builder)) {
        rawPattern(pattern)
    }

    fun standardGrammar(init: StandardGrammar.() -> Unit) = initNode(StandardGrammar(builder), init)

    fun request(identifier: String, init: Request.() -> Unit) = initNode(Request(builder)) {
        builder.addNode("identifier", identifier)
        init()
    }

    fun meta(init: Meta.() -> Unit) = initNode(Meta(builder), init)
}

class Meta(builder: IHDSMutator): BuilderNode(builder, "meta") {
    fun node(name: String, value: IHDSMutator) {
        builder.pushNode(name)
        builder.appendChildren(value)
        builder.popNode()
    }
    fun node(name: String, value: IHDSDocument) = node(name, value.mutableClone)
    fun node(name: String, value: HdsRoot) = node(name, value.builder)
    fun hdsNode(name: String, init: HdsRoot.() -> Unit) {
        val hds = HdsRoot()
        hds.init()

        return node(name, hds)
    }
}
