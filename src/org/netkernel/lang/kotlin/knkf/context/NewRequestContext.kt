package org.netkernel.lang.kotlin.knkf.context

import org.netkernel.lang.kotlin.knkf.Identifier
import org.netkernel.layer0.nkf.INKFRequestContext

class NewRequestContext(nkfContext: INKFRequestContext): RequestContextWithResponse<Identifier>(nkfContext), RequestContextWithPrimary
