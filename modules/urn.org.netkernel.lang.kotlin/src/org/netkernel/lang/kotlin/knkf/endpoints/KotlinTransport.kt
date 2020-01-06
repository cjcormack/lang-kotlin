package org.netkernel.lang.kotlin.knkf.endpoints

import org.netkernel.lang.kotlin.knkf.context.GeneralRequestContext
import org.netkernel.lang.kotlin.knkf.context.RequestContext
import org.netkernel.lang.kotlin.knkf.context.TransportRequestContext
import org.netkernel.layer0.nkf.INKFRequestContext
import org.netkernel.module.standard.endpoint.StandardTransportImpl

abstract class KotlinTransport: StandardTransportImpl() {
    val transportContext
        get() = TransportRequestContext(super.getTransportContext())

    override fun postCommission(aContext: INKFRequestContext) {
        GeneralRequestContext(aContext).postCommission()
    }

    override fun preDecommission(aContext: INKFRequestContext) {
        GeneralRequestContext(aContext).preDecommission()
    }

    @Deprecated("NKF context should be used via property", ReplaceWith("transportContext"))
    override fun getTransportContext(): INKFRequestContext {
        return super.getTransportContext()
    }

    abstract fun RequestContext.postCommission()
    abstract fun RequestContext.preDecommission()
}
