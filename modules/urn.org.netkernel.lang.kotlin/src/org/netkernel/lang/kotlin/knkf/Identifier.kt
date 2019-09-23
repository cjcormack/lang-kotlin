package org.netkernel.lang.kotlin.knkf

import org.netkernel.urii.IIdentifier

data class Identifier(val identifier: String): IIdentifier {
    override fun toString(): String {
        return identifier
    }
}
