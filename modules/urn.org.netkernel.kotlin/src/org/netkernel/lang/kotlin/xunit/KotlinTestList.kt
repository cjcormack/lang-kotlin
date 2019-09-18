package org.netkernel.lang.kotlin.xunit

import org.netkernel.lang.kotlin.knkf.context.SourceRequestContext
import org.netkernel.lang.kotlin.knkf.endpoints.KotlinAccessor
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.functions

@Target(AnnotationTarget.FUNCTION)
@MustBeDocumented
annotation class KotlinTest(val name: String)

abstract class KotlinTestList: KotlinAccessor() {
    init {
        declareThreadSafe()
    }

    final override fun SourceRequestContext.onSource() {
        response {
            testlist {
                this@KotlinTestList::class.functions.forEach {
                    val testAnnotation = it.findAnnotation<KotlinTest>()

                    if (testAnnotation != null) {
                        test(testAnnotation.name) {
                            it.call(this@KotlinTestList, this)
                        }
                    }
                }
            }
        }
    }
}
