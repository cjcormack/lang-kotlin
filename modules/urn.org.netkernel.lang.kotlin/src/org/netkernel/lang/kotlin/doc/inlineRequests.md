#MARKDOWN

{endpoint}KotlinInlineRequest{/endpoint}

Sometimes it is useful to be able to specify a NetKernel request inline in your Kotlin
code. For example, if you're building a declarative request using a DSL.

{kotlin}
val myConfig = mapper {
    endpoint {
        activeGrammar("active:my-service") {
            argument("operand") {
                representation(String::class.java)
            }
            argument("config", min = 0, max = 1)
        }
        inlineSource(init = {
            argument("config", "arg:config")
        }) {
            response {
                "Hello World!"
            }
        }
    }
}
{/kotlin}

The above **inlineSource** function in the declarative request DSL creates a request to **active:inlineRequest**
with an operator argument which is an [InlineRequest](../../../dokka/lang-kotlin/org.netkernel.lang.kotlin.inline/-inline-request/index.html)
wrapping the lambda (the argument is specified as an HDS2 literal with the **InlineRequest** serialized). The 

## SOURCE

For a SOURCE request, a lambda matching the typealias of
[InlineSourceLambda](../../../dokka/lang-kotlin/org.netkernel.lang.kotlin.inline/-inline-source-lambda.html)
should be used.

## SINK

For a SINK request, a lambda matching the typealias of
[InlineSinkLambda](../../../dokka/lang-kotlin/org.netkernel.lang.kotlin.inline/-inline-sink-lambda.html)
should be used.

## EXISTS

For a EXISTS request, a lambda matching the typealias of
[InlineExistsLambda](../../../dokka/lang-kotlin/org.netkernel.lang.kotlin.inline/-inline-exists-lambda.html)
should be used.

## DELETE

For a DELETE request, a lambda matching the typealias of
[InlineDeleteLambda](../../../dokka/lang-kotlin/org.netkernel.lang.kotlin.inline/-inline-delete-lambda.html)
should be used.

## NEW

For a NEW request, a lambda matching the typealias of
[InlineNewLambda](../../../dokka/lang-kotlin/org.netkernel.lang.kotlin.inline/-inline-new-lambda.html)
should be used.
