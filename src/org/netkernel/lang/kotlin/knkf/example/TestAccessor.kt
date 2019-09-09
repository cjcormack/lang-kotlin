package org.netkernel.lang.kotlin.knkf.example

import org.netkernel.lang.kotlin.knkf.Identifier
import org.netkernel.lang.kotlin.knkf.KotlinAccessor
import org.netkernel.lang.kotlin.knkf.context.SourceRequestContext
import org.netkernel.lang.kotlin.knkf.context.request.sourceRequest

class TestAccessor: KotlinAccessor() {
    override fun SourceRequestContext.onSource() {
        response {
            source("arg:hello") {
                argumentByValue("hello") {
                    sourceRequest<Any>(Identifier("hey"))
                }
            }
        }
    }
}
