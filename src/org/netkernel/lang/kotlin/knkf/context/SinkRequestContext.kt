package org.netkernel.lang.kotlin.knkf.context

import org.netkernel.layer0.nkf.INKFRequestContext

class SinkRequestContext(nkfContext: INKFRequestContext): RequestContext(nkfContext), RequestContextWithPrimary
