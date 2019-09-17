package org.netkernel.lang.kotlin.xunit

import org.netkernel.lang.kotlin.dsl.BuilderNode
import org.netkernel.lang.kotlin.dsl.declarativeRequest.DeclarativeRequestContainerImpl
import org.netkernel.lang.kotlin.dsl.declarativeRequest.Request
import org.netkernel.lang.kotlin.dsl.initNode
import org.netkernel.lang.kotlin.inline.*
import org.netkernel.lang.kotlin.knkf.Identifier
import org.netkernel.mod.hds.HDSFactory
import org.netkernel.mod.hds.IHDSDocument
import org.netkernel.mod.hds.IHDSMutator

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
}

class Test(builder: IHDSMutator): BuilderNode(builder, "test") {
    private val setupRequestFactory = DeclarativeRequestContainerImpl(builder, "setup")
    private val requestFactory = DeclarativeRequestContainerImpl(builder, "request")
    private val teardownRequestFactory = DeclarativeRequestContainerImpl(builder, "teardown")

    fun setup(identifier: Identifier, init: Request.() -> Unit = {}) = setupRequestFactory.request(identifier, init)
    fun setup(identifier: String, init: Request.() -> Unit = {}) = setup(Identifier(identifier), init)
    fun setupInlineSource(init: Request.() -> Unit = {}, lambda: InlineSourceLambda) = setupRequestFactory.inlineSource(init, lambda)
    fun setupInlineSink(init: Request.() -> Unit = {}, lambda: InlineSinkLambda) = setupRequestFactory.inlineSink(init, lambda)
    fun setupInlineExists(init: Request.() -> Unit = {}, lambda: InlineExistsLambda) = setupRequestFactory.inlineExists(init, lambda)
    fun setupInlineNew(init: Request.() -> Unit = {}, lambda: InlineNewLambda) = setupRequestFactory.inlineNew(init, lambda)
    fun setupInlineDelete(init: Request.() -> Unit = {}, lambda: InlineDeleteLambda) = setupRequestFactory.inlineDelete(init, lambda)

    fun request(identifier: Identifier, init: Request.() -> Unit = {}) = requestFactory.request(identifier, init)
    fun request(identifier: String, init: Request.() -> Unit = {}) = request(Identifier(identifier), init)
    fun requestInlineSource(init: Request.() -> Unit = {}, lambda: InlineSourceLambda) = requestFactory.inlineSource(init, lambda)
    fun requestInlineSink(init: Request.() -> Unit = {}, lambda: InlineSinkLambda) = requestFactory.inlineSink(init, lambda)
    fun requestInlineExists(init: Request.() -> Unit = {}, lambda: InlineExistsLambda) = requestFactory.inlineExists(init, lambda)
    fun requestInlineNew(init: Request.() -> Unit = {}, lambda: InlineNewLambda) = requestFactory.inlineNew(init, lambda)
    fun requestInlineDelete(init: Request.() -> Unit = {}, lambda: InlineDeleteLambda) = requestFactory.inlineDelete(init, lambda)

    fun teardown(identifier: Identifier, init: Request.() -> Unit = {}) = teardownRequestFactory.request(identifier, init)
    fun teardown(identifier: String, init: Request.() -> Unit = {}) = teardown(Identifier(identifier), init)
    fun teardownInlineSource(init: Request.() -> Unit = {}, lambda: InlineSourceLambda) = teardownRequestFactory.inlineSource(init, lambda)
    fun teardownInlineSink(init: Request.() -> Unit = {}, lambda: InlineSinkLambda) = teardownRequestFactory.inlineSink(init, lambda)
    fun teardownInlineExists(init: Request.() -> Unit = {}, lambda: InlineExistsLambda) = teardownRequestFactory.inlineExists(init, lambda)
    fun teardownInlineNew(init: Request.() -> Unit = {}, lambda: InlineNewLambda) = teardownRequestFactory.inlineNew(init, lambda)
    fun teardownInlineDelete(init: Request.() -> Unit = {}, lambda: InlineDeleteLambda) = teardownRequestFactory.inlineDelete(init, lambda)

    fun assert(init: (Assert.() -> Unit)) = initNode(Assert(builder), init)
}

class Assert(builder: IHDSMutator): BuilderNode(builder, "assert") {
    fun isTrue() {
        builder.addNode("true", null)
    }
    fun isFalse() {
        builder.addNode("false", null)
    }
    fun isNull() {
        builder.addNode("null", null)
    }
    fun isNotNull() {
        builder.addNode("notNull", null)
    }
    fun stringEquals(value: String) {
        builder.addNode("stringEquals", value)
    }
}
