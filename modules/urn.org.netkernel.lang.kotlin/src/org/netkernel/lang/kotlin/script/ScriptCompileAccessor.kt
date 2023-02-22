package org.netkernel.lang.kotlin.script

import org.netkernel.lang.kotlin.knkf.context.SourceRequestContext
import org.netkernel.layer0.util.RequestScopeClassLoader

class ScriptCompileAccessor: BaseScriptAccessor() {
    override fun SourceRequestContext.onSource() {
        val cl = RequestScopeClassLoader(nkfContext.kernelContext.requestScope)
        Thread.currentThread().contextClassLoader = cl

        val script = source<String>("arg:operator")

        this.nkfContext.getParamValues("dependencies")

        val kotlinScriptConfig = loadKotlinScriptConfig()
        val (_, report) = compileKotlin(kotlinScriptConfig.scriptConfiguration, script)

        response(NetKernelKotlinScriptCompilationResult(report))
    }
}
