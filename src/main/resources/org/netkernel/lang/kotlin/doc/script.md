#MARKDOWN

{endpoint}KotlinScriptRuntime{/endpoint}

The **active:kotlinScript** service runs a compiled Kotlin Script program. The script that runs will be a sub-type of
[NetKernelKotlinScript](../../../dokka/lang-kotlin/org.netkernel.lang.kotlin.script/-net-kernel-kotlin-script/index.html).

The **context** property will be of the appropriate sub-type for the request's verb. There are two approaches for handling
the **context** property:

1. Use the appropriate **on{VERB}** function, which takes a function literal with the appropriate RequestContext as the receiver.
The lambda will only be executed if the request is of the correct verb (e.g. **onSource** will not be evaluated if this is
a SINK request). For example:
{kotlin}
onSource {
    response {
        "Hello World"
    }
}

onExists {
    response(true)
}
{/kotlin}
2. Using a type check. The following example is functionally equivalent to the previous example:
{kotlin}
if (context is SourceRequestContext) {
    context.with {
        response {
            "Hello World"
        }
    }
} else if (context is ExistsRequestContext) {
    context.with {
        response(true)
    }
}
{/kotlin}

The Kotlin Script program will have access to the [Standard library](doc:lang:kotlin:library). If you're writing
Kotlin Scripts in an IDE, then you may want to take a look at the [IDE Support documentation](doc:lang:kotlin:ide)
in order to get working syntax highlighting.

# SOURCE

For a **SOURCE** request, the **context** will be a [SourceRequestContext](../../../dokka/lang-kotlin/org.netkernel.lang.kotlin.knkf.context/-source-request-context/index.html)
and the type-safe function is [onSource(lazyOnSource: SourceRequestContext.() -> Unit)](../../../dokka/lang-kotlin/org.netkernel.lang.kotlin.script/-net-kernel-kotlin-script/on-source.html).

# SINK

For a **SINK** request, the **context** will be a [SinkRequestContext](../../../dokka/lang-kotlin/org.netkernel.lang.kotlin.knkf.context/-sink-request-context/index.html)
and the type-safe function is [onSink(lazyOnSink: SinkRequestContext.() -> Unit)](../../../dokka/lang-kotlin/org.netkernel.lang.kotlin.script/-net-kernel-kotlin-script/on-sink.html).

# EXISTS

For a **EXISTS** request, the **context** will be a [ExistsRequestContext](../../../dokka/lang-kotlin/org.netkernel.lang.kotlin.knkf.context/-exists-request-context/index.html)
and the type-safe function is [onExists(lazyOnExists: ExistsRequestContext.() -> Unit)](../../../dokka/lang-kotlin/org.netkernel.lang.kotlin.script/-net-kernel-kotlin-script/on-exists.html).

# NEW

For a **NEW** request, the **context** will be a [NewRequestContext](../../../dokka/lang-kotlin/org.netkernel.lang.kotlin.knkf.context/-new-request-context/index.html)
and the type-safe function is [onNew(lazyOnNew: NewRequestContext.() -> Unit)](../../../dokka/lang-kotlin/org.netkernel.lang.kotlin.script/-net-kernel-kotlin-script/on-new.html).

# DELETE

For a **DELETE** request, the **context** will be a [DeleteRequestContext](../../../dokka/lang-kotlin/org.netkernel.lang.kotlin.knkf.context/-delete-request-context/index.html)
and the type-safe function is [onDelete(lazyOnDelete: DeleteRequestContext.() -> Unit)](../../../dokka/lang-kotlin/org.netkernel.lang.kotlin.script/-net-kernel-kotlin-script/on-delete.html).

