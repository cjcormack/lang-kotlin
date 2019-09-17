package org.netkernel.lang.kotlin.inline

import org.netkernel.lang.kotlin.knkf.context.*
import org.netkernel.lang.kotlin.knkf.endpoints.KotlinAccessor
import org.netkernel.lang.kotlin.util.firstValue
import org.netkernel.layer0.meta.impl.SourcedArgumentMetaImpl
import org.netkernel.mod.hds.IHDSDocument

class InlineKotlinAccessor: KotlinAccessor() {
    init {
        declareThreadSafe()
        declareArgument(SourcedArgumentMetaImpl("operator", "Lambda to run", null, arrayOf<Class<*>>(InlineRequest::class.java)))
    }

    private fun <T : RequestContext> T.execLambda() {
        val execConfig = source<IHDSDocument>("arg:operator")

        val lambda = execConfig.reader.firstValue<InlineRequest<T>>("//lambda")

        lambda.lambda(this)
    }

    override fun SourceRequestContext.onSource() = this.execLambda()
    override fun SinkRequestContext.onSink() = this.execLambda()
    override fun ExistsRequestContext.onExists() = this.execLambda()
    override fun NewRequestContext.onNew() = this.execLambda()
    override fun DeleteRequestContext.onDelete() = this.execLambda()
}
