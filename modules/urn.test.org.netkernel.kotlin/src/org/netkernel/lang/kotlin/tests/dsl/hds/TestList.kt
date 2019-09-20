package org.netkernel.lang.kotlin.tests.dsl.hds

import org.netkernel.lang.kotlin.dsl.asserts.hds.hdsSchematron
import org.netkernel.lang.kotlin.dsl.hds.HdsNode
import org.netkernel.lang.kotlin.dsl.hds.hds
import org.netkernel.lang.kotlin.dsl.xunit.Test
import org.netkernel.lang.kotlin.dsl.xunit.TestList
import org.netkernel.lang.kotlin.xunit.XUnitTest
import org.netkernel.lang.kotlin.xunit.XUnitTestList

class TestList: XUnitTestList() {
    override fun TestList.initTestList() {
        import("res:/org/netkernel/mod/hds/assertions/assertLibrary.xml")
    }

    @XUnitTest("Adding child outside of init()")
    fun Test.inlineSourceTest()  {
        requestInlineSource {
            response {
                hds {
                    val testNode = node("test") {
                        node("child1", "Child One")
                    }

                    testNode.node("child2", "Child Two")
                }
            }
        }
        assert {
            hdsSchematron {
                schema("urn:test:kotlin") {
                    pattern {
                        rule("/") {
                            assert("test", "'test' expected to exist at root")
                            assert("not(child2)", "No 'child2' expected at root")
                        }
                        rule("/test") {
                            assert("child1", "'child1` expected at root")
                            assert("child2", "'child2` expected at root")
                        }
                    }
                }
            }
        }
    }
}
