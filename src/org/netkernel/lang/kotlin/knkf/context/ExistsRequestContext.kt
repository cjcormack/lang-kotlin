package org.netkernel.lang.kotlin.knkf.context

import org.netkernel.layer0.nkf.INKFRequestContext

class ExistsRequestContext(nkfContext: INKFRequestContext): RequestContextWithResponse<Boolean>(nkfContext)
