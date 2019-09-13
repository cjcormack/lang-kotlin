package org.netkernel.lang.kotlin.knkf.context

import org.netkernel.layer0.nkf.INKFRequestContext

class TransparentOverlayRequestContext(nkfContext: INKFRequestContext): RequestContextWithResponse<Any>(nkfContext), RequestContextWithPrimary
