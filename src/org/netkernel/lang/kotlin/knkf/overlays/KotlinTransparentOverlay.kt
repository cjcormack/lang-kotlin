package org.netkernel.lang.kotlin.knkf.overlays

import org.netkernel.layer0.nkf.INKFRequestContext
import org.netkernel.module.standard.endpoint.TransparentOverlayImpl

abstract class KotlinTransparentOverlay: TransparentOverlayImpl() {
    final override fun onRequest(identifier: String?, context: INKFRequestContext?) {
        checkNotNull(identifier)
        checkNotNull(context)

        TransparentOverlayContext(context).onRequest(identifier)
    }

    abstract fun TransparentOverlayContext.onRequest(elementId: String)
}
