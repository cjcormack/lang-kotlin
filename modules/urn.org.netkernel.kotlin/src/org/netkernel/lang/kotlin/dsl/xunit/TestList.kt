package org.netkernel.lang.kotlin.dsl.xunit

import org.netkernel.lang.kotlin.dsl.BuilderNode
import org.netkernel.lang.kotlin.dsl.initNode
import org.netkernel.lang.kotlin.knkf.Identifier
import org.netkernel.mod.hds.HDSFactory
import org.netkernel.mod.hds.IHDSDocument

fun testlist(init: TestList.() -> Unit): IHDSDocument {
    val testList = initNode(TestList(), init)

    return testList.builder.toDocument(false)
}

class TestList: BuilderNode(HDSFactory.newDocument(), "testlist") {
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
}
