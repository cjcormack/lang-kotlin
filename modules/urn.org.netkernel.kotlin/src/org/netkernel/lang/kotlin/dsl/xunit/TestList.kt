package org.netkernel.lang.kotlin.dsl.xunit

import org.netkernel.lang.kotlin.dsl.BuilderNode
import org.netkernel.lang.kotlin.dsl.declarativeRequest.DeclarativeRequestContainerImpl
import org.netkernel.lang.kotlin.dsl.initNode
import org.netkernel.lang.kotlin.knkf.Identifier
import org.netkernel.lang.kotlin.dsl.declarativeRequest.Request
import org.netkernel.lang.kotlin.inline.InlineSourceLambda
import org.netkernel.mod.hds.HDSFactory
import org.netkernel.mod.hds.IHDSDocument

fun testlist(init: TestList.() -> Unit): IHDSDocument {
    val testList = initNode(TestList(), init)

    return testList.builder.toDocument(false)
}

class TestList: BuilderNode(HDSFactory.newDocument(), "testlist") {
    private val assertDefinitionInlineRequestFactory = DeclarativeRequestContainerImpl(builder, "assertDefinition")

    fun test(name: String? = null, init: Test.() -> Unit) = initNode(Test(builder)) {
        if (name != null) {
            builder.addNode("@name", name)
        }
        init()
    }

    fun import(identifier: Identifier) {
        builder.addNode("import", identifier.identifier)
    }

    fun import(identifier: String) {
        import(Identifier(identifier))
    }

    fun assertDefinition(name: String, identifier: String, init: Request.() -> Unit): AssertDefinition {
        val request = initNode(Request(builder, "assertDefinition")) {
            builder.addNode("@name", name)
            builder.addNode("identifier", identifier)
            init()
        }

        return AssertDefinition(name, request)
    }

    fun assertDefinition(name: String, identifier: Identifier, init: Request.() -> Unit) = assertDefinition(name, identifier.identifier, init)

    fun inlineAssertDefinition(name: String, init: Request.() -> Unit = {}, lambda: InlineSourceLambda): AssertDefinition {
        val request =  assertDefinitionInlineRequestFactory.inlineSource({
            builder.addNode("@name", name)
            init()
        }, lambda)

        return AssertDefinition(name, request)
    }
}

class AssertDefinition(val name: String, val request: Request)
