package org.netkernel.lang.kotlin.tests.inlineRequests

import org.netkernel.lang.kotlin.knkf.Identifier
import org.netkernel.lang.kotlin.knkf.context.sourcePrimary
import org.netkernel.lang.kotlin.util.firstValue
import org.netkernel.lang.kotlin.xunit.XUnitTest
import org.netkernel.lang.kotlin.xunit.XUnitTestList
import org.netkernel.lang.kotlin.dsl.xunit.Test
import org.netkernel.mod.hds.IHDSDocument

class TestList: XUnitTestList() {
    @XUnitTest("Inline SOURCE Test")
    fun Test.inlineSourceTest()  {
        requestInlineSource {
            response {
                "Hello World!"
            }
        }
        assert {
            stringEquals("Hello World!")
        }
    }

    @XUnitTest("Inline SINK Test")
    fun Test.inlineSinkTest() {
        requestInlineSink {}
        assert {
            isNull()
        }
    }

    @XUnitTest("Inline SINK Test with primary")
    fun Test.inlineSinkTestWithPrimary() {
        requestInlineSink(init = {
            argument("primary") {
                literalHds {
                    node("item") {
                        node("id", "HelloWorld")
                    }
                }
            }
        }) {
            val item = sourcePrimary<IHDSDocument>()
            println(item.reader.firstValue<String>("/item/id"))
        }

        assert {
            isNull()
        }
    }

    @XUnitTest("Inline EXISTS Test (true)")
    fun Test.inlineExistsTrueTest() {
        requestInlineExists {
            response {
                true
            }
        }
        assert {
            isTrue()
        }
    }

    @XUnitTest("Inline EXISTS Test (false)")
    fun Test.inlineExistsFalseTest() {
        requestInlineExists {
            response {
                false
            }
        }
        assert {
            isFalse()
        }
    }

    @XUnitTest("Inline DELETE Test (true)")
    fun Test.inlineDeleteTrueTest() {
        requestInlineDelete {
            response {
                true
            }
        }
        assert {
            isTrue()
        }
    }

    @XUnitTest("Inline DELETE Test (false)")
    fun Test.inlineDeleteFalseTest() {
        requestInlineDelete {
            response {
                false
            }
        }
        assert {
            isFalse()
        }
    }

    @XUnitTest("Inline NEW Test")
    fun Test.inlineNewTest() {
        requestInlineNew {
            response {
                Identifier("HelloWorld")
            }
        }
        assert {
            stringEquals("HelloWorld")
        }
    }

    @XUnitTest("Inline NEW Test with Primary")
    fun Test.inlineNewTestWithPrimary() {
        val req = requestInlineNew{
            response {
                val item = sourcePrimary<IHDSDocument>()

                Identifier(item.reader.firstValue("/item/id"))
            }
        }
        req.argument("primary", "helloWorld")

        assert {
            stringEquals("HelloWorld")
        }
    }
}
