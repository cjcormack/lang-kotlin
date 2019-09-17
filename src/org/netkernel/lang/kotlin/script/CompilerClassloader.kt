package org.netkernel.lang.kotlin.script

import org.netkernel.layer0.nkf.INKFRequestContext
import org.netkernel.layer0.util.DynamicURLClassLoader
import org.netkernel.layer0.util.SpaceClassLoader
import org.netkernel.urii.ClassLoaderWithExports
import java.io.File
import java.net.URI
import java.net.URL
import java.net.URLClassLoader

internal fun INKFRequestContext.getKotlinCompilerClassLoader(): URLClassLoader {
    val urls = ArrayList<URL>()

    generateSequence(this.kernelContext.thisKernelRequest.requestScope, { it.parent }).forEach { scope ->
        val spaceClassLoader = scope.space.classLoader
        if (spaceClassLoader != null) {
            urls.addAll(scope.space.classLoader.getJarUrls())
        }
    }

    return URLClassLoader(urls.toTypedArray())
}

internal fun ClassLoader.getJarUrls(): List<URL> {
    val urls = when (this) {
        is URLClassLoader -> this.urLs.toList()
        is DynamicURLClassLoader -> this.getUrls()
        else -> {
            emptyList()
        }
    }.toMutableList()

    val parentCl = this.parent
    // TODO workout a neater way of prevent this infinite recursion!
    if (parentCl != null && !parentCl.javaClass.name.contains("jdk")) {
        urls.addAll(parentCl.getJarUrls())
    }

    if (this is SpaceClassLoader) {
        this.getImports().forEach {
            urls.addAll(it.getJarUrls())
        }
        urls.addAll(this.parentClassLoader?.getJarUrls() ?: emptyList())
    }

    return urls
}

// this is in a different extension to hide how hideous it from the main getJarUrls.
internal fun DynamicURLClassLoader.getUrls(): List<URL> {
    val dynamicCL = generateSequence<Class<*>>(this.javaClass) { it.superclass }.filter { it ==  DynamicURLClassLoader::class.java }.first()

    // horrible, HORRIBLE hack!
    val mURLsField = dynamicCL.getDeclaredField("mURLs")
    mURLsField.isAccessible = true
    val mURLsValue = mURLsField.get(this)

    check(mURLsValue is List<*>)

    return mURLsValue.map { url ->
        checkNotNull(url)
        check(url.javaClass.canonicalName == "org.netkernel.layer0.util.DynamicURLClassLoader.ClassPathElement")

        val baseUrlField = url.javaClass.getDeclaredField("mBaseURL")
        val baseUrlValue = baseUrlField.get(url)

        check(baseUrlValue is String)

        val f = if (baseUrlValue.startsWith("file:") && baseUrlValue.endsWith("/")) {
            File(URI.create(baseUrlValue))
        } else if (baseUrlValue.startsWith("jar:file:") && baseUrlValue.endsWith("!/")) {
            val fileURI = baseUrlValue.substring(4, baseUrlValue.length - 2)
            File(URI.create(fileURI))
        } else {
            throw Exception("Not supported URL scheme ($baseUrlValue)")
        }

        check(f.exists())
        f.toURI().toURL()
    }
}

internal fun SpaceClassLoader.getImports(): List<ClassLoaderWithExports> {
    val dynamicCL = generateSequence<Class<*>>(this.javaClass) { it.superclass }.filter { it ==  SpaceClassLoader::class.java }.first()

    // another horrible hack!
    val mImportsField = dynamicCL.getDeclaredField("mImports")
    mImportsField.isAccessible = true

    val mImportsValue = mImportsField.get(this)
    check(mImportsValue is Array<*>)

    return mImportsValue.map {
        if (it is ClassLoaderWithExports) {
            it
        } else {
            throw Exception("Unexpectedly got a list with something not a ClassLoaderWithExports from mImports")
        }
    }.toList()
}
