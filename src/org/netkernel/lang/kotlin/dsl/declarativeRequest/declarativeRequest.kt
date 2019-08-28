package org.netkernel.lang.kotlin.dsl.declarativeRequest

import org.netkernel.lang.kotlin.dsl.BuilderNode
import org.netkernel.lang.kotlin.dsl.hds.HdsRoot
import org.netkernel.lang.kotlin.dsl.initNode
import org.netkernel.lang.kotlin.knkf.Verb
import org.netkernel.mod.hds.IHDSDocument
import org.netkernel.mod.hds.IHDSMutator
import kotlin.reflect.KClass

class Request(builder: IHDSMutator): BuilderNode(builder, "request") {
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

    fun argument(name: String, identifier: String, init: (RequestArgument.() -> Unit)? = null) = initNode(RequestArgument(builder)) {
        builder.addNode("@name", name)

        if (init != null) {
            init()
        }

        builder.setValue(identifier)
    }

    fun argument(name: String, init: RequestArgument.() -> Unit) = initNode(RequestArgument(builder)) {
        builder.addNode("@name", name)
        init()
    }

    fun varargs() {
        builder.pushNode("varargs")
        builder.popNode()
    }

    fun header(name: String, init: RequestHeader.() -> Unit) = initNode(RequestHeader(builder)) {
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

abstract class LiteralBuilder(builder: IHDSMutator, hdsName: String): BuilderNode(builder, hdsName) {
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

class RequestArgument(builder: IHDSMutator): LiteralBuilder(builder, "argument") {
    fun identifier(identifier: String) {
        builder.setValue(identifier)
    }

    fun method(method: Method) {
        builder.addNode("@method", method.value)
    }

    fun tolerant(tolerant: Boolean) {
        builder.addNode("@tolerant", tolerant)
    }

    fun request(identifier: String, init: Request.() -> Unit) = initNode(Request(builder)) {
        builder.addNode("identifier", identifier)
        init()
    }

    enum class Method(val value: String) {
        VALUE("value"),
        DATA_URI("data-uri"),
        AS_STRING("as-string"),
        FROM_STRING("from-string")
    }
}

class LiteralConstructorArguments(builder: IHDSMutator): LiteralBuilder(builder, "literal")

class RequestHeader(builder: IHDSMutator): LiteralBuilder(builder, "header") {
    fun sticky(sticky: Boolean) {
        builder.addNode("@sticky", sticky)
    }
}
