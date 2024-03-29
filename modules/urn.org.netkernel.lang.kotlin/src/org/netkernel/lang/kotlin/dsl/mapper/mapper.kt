package org.netkernel.lang.kotlin.dsl.mapper

import org.netkernel.lang.kotlin.dsl.BuilderNode
import org.netkernel.lang.kotlin.dsl.declarativeRequest.DeclarativeRequestContainer
import org.netkernel.lang.kotlin.dsl.declarativeRequest.DeclarativeRequestContainerImpl
import org.netkernel.lang.kotlin.dsl.declarativeRequest.Request
import org.netkernel.lang.kotlin.dsl.grammar.ActiveGrammar
import org.netkernel.lang.kotlin.dsl.grammar.SimpleGrammar
import org.netkernel.lang.kotlin.dsl.grammar.StandardGrammar
import org.netkernel.lang.kotlin.dsl.hds.HdsRoot
import org.netkernel.lang.kotlin.dsl.initNode
import org.netkernel.lang.kotlin.inline.*
import org.netkernel.lang.kotlin.knkf.Identifier
import org.netkernel.mod.hds.HDSFactory
import org.netkernel.mod.hds.IHDSDocument
import org.netkernel.mod.hds.IHDSMutator

fun mapper(init: MapperConfig.() -> Unit): IHDSDocument {
    val mapperConfig = initNode(MapperConfig(), init)

    return mapperConfig.builder.toDocument(false)
}

class MapperConfig(): BuilderNode(HDSFactory.newDocument(), "config") {
    fun import(uri: String) {
        builder.addNode("import", uri)
    }

    fun endpoint(init: Endpoint.() -> Unit) = initNode(Endpoint(builder), init)

    fun meta(init: Meta.() -> Unit) = initNode(Meta(builder), init)
}

class Endpoint(builderToClone: IHDSMutator): BuilderNode(builderToClone, "endpoint"), DeclarativeRequestContainer {
    private val requestFactory = DeclarativeRequestContainerImpl(builder, "request")

    override fun request(identifier: Identifier, init: Request.() -> Unit) = requestFactory.request(identifier, init)
    override fun request(identifier: String, init: Request.() -> Unit) = requestFactory.request(identifier, init)
    override fun inlineSource(init: Request.() -> Unit, lambda: InlineSourceLambda) = requestFactory.inlineSource(init, lambda)
    override fun inlineSink(init: Request.() -> Unit, lambda: InlineSinkLambda) = requestFactory.inlineSink(init, lambda)
    override fun inlineExists(init: Request.() -> Unit, lambda: InlineExistsLambda) = requestFactory.inlineExists(init, lambda)
    override fun inlineNew(init: Request.() -> Unit, lambda: InlineNewLambda) = requestFactory.inlineNew(init, lambda)
    override fun inlineDelete(init: Request.() -> Unit, lambda: InlineDeleteLambda) = requestFactory.inlineDelete(init, lambda)

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

    fun meta(init: Meta.() -> Unit) = initNode(Meta(builder), init)
}

class Meta(builderToClone: IHDSMutator): BuilderNode(builderToClone, "meta") {
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
