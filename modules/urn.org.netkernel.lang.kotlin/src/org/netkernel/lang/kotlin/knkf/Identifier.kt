package org.netkernel.lang.kotlin.knkf

import org.netkernel.urii.IIdentifier

@JvmInline
value class Identifier(val identifier: String): IIdentifier {
    override fun toString(): String {
        return identifier
    }
}
