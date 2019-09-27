#MARKDOWN

# XUnit Tests

The NetKotlin module comes with some helpers for building XUnit TestLists. By subclassing
[KotlinTestList](../../../dokka/urn.org.netkernel.lang.kotlin/org.netkernel.lang.kotlin.xunit/-kotlin-test-list/index.html)
and annotating test methods with [KotlinTest](../../../dokka/urn.org.netkernel.lang.kotlin/org.netkernel.lang.kotlin.xunit/-kotlin-test/index.html).
The method should either take a [Test](../../../dokka/urn.org.netkernel.lang.kotlin/org.netkernel.lang.kotlin.xunit/-test/index.html)
as a receiver, or as the first parameter.

{kotlin}
class InlineRequestsTestList: KotlinTestList() {
    @KotlinTest("Inline SOURCE Test")
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

    @KotlinTest("Inline SINK Test")
    fun Test.inlineSinkTest() {
        requestInlineSink {}
        assert {
            isNull()
        }
    }

    @KotlinTest("Inline SINK Test with primary")
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

    @KotlinTest("Inline EXISTS Test (true)")
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

    @KotlinTest("Inline EXISTS Test (false)")
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

    @KotlinTest("Inline DELETE Test (true)")
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

    @KotlinTest("Inline DELETE Test (false)")
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

    @KotlinTest("Inline NEW Test")
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

    @KotlinTest("Inline NEW Test with Primary")
    fun Test.inlineNewTestWithPrimary() {
        requestInlineNew(init = {
            argument("primary") {
                literalHds {
                    node("item") {
                        node("id", "HelloWorld")
                    }
                }
            }
        }) {
            response {
                val item = sourcePrimary<IHDSDocument>()

                Identifier(item.reader.firstValue("/item/id"))
            }
        }

        assert {
            stringEquals("HelloWorld")
        }
    }
}
{/kotlin}
