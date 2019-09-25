package org.netkernel.lang.kotlin.tests.dsl.mapper

import org.netkernel.lang.kotlin.dsl.asserts.hds.hdsSchematron
import org.netkernel.lang.kotlin.dsl.mapper.mapper
import org.netkernel.lang.kotlin.dsl.xunit.Test
import org.netkernel.lang.kotlin.dsl.xunit.TestList
import org.netkernel.lang.kotlin.xunit.XUnitTest
import org.netkernel.lang.kotlin.xunit.XUnitTestList

class TestList: XUnitTestList() {
    override fun TestList.initTestList() {
        import("res:/org/netkernel/mod/hds/assertions/assertLibrary.xml")
    }

    @XUnitTest("Endpoint with active grammar (NK-23)")
    fun Test.endpointWithActiveGrammar() {
        requestInlineSource {
            response {
                mapper {
                    endpoint {
                        activeGrammar("active:larkTest")
                        request("active:testInternal")
                    }
                }
            }
        }
        assert {
            hdsSchematron {
                schema("urn:test:kotlin:dsl:mapper") {
                    pattern {
                        rule("/") {
                            assert("count(*) = 1", "Only one node expected")
                            assert("config", "Root node expected to be 'config'")
                        }
                        rule("/config") {
                            assert("count(*) = 1", "Only one node expected")
                            assert("endpoint", "Node expected to be 'endpoint'")
                        }
                        rule("/config/endpoint") {
                            assert("count(*) = 2", "Only two nodes expected")
                            assert("grammar", "Expected a 'grammar' node")
                            assert("request", "Expected a 'request' node")
                        }
                        rule("/config/endpoint/grammar") {
                            assert("count(*) = 1", "Only one node expected")
                            assert("active", "Expected an 'active' node")
                        }
                        rule("/config/endpoint/grammar/active") {
                            assert("count(*) = 1", "Only one node expected")
                            assert("identifier = 'active:larkTest'", "Expected an 'identifier' node to have correct value")
                        }
                        rule("/config/endpoint/request") {
                            assert("count(*) = 1", "Only one node expected")
                            assert("identifier = 'active:testInternal'", "Expected an 'identifier' node to have correct value")
                        }
                    }
                }
            }
        }
    }
}
