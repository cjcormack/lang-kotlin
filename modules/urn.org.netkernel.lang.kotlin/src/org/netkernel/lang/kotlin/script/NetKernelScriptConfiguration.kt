package org.netkernel.lang.kotlin.script

import kotlin.script.experimental.api.*

object NetKernelScriptConfiguration : ScriptCompilationConfiguration({
    defaultImports("org.netkernel.layer0.nkf.INKFRequestContext", "org.netkernel.lang.kotlin.knkf.context.*", "org.netkernel.lang.kotlin.knkf.*")
    ide {
        acceptedLocations(ScriptAcceptedLocation.Everywhere)
    }
    baseClass(NetKernelKotlinScript::class)
})
