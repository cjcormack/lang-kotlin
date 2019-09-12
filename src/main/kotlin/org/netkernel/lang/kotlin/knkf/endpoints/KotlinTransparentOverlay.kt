package org.netkernel.lang.kotlin.knkf.endpoints

import org.netkernel.lang.kotlin.knkf.context.TransparentOverlayRequestContext
import org.netkernel.layer0.nkf.INKFRequestContext
import org.netkernel.module.standard.endpoint.TransparentOverlayImpl

abstract class KotlinTransparentOverlay: TransparentOverlayImpl() {
    final override fun onRequest(identifier: String?, context: INKFRequestContext?) {
        checkNotNull(identifier)
        checkNotNull(context)

        TransparentOverlayRequestContext(context).onRequest(identifier)
    }

    abstract fun TransparentOverlayRequestContext.onRequest(elementId: String)
}
