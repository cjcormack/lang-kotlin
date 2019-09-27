#MARKDOWN

# NetKernel Foundation API for Kotlin

The NetKernel Foundation API for Kotlin (KNKF) provides Kotlin specific extensions to the NetKernel Foundation API (NKF).
In particular, it supplies:

* Base classes for endpoints—e.g. accessors and transreptors,
* An API for building and issuing NetKernel requests.

## Endpoints

If you're building NetKernel endpoints in Kotlin, then there are a range of base classes provided in the 
[org.netkernel.lang.kotlin.knkf.endpoints](../../../dokka/urn.org.netkernel.lang.kotlin/org.netkernel.lang.kotlin.knkf.endpoints/index.html)
package.

### Accessors

Accessors implemented in Kotlin should be written as classes that extend the
[KotlinAccessor](../../../dokka/urn.org.netkernel.lang.kotlin/org.netkernel.lang.kotlin.knkf.endpoints/-kotlin-accessor/index.html) base class,
which provides overrideable functions for SOURCE, SINK, EXISTS, DELETE and NEW. These functions supply an appropriate
RequestContext for the type of the request to match both response type and availability of primary representation
base on the [request verb semantics](doc:physicalreference:request:verbs).

### Transreptors

Transreptors implemented in Kotlin should be written as classes that extend the
[KotlinTransreptor](../../../dokka/urn.org.netkernel.lang.kotlin/org.netkernel.lang.kotlin.knkf.endpoints/-kotlin-transreptor/index.html)
base class, which requires the implementation of
[TransreptorRequestContext<T>.onTransrept()](../../../dokka/urn.org.netkernel.lang.kotlin/org.netkernel.lang.kotlin.knkf.endpoints/-kotlin-transreptor/on-transrept.html).
There are two generic types that are required:

* **F**—this is the 'from' representation for the transreptor. If this is a *-to-one or n-to-m transreptor, then you should
use **Any**. The class provided in the **fromRepresentation** call is required to meet the generic type (i.e. be a **Class<F>**).
* **T**—this is the 'to' representation for the transreptor. If this is a one-to-* or n-to-m transreptor, then you should
use **Any**. The class provided in the **toRepresentation** call is required to meet the generic type, and the response
set in **onTransrept** must be of type **T**.

## Request Context

A RequestContext is a Kotlin specific wrapper around an INKFRequestContext, which supplies type-safe functions for creating
and issuing requests, and setting responses. The exact available functions that are available depends on the context in
which the RequestContext has been provided, and the availability falls into three main areas:

1. General—these are the functions that are available in every context, and are defined as either local or extension functions on
the [RequestContext](../../../dokka/urn.org.netkernel.lang.kotlin/org.netkernel.lang.kotlin.knkf.context/-request-context/index.html) class.
2. Contexts with responses—these are functions that are available in contexts where setting a response is semantically
valid (e.g. on a SOURCE request, but not on a SINK request), and are defined as either local or extension functions on
the [RequestContextWithResponse](../../../dokka/urn.org.netkernel.lang.kotlin/org.netkernel.lang.kotlin.knkf.context/-request-context-with-response/index.html)
class.
3 Contexts with a primary representation—these are functions that are available when there is a primary representation
attached to the request (e.g. in a SINK request), and are defined as either local or extension functions on
the [RequestContextWithPrimary](../../../dokka/urn.org.netkernel.lang.kotlin/org.netkernel.lang.kotlin.knkf.context/-request-context-with-primary.html)
interface (its functions are made available to implementing concrete RequestContexts using [delegation](https://kotlinlang.org/docs/reference/delegation.html)).

The specific RequestContexts for each context are as follows:

* KotlinAccessor:
    * **onSource**: [SourceRequestContext](../../../dokka/urn.org.netkernel.lang.kotlin/org.netkernel.lang.kotlin.knkf.context/-source-request-context/index.html),
    * **onSink**: [SinkRequestContext](../../../dokka/urn.org.netkernel.lang.kotlin/org.netkernel.lang.kotlin.knkf.context/-sink-request-context/index.html),
    * **onExists**: [ExistsRequestContext](../../../dokka/urn.org.netkernel.lang.kotlin/org.netkernel.lang.kotlin.knkf.context/-exists-request-context/index.html),
    * **onDelete**: [DeleteRequestContext](../../../dokka/urn.org.netkernel.lang.kotlin/org.netkernel.lang.kotlin.knkf.context/-delete-request-context/index.html),
    * **onNew**: [NewRequestContext](../../../dokka/urn.org.netkernel.lang.kotlin/org.netkernel.lang.kotlin.knkf.context/-new-request-context/index.html),
* KotlinTransreptor:
    * **onTransrept**: [TransreptorRequestContext](../../../dokka/urn.org.netkernel.lang.kotlin/org.netkernel.lang.kotlin.knkf.context/-transreptor-request-context/index.html)
* KotlinTransparentOverlay:
    * **onRequest**: [TransparentOverlayRequestContext](../../../dokka/urn.org.netkernel.lang.kotlin/org.netkernel.lang.kotlin.knkf.context/-transparent-overlay-request-context/index.html)

### Making Requests

Some examples of using a RequestContext to create requests:

#### Simple SOURCE request

{kotlin}
val config = source<IHDSDocument>("res:/etc/MyConfig.xml")
{/kotlin}

#### SOURCE with arguments by value

{kotlin}
val myValue = source<String>("active:toUpper") {
    argumentByValue("operand")
}
{/kotlin}

#### SOURCE with argument from inner request

{kotlin}
val myValue = source<String>("active:toUpper") {
    argumentByRequest("operand") {
        sourceRequest<Any>("active:freemarker") {
            argumentByValue("operator", "Hello ${name}")
            argumentByValue("name", "world")
        }
    }
}
{/kotlin}

#### Generic request

{kotlin}
val myValue = issue<String> {
    // we need to specify generic types for primary representation and response
    request<Any,String>("active:toUpper") {
        verb(Verb.SOURCE)
        argumentByValue("operand", "hello world")
    }
}
{/kotlin}

#### Transrept

{kotlin}
val myValue = transrept<String>(12345)
{/kotlin}

### Setting a Response

{kotlin}
response {
    source("active:toUpper") {
        argumentByValue("operand", "Hello World")
    }
}
{/kotlin}
