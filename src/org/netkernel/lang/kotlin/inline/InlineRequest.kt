package org.netkernel.lang.kotlin.inline

import java.io.Serializable

class InlineRequest<T>(val lambda: T.() -> Unit): Serializable
