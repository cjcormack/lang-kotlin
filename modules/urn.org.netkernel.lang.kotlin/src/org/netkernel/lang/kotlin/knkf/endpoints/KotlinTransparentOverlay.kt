package org.netkernel.lang.kotlin.knkf.endpoints

import org.netkernel.lang.kotlin.knkf.context.GeneralRequestContext
import org.netkernel.lang.kotlin.knkf.context.RequestContext
import org.netkernel.lang.kotlin.knkf.context.TransparentOverlayRequestContext
import org.netkernel.layer0.nkf.INKFRequestContext
import org.netkernel.module.standard.endpoint.TransparentOverlayImpl

abstract class KotlinTransparentOverlay: TransparentOverlayImpl() {
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

    final override fun onRequest(identifier: String?, context: INKFRequestContext?) {
        checkNotNull(identifier)
        checkNotNull(context)

        TransparentOverlayRequestContext(context).onRequest(identifier)
    }

    open fun RequestContext.postCommission() {}

    open fun RequestContext.preDecommission() {}

    abstract fun TransparentOverlayRequestContext.onRequest(elementId: String)
}
