package org.netkernel.lang.kotlin.dsl.grammar

import org.netkernel.lang.kotlin.dsl.BuilderNode
import org.netkernel.lang.kotlin.dsl.initNode
import org.netkernel.mod.hds.IHDSMutator

abstract class BaseStandardGrammarNode(builder: IHDSMutator, hdsName: String): BuilderNode(builder, hdsName) {
    fun group(init: (StandardGrammarGroup.() -> Unit)) = initNode(StandardGrammarGroup(builder)) {
        init()
    }

    fun optional(init: (StandardGrammarNode.() -> Unit)) = initNode(StandardGrammarNode(builder, "optional"), init)
    fun choice(init: (StandardGrammarNode.() -> Unit)) = initNode(StandardGrammarNode(builder, "choice"), init)
    fun interleave(init: (StandardGrammarNode.() -> Unit)) = initNode(StandardGrammarNode(builder, "interleave"), init)
}

class StandardGrammarNode(builder: IHDSMutator, hdsName: String): BaseStandardGrammarNode(builder, hdsName)

enum class RegexType(val value: String) {
    URI("uri"),
    ACTIVE_ESCAPED_URI("active-escaped-uri"),
    ACTIVE_ESCAPED_URI_LOOSE("active-escaped-uri-loose"),
    RELATIVE_PATH("relative-path"),
    RELATIVE_DIRECTORY_PATH("relative-directory-path"),
    PATH_SEGMENT("path-segment"),
    INTEGER("integer"),
    FLOAT("float"),
    NMTOKEN("nmtoken"),
    ALPHANUM("alphanum"),
    ANYTHING("anything"),
    NOTHING("nothing")
}

class StandardGrammar(builder: IHDSMutator): BaseStandardGrammarNode(builder, "grammar")

class StandardGrammarGroup(builder: IHDSMutator): BaseStandardGrammarNode(builder, "group") {
    fun name(name: String) {
        builder.addNode("@name", name)
    }

    fun min(min: Int) {
        builder.addNode("@min", min)
    }

    fun max(max: Int) {
        builder.addNode("@max", max)
    }

    fun encoding(encoding: Encoding) {
        builder.addNode("@encoding", encoding.value)
    }

    operator fun String.unaryPlus() {
        builder.setValue(this)
    }

    fun regex(regex: Regex) {
        builder.addNode("regex", regex.pattern)
    }

    fun regex(type: RegexType) {
        builder.pushNode("regex")
        builder.addNode("@type", type.value)
        builder.popNode()
    }

    enum class Encoding(val value: String) {
        ACTIVE("active"),
        URL_QUERY("url-query")
    }
}
