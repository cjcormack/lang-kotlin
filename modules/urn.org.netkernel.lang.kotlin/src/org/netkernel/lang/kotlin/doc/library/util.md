#MARKDOWN

# Utilities

There are various utilities available in the
[org.netkernel.lang.kotlin.util package](../../../dokka/lang-kotlin/org.netkernel.lang.kotlin.util/index.html).

## IHDSReader extension functions

There are a couple of extension functions for IHDSReader. See the
[Dokka docs](../../../dokka/lang-kotlin/org.netkernel.lang.kotlin.util/org.netkernel.mod.hds.-i-h-d-s-reader/index.html)
for more information.

### Type-safe getFirstValue

Getting a value in a type-safe way can be done via:

{kotlin}
val host: String
val port: Int

source<IHDSDocument>("res:/etc/MyConfig.xml").reader.let { reader ->
    // 'this' is the RequestContext (and is optional), and is used to allow an automatically transrept
    // the value of '/config/host' if it isn't a String.
    host = reader.firstValue("/config/host", this)
    // no need to supply a <T> as it is inferred from the 'val'
    port = reader.firstValue("/config/port", default = 8443)
}

println("Connecting to $host:$port")
{/kotlin}

### Type-safe getValues

Getting all values that match a path in a type-safe way can be done with:

{kotlin}
val config = source<IHDSDocument>("res:/etc/MyConfig.xml")

// 'this' is the RequestContext (and is optional), and is used to allow an automatically transrept
// each value that is not a String.
val configValues = config.reader.values<String>("/config/settings/value")
{/kotlin}
