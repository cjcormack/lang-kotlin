package org.netkernel.lang.kotlin.tests.dsl.hds

import org.netkernel.lang.kotlin.dsl.asserts.hds.hdsSchematron
import org.netkernel.lang.kotlin.dsl.hds.hds
import org.netkernel.lang.kotlin.dsl.xunit.Test
import org.netkernel.lang.kotlin.dsl.xunit.TestList
import org.netkernel.lang.kotlin.xunit.XUnitTest
import org.netkernel.lang.kotlin.xunit.XUnitTestList

class TestList: XUnitTestList() {
    override fun TestList.initTestList() {
        import("res:/org/netkernel/mod/hds/assertions/assertLibrary.xml")
    }

    @XUnitTest("Empty")
    fun Test.empty() {
        requestInlineSource {
            response {
                hds {}
            }
        }
        assert {
            hdsSchematron {
                schema("urn:test:kotlin:empty") {
                    pattern {
                        rule("/") {
                            assert("count(*) = 0", "No children expected")

                        }
                    }
                }
            }
        }
    }

    @XUnitTest("Multiple children")
    fun Test.multipleChildren() {
        requestInlineSource {
            response {
                hds {
                    node("parent") {
                        node("child1", "Child One")
                        node("child2", "Child Two")
                    }
                }
            }
        }
        assert {
            hdsSchematron {
                schema("urn:test:kotlin:multipleChildren") {
                    pattern {
                        rule("/") {
                            assert("count(*) = 1", "One child expected")
                            assert("parent", "Parent node expected")
                        }
                        rule("/parent") {
                            assert("count(*) = 2", "Two children expected")
                            assert("child1", "'child1' expected")
                            assert("child1 = 'Child One'", "'child1' should have correct value")
                            assert("child2", "'child2' expected")
                            assert("child2 = 'Child Two'", "'child2' should have correct value")
                        }
                    }
                }
            }
        }
    }

    @XUnitTest("Setting attribute")
    fun Test.settingAttribute() {
        requestInlineSource {
            response {
                hds {
                    node("parent") {
                        node("node") {
                            node("@attr", "value")
                        }
                    }
                }
            }
        }
        assert {
            hdsSchematron {
                schema("urn:test:kotlin:settingAttribute") {
                    pattern {
                        rule("/") {
                            assert("parent", "Parent node expected")
                        }
                        rule("/parent") {
                            assert("node", "'node' should exist")
                            assert("node = ''", "'node' should have no value")
                        }
                        rule("/parent/node") {
                            assert("@attr = 'value'", "@attr should exist and have correct value")
                        }
                    }
                }
            }
        }
    }

    @XUnitTest("Attribute and value")
    fun Test.attributeAndValue() {
        requestInlineSource {
            response {
                hds {
                    node("parent") {
                        node("node") {
                            node("@attr", "attr value")
                            "Node value"
                        }
                    }
                }
            }
        }
        assert {
            hdsSchematron {
                schema("urn:test:kotlin:attributeAndValue") {
                    pattern {
                        rule("/") {
                            assert("parent", "Parent node expected")
                        }
                        rule("/parent") {
                            assert("node", "'node' should exist")
                            assert("node = 'Node value'", "'node' should have correct value")
                        }
                        rule("/parent/node") {
                            assert("@attr = 'attr value'", "@attr should exist and have correct value")
                        }
                    }
                }
            }
        }
    }

    @XUnitTest("Adding child outside of init()")
    fun Test.childOutsideOfInit()  {
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
                schema("urn:test:kotlin:childOutsideOfInit") {
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
