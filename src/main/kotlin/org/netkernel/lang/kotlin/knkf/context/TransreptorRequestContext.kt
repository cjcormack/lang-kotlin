package org.netkernel.lang.kotlin.knkf.context

import org.netkernel.layer0.nkf.INKFRequestContext

class TransreptorRequestContext<R>(nkfContext: INKFRequestContext): RequestContextWithResponse<R>(nkfContext), RequestContextWithPrimary
