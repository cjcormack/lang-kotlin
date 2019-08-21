package org.netkernel.lang.kotlin.knkf

import org.netkernel.layer0.nkf.INKFRequestContext
import org.netkernel.module.standard.endpoint.StandardTransreptorImpl

abstract class KotlinTransreptor: StandardTransreptorImpl() {
    final override fun onTransrept(context: INKFRequestContext?) {
        checkNotNull(context)

        RequestContext(context).onTransrept()
    }

    abstract fun RequestContext.onTransrept()
}
