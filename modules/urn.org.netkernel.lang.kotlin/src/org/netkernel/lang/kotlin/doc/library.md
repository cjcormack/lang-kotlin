#MARKDOWN

# NetKernel's Kotlin Library

As well as access to all of the same APIs as those available in NetKernel code written in Java, the Kotlin module comes
with Kotlin specific extensions and utilities. These fall into three main categories:

1. [NetKernel Foundation API for Kotlin (KNKF)](doc:lang:kotlin:library:knkf)—this is an extension of the standard
NetKernel Foundation API (NKF) and supplies both Kotlin specific base classes for writing endpoints, and new request
context objects which provide type-safe builders for issuing requests and setting responses.
2. [Type-safe builders (DSLs)](doc:lang:kotlin:library:dsls)—this is a collection of functions which provide type-safe ways
of building various NetKernel structures, for example, HDS and a Mapper Overlay configuration.
3. [Utilities](doc:lang:kotlin:library:util)—this is a collection of miscellaneous 

## Usage

In order to use the kotlin library, you will need the Kotlin module on your compile classpath (or use [Kotlin Script](doc:lang:kotlin:script))
and imported into your NetKernel module.

### Warning

Some functionality uses **inline functions** which will get compiled into your code (generally these are used
to allow access to 'erased' generic types at runtime. These functions have been written as wrappers around non-inline
functions and so there should not be an issue if running a newer version of the Kotlin module at runtime to that used
for compilation. Below is an example of such an inline function and its corresponding non-inline version:

{kotlin}
inline fun <reified R> RequestContext.source(identifier: String, noinline init: SourceRequest<R>.() -> Unit = {}): R {
    return source(Identifier(identifier), R::class.java, init)
}

fun <R> RequestContext.source(identifier: Identifier, representationClass: Class<R>, init: SourceRequest<R>.() -> Unit = {}): R {
    return sourceRequest(identifier, representationClass, init).issue()
}
{/kotlin}

## Dokka (Documentation)

Documentation has been generated with [Dokka](https://github.com/Kotlin/dokka) for the Kotlin module, it is available at
[/dokka/lang-kotlin/index.html](../../../dokka/urn.org.netkernel.lang.kotlin/index.html).
