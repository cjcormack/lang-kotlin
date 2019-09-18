package org.netkernel.lang.kotlin.dsl.restOverlay

import org.netkernel.lang.kotlin.dsl.BuilderNode
import org.netkernel.lang.kotlin.dsl.grammar.SimpleGrammar
import org.netkernel.lang.kotlin.dsl.initNode
import org.netkernel.lang.kotlin.dsl.mapper.Meta
import org.netkernel.lang.kotlin.knkf.Verb
import org.netkernel.mod.hds.IHDSMutator

enum class Method {
    GET,
    POST,
    PUT,
    PATCH,
    HEAD,
    DELETE
}

fun Meta.rest(init: RestMetaNode.() -> Unit) = initNode(RestMetaNode(builder), init)

class RestMetaNode(builder: IHDSMutator): BuilderNode(builder, "rest") {
    fun method(vararg methods: Method) {
        builder.addNode("method", methods.joinToString(","))
    }

    fun simpleGrammar(init: SimpleGrammar.() -> Unit) = initNode(SimpleGrammar(builder), init)
    fun simpleGrammar(pattern: String) = initNode(SimpleGrammar(builder)) {
        rawPattern(pattern)
    }

    fun produces(mimeType: String, withTransform: String? = null) {
        builder.pushNode("produces")

        if (withTransform != null) {
            builder.addNode("withTransform", withTransform)
        }

        builder.setValue(mimeType)

        builder.popNode()
    }

     fun consumes(vararg mimeTypes: String) {
         builder.addNode("consumes", mimeTypes.joinToString(","))
     }

    fun language(language: String) {
        builder.addNode("language", language)
    }

    fun mustBeSSL() {
        builder.pushNode("mustBeSSL")
        builder.popNode()
    }

    fun etag() {
        builder.pushNode("Etag")
        builder.popNode()
    }

    fun compress() {
        builder.pushNode("compress")
        builder.popNode()
    }

    fun ignoreAmbiguityWarning() {
        builder.pushNode("ignoreAmbiguityWarning")
        builder.popNode()
    }

    fun id(id: String) {
        builder.addNode("id", id)
    }

    fun exceptionTarget(exceptionTarget: String) {
        builder.addNode("exceptionTarget", exceptionTarget)
    }

    fun preTarget(preTarget: String) {
        builder.addNode("preTarget", preTarget)
    }

    fun wrapperTarget(wrapperTarget: String) {
        builder.addNode("wrapperTarget", wrapperTarget)
    }

    fun maxContentLength(maxContentLength: Int) {
        builder.addNode("maxContentLength", maxContentLength)
    }

    fun verbTranslate(verbTranslation: Map<Verb, Method>) {
        builder.addNode("verbTranslate", verbTranslation.map {
            "${it.key.name}:${it.value.name}"
        }.joinToString(","))
    }
}
