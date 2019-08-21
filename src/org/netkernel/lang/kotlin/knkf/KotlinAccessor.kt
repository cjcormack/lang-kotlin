package org.netkernel.lang.kotlin.knkf

import org.netkernel.layer0.nkf.INKFRequestContext
import org.netkernel.module.standard.endpoint.StandardAccessorImpl

abstract class KotlinAccessor: StandardAccessorImpl() {
    final override fun onNew(context: INKFRequestContext?) {
        checkNotNull(context)

        RequestContext(context).onNew()
    }

    final override fun onSource(context: INKFRequestContext?) {
        checkNotNull(context)

        RequestContext(context).onSource()
    }

    final override fun onDelete(context: INKFRequestContext?) {
        checkNotNull(context)

        RequestContext(context).onDelete()
    }

    final override fun onSink(context: INKFRequestContext?) {
        checkNotNull(context)

        RequestContext(context).onSink()
    }

    final override fun onExists(context: INKFRequestContext?) {
        checkNotNull(context)

        RequestContext(context).onExists()
    }

    open fun RequestContext.onNew() {
        NotImplementedError()
    }

    open fun RequestContext.onSource() {
        NotImplementedError()
    }

    open fun RequestContext.onDelete() {
        NotImplementedError()
    }

    open fun RequestContext.onSink() {
        NotImplementedError()
    }

    open fun RequestContext.onExists() {
        NotImplementedError()
    }
}

