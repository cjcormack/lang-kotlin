package org.netkernel.lang.kotlin.inline

import org.netkernel.lang.kotlin.knkf.context.*

typealias InlineSourceLambda = SourceRequestContext.() -> Unit
typealias InlineSinkLambda = SinkRequestContext.() -> Unit
typealias InlineExistsLambda = ExistsRequestContext.() -> Unit
typealias InlineDeleteLambda = DeleteRequestContext.() -> Unit
typealias InlineNewLambda = NewRequestContext.() -> Unit
