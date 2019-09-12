#MARKDOWN

# Type-safe builders

[Type-safe builders](https://kotlinlang.org/docs/reference/type-safe-builders.html), which are effectively DSLs, allow
construction of data structures in a type-safe manner. It is quite easy to create your own, or extend the provided
type-safe builders.

## HDS

[HDS](book:mod:hds) structures can easily be constructed in your Kotlin code:

{kotlin}
val myHds = hds {
    node("config") {
        node("host", "https://example.com")
        node("port", 8443)
        node("credentials") {
            node("username", "joanna.bloggs")
            node("password", "correct horse battery staple")
        }
    }
}
{/kotlin}

See the [Dokka docs](../../../dokka/lang-kotlin/org.netkernel.lang.kotlin.dsl.hds/index.html) for more details.

## Mapper Config

Configuration for a [mapper overlay](doc:logicalreference:module:standard:logical:mapper) can be achieved in Kotlin:

{kotlin}
val myConfig = mapper {
    endpoint {
        activeGrammar("active:my-service") {
            argument("operand") {
                representation(String::class.java)
            }
            argument("config", min = 0, max = 1)
        }
        request("active:other-service") {
            representation(IHDSDocument::class.java)
            argument("enableFeature") {
                literal(true)
            }
            varargs()
        }
    }
}
{/kotlin}

Various bits of Dokka documentation for the related builders are available:

* [Mapper config](../../../dokka/lang-kotlin/org.netkernel.lang.kotlin.dsl.mapper/index.html),
* [REST Overlay Meta data](../../../dokka/lang-kotlin/org.netkernel.lang.kotlin.dsl.rest-overlay/index.html),
* [Grammars](../../../dokka/lang-kotlin/org.netkernel.lang.kotlin.dsl.grammar/index.html).
