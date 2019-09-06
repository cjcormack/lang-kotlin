package org.netkernel.lang.kotlin.knkf.overlays

import org.netkernel.lang.kotlin.knkf.BaseRequestContext
import org.netkernel.lang.kotlin.knkf.Request
import org.netkernel.layer0.nkf.INKFRequestContext

class TransparentOverlayContext(context: INKFRequestContext): BaseRequestContext<Any>(context) {
    fun issuableClone(): Request<Any> {
        return Request<Any>(this, context.thisRequest.issuableClone)
    }
    fun issuableClone(init: Request<Any>.() -> Unit): Request<Any> {
        val request = Request<Any>(this, context.thisRequest.issuableClone)
        request.init()
        return request
    }
}
