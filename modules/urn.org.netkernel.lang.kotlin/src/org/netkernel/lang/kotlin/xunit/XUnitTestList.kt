package org.netkernel.lang.kotlin.xunit

import org.netkernel.lang.kotlin.dsl.xunit.TestList
import org.netkernel.lang.kotlin.dsl.xunit.testlist
import org.netkernel.lang.kotlin.knkf.context.SourceRequestContext
import org.netkernel.lang.kotlin.knkf.endpoints.KotlinAccessor
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.functions

@Target(AnnotationTarget.FUNCTION)
@MustBeDocumented
annotation class XUnitTest(val name: String)

abstract class XUnitTestList: KotlinAccessor() {
    init {
        declareThreadSafe()
    }

    final override fun SourceRequestContext.onSource() {
        response {
            testlist {
                initTestList()
                this@XUnitTestList::class.functions.forEach {
                    val testAnnotation = it.findAnnotation<XUnitTest>()

                    if (testAnnotation != null) {
                        test(testAnnotation.name) {
                            it.call(this@XUnitTestList, this)
                        }
                    }
                }
            }
        }
    }
    open fun TestList.initTestList() {}
}
