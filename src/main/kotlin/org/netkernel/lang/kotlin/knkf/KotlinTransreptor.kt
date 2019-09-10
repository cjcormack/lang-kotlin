package org.netkernel.lang.kotlin.knkf

import org.netkernel.lang.kotlin.knkf.context.TransreptorRequestContext
import org.netkernel.layer0.nkf.INKFRequestContext
import org.netkernel.module.standard.endpoint.StandardTransreptorImpl

abstract class KotlinTransreptor<F,T>: StandardTransreptorImpl() {
    final override fun onTransrept(context: INKFRequestContext?) {
        checkNotNull(context)

        TransreptorRequestContext<T>(context).onTransrept()
    }

    abstract fun TransreptorRequestContext<T>.onTransrept()

    fun fromRepresentation(aRepresentation: Class<F>) {
        super.declareFromRepresentation(aRepresentation)
    }

    fun toRepresentation(aRepresentation: Class<T>) {
        super.declareToRepresentation(aRepresentation)
    }

    @Deprecated("Deprecated in favour of generic version", ReplaceWith("fromRepresentation(aRepresentation)"))
    override fun declareFromRepresentation(aRepresentation: Class<*>?) {
        super.declareFromRepresentation(aRepresentation)
    }

    @Deprecated("Deprecated in favour of generic version", ReplaceWith("toRepresentation(aRepresentation)"))
    override fun declareToRepresentation(aRepresentation: Class<*>?) {
        super.declareToRepresentation(aRepresentation)
    }
}
