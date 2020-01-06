package org.netkernel.lang.kotlin.dsl.declarativeRequest

import org.netkernel.lang.kotlin.dsl.BuilderNode
import org.netkernel.lang.kotlin.dsl.hds.HdsRoot
import org.netkernel.lang.kotlin.dsl.initNode
import org.netkernel.lang.kotlin.inline.*
import org.netkernel.lang.kotlin.knkf.Identifier
import org.netkernel.lang.kotlin.knkf.Verb
import org.netkernel.mod.hds.HDSFactory
import org.netkernel.mod.hds.IHDSDocument
import org.netkernel.mod.hds.IHDSMutator
import kotlin.reflect.KClass

fun declarativeRequest(identifier: String, init: Request.() -> Unit = {}): IHDSDocument = declarativeRequest(Identifier(identifier), init)

fun declarativeRequest(identifier: Identifier, init: Request.() -> Unit = {}): IHDSDocument {
    val declarativeRequest = initNode(Request(HDSFactory.newDocument()), {
        builder.addNode("identifier", identifier.identifier)
        init()
    })

    return declarativeRequest.builder.toDocument(false)
}

class Request(builderToClone: IHDSMutator, hdsName: String = "request"): BuilderNode(builderToClone, hdsName) {
    fun verb(verb: Verb) {
        builder.addNode("verb", verb.name)
    }

    fun representation(representationClass: KClass<*>) {
        builder.addNode("representation", representationClass.qualifiedName)
    }

    fun representation(representationClass: Class<*>) {
        builder.addNode("representation", representationClass.canonicalName)
    }

    fun representation(representationClassName: String) {
        builder.addNode("representation", representationClassName)
    }

    fun argument(name: String, identifier: String? = null, method: ArgumentMethod? = null, tolerant: Boolean? = null, init: (Argument.() -> Unit)? = null) = initNode(Argument(builder)) {
        builder.addNode("@name", name)

        if (method != null) {
            builder.addNode("@method", method.value)
        }
        if (tolerant != null) {
            builder.addNode("@tolerant", tolerant)
        }

        if (init != null) {
            init()
        }

        if (identifier != null) {
            builder.setValue(identifier)
        }
    }

    fun varargs() {
        builder.pushNode("varargs")
        builder.popNode()
    }

    fun header(name: String, init: Header.() -> Unit) = initNode(Header(builder)) {
        builder.addNode("@name", name)

        init()
    }

    fun header(name: String, value: String, sticky: Boolean? = null) = header(name) {
        if (sticky != null) {
            sticky(sticky)
        }

        builder.setValue(value)
    }
}

enum class ArgumentMethod(val value: String) {
    VALUE("value"),
    DATA_URI("data-uri"),
    AS_STRING("as-string"),
    FROM_STRING("from-string")
}

abstract class LiteralBuilder(builderToClone: IHDSMutator, hdsName: String): BuilderNode(builderToClone, hdsName) {
    private fun doLiteral(type: String, value: Any) = initNode(LiteralConstructorArguments(builder)) {
        builder.addNode("@type", type)
        builder.setValue(value)
    }

    fun literal(value: IHDSMutator) = initNode(LiteralConstructorArguments(builder)) {
        builder.addNode("@type", "hds2")
        builder.appendChildren(value)
    }
    fun literal(value: IHDSDocument) = literal(value.mutableClone)
    fun literal(value: HdsRoot) = literal(value.builder)
    fun literalHds(init: HdsRoot.() -> Unit): LiteralConstructorArguments {
        val hds = HdsRoot()
        hds.init()

        return literal(hds)
    }

    fun literal(value: String) = doLiteral("string", value)
    fun literal(value: Boolean) = doLiteral("boolean", value)
    fun literal(value: Char) = doLiteral("char", value)
    fun literal(value: Int) = doLiteral("integer", value)
    fun literal(value: Byte) = doLiteral("byte", value)
    fun literal(value: Long) = doLiteral("long", value)
    fun literal(value: Float) = doLiteral("float", value)
    fun literal(value: Double) = doLiteral("double", value)

    fun literalObject(value: KClass<*>, init: LiteralConstructorArguments.() -> Unit) = initNode(LiteralConstructorArguments(builder)) {
        builder.addNode("@type", value.qualifiedName)
        init()
    }

    fun literalObject(value: Class<*>, init: LiteralConstructorArguments.() -> Unit) = initNode(LiteralConstructorArguments(builder)) {
        builder.addNode("@type", value.canonicalName)
        init()
    }
}

interface DeclarativeRequestContainer {
    fun request(identifier: Identifier, init: Request.() -> Unit = {}): Request
    fun request(identifier: String, init: Request.() -> Unit = {}): Request
    fun inlineSource(init: Request.() -> Unit = {}, lambda: InlineSourceLambda): Request
    fun inlineSink(init: Request.() -> Unit = {}, lambda: InlineSinkLambda): Request
    fun inlineExists(init: Request.() -> Unit = {}, lambda: InlineExistsLambda): Request
    fun inlineNew(init: Request.() -> Unit = {}, lambda: InlineNewLambda): Request
    fun inlineDelete(init: Request.() -> Unit = {}, lambda: InlineDeleteLambda): Request
}

internal class DeclarativeRequestContainerImpl(private val builder: IHDSMutator, private val requestHdsName: String): DeclarativeRequestContainer {
    override fun request(identifier: Identifier, init: Request.() -> Unit) = initNode(Request(builder, requestHdsName)) {
        builder.addNode("identifier", identifier.identifier)
    }
    override fun request(identifier: String, init: Request.() -> Unit) = request(Identifier(identifier), init)

    override fun inlineSource(init: Request.() -> Unit, lambda: InlineSourceLambda) = inlineRequest(Verb.SOURCE, init, InlineRequest(lambda))

    override fun inlineSink(init: Request.() -> Unit, lambda: InlineSinkLambda) = inlineRequest(Verb.SINK, init, InlineRequest(lambda))

    override fun inlineExists(init: Request.() -> Unit, lambda: InlineExistsLambda) = inlineRequest(Verb.EXISTS, init, InlineRequest(lambda))

    override fun inlineNew(init: Request.() -> Unit, lambda: InlineNewLambda) = inlineRequest(Verb.NEW, init, InlineRequest(lambda))

    override fun inlineDelete(init: Request.() -> Unit, lambda: InlineDeleteLambda) = inlineRequest(Verb.DELETE, init, InlineRequest(lambda))

    private fun <T> inlineRequest(verb: Verb, init: Request.() -> Unit, inlineRequest: InlineRequest<T>) = initNode(Request(builder, requestHdsName)) {
        builder.addNode("identifier", "active:inlineKotlin")
        verb(verb)
        argument("operator") {
            literalHds {
                node("lambda", inlineRequest)
            }
        }
        init()
    }
}

class Argument(builderToClone: IHDSMutator): LiteralBuilder(builderToClone, "argument"), DeclarativeRequestContainer {
    private val requestFactory = DeclarativeRequestContainerImpl(builder, "request")

    override fun request(identifier: Identifier, init: Request.() -> Unit) = requestFactory.request(identifier, init)
    override fun request(identifier: String, init: Request.() -> Unit) = requestFactory.request(identifier, init)
    override fun inlineSource(init: Request.() -> Unit, lambda: InlineSourceLambda) = requestFactory.inlineSource(init, lambda)
    override fun inlineSink(init: Request.() -> Unit, lambda: InlineSinkLambda) = requestFactory.inlineSink(init, lambda)
    override fun inlineExists(init: Request.() -> Unit, lambda: InlineExistsLambda) = requestFactory.inlineExists(init, lambda)
    override fun inlineNew(init: Request.() -> Unit, lambda: InlineNewLambda) = requestFactory.inlineNew(init, lambda)
    override fun inlineDelete(init: Request.() -> Unit, lambda: InlineDeleteLambda) = requestFactory.inlineDelete(init, lambda)
}

class LiteralConstructorArguments(builderToClone: IHDSMutator): LiteralBuilder(builderToClone, "literal")

class Header(builderToClone: IHDSMutator): LiteralBuilder(builderToClone, "header") {
    fun sticky(sticky: Boolean) {
        builder.addNode("@sticky", sticky)
    }
}
