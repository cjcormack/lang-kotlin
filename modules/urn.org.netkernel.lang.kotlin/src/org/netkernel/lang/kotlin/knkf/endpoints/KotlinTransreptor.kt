package org.netkernel.lang.kotlin.knkf.endpoints

import org.netkernel.lang.kotlin.knkf.context.GeneralRequestContext
import org.netkernel.lang.kotlin.knkf.context.RequestContext
import org.netkernel.lang.kotlin.knkf.context.TransreptorRequestContext
import org.netkernel.layer0.nkf.INKFRequestContext
import org.netkernel.module.standard.endpoint.StandardTransreptorImpl

abstract class KotlinTransreptor<F,T>: StandardTransreptorImpl() {
    init {
        declareThreadSafe()
    }

    final override fun declareThreadSafe() {
        super.declareThreadSafe()
    }

    final override fun postCommission(context: INKFRequestContext) {
        GeneralRequestContext(context).postCommission()
    }

    final override fun preDecommission(context: INKFRequestContext) {
        GeneralRequestContext(context).preDecommission()
    }

    final override fun onTransrept(context: INKFRequestContext?) {
        checkNotNull(context)

        TransreptorRequestContext<T>(context).onTransrept()
    }

    fun fromRepresentation(aRepresentation: Class<F>) {
        super.declareFromRepresentation(aRepresentation)
    }

    fun toRepresentation(aRepresentation: Class<T>) {
        super.declareToRepresentation(aRepresentation)
    }

    @Deprecated("Deprecated in favour of generic version", ReplaceWith("fromRepresentation(aRepresentation)"))
    final override fun declareFromRepresentation(aRepresentation: Class<*>?) {
        super.declareFromRepresentation(aRepresentation)
    }

    @Deprecated("Deprecated in favour of generic version", ReplaceWith("toRepresentation(aRepresentation)"))
    final override fun declareToRepresentation(aRepresentation: Class<*>?) {
        super.declareToRepresentation(aRepresentation)
    }

    open fun RequestContext.postCommission() {}

    open fun RequestContext.preDecommission() {}

    abstract fun TransreptorRequestContext<T>.onTransrept()

}
