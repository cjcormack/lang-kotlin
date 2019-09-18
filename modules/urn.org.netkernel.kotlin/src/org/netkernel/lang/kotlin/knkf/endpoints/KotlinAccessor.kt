package org.netkernel.lang.kotlin.knkf.endpoints

import org.netkernel.lang.kotlin.knkf.context.*
import org.netkernel.layer0.nkf.INKFRequestContext
import org.netkernel.module.standard.endpoint.StandardAccessorImpl

abstract class KotlinAccessor: StandardAccessorImpl() {
    final override fun onNew(context: INKFRequestContext) {
        NewRequestContext(context).onNew()
    }

    final override fun onSource(context: INKFRequestContext) {
        SourceRequestContext(context).onSource()
    }

    final override fun onDelete(context: INKFRequestContext) {
        DeleteRequestContext(context).onDelete()
    }

    final override fun onSink(context: INKFRequestContext) {
        SinkRequestContext(context).onSink()
    }

    final override fun onExists(context: INKFRequestContext) {
        ExistsRequestContext(context).onExists()
    }

    final override fun declareThreadSafe() {
        super.declareThreadSafe()
    }

    open fun NewRequestContext.onNew() {
        throw NotImplementedError()
    }

    open fun SourceRequestContext.onSource() {
        throw NotImplementedError()
    }

    open fun DeleteRequestContext.onDelete() {
        throw NotImplementedError()
    }

    open fun SinkRequestContext.onSink() {
        throw NotImplementedError()
    }

    open fun ExistsRequestContext.onExists() {
        throw NotImplementedError()
    }
}

