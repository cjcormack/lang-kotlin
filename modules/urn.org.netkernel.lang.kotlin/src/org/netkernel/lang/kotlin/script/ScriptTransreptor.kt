package org.netkernel.lang.kotlin.script

import org.netkernel.lang.kotlin.knkf.context.TransreptorRequestContext

class ScriptTransreptor: BaseKotlinScriptTransreptor<ScriptRepresentation>() {
    init {
        this.toRepresentation(ScriptRepresentation::class.java)
    }

    override fun TransreptorRequestContext<ScriptRepresentation>.onTransrept() {
        response {
            ScriptRepresentation(performCompilation(NetKernelScriptConfiguration))
        }
    }
}
