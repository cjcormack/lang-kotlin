package org.netkernel.lang.kotlin.dsl.xunit

import org.netkernel.lang.kotlin.dsl.BuilderNode
import org.netkernel.lang.kotlin.dsl.declarativeRequest.LiteralBuilder
import org.netkernel.lang.kotlin.dsl.initNode
import org.netkernel.mod.hds.IHDSMutator
import kotlin.reflect.KClass

class Assert(builderToClone: IHDSMutator): BuilderNode(builderToClone, "assert") {
    fun isTrue() {
        builder.addNode("true", null)
    }
    fun isFalse() {
        builder.addNode("false", null)
    }
    fun isNull() {
        builder.addNode("null", null)
    }
    fun isNotNull() {
        builder.addNode("notNull", null)
    }
    fun stringEquals(value: String) {
        builder.addNode("stringEquals", value)
    }
    fun regex(regex: Regex) {
        builder.addNode("regex", regex.pattern)
    }
    fun int() {
        builder.addNode("int", null)
    }
    fun float() {
        builder.addNode("float", null)
    }
    fun maxTime(maxTimeMs: Long) {
        builder.addNode("maxTime", maxTimeMs)
    }
    fun minTime(minTimeMs: Long) {
        builder.addNode("minTime", minTimeMs)
    }
    fun `class`(representationClass: KClass<*>) {
        representationClass(representationClass)
    }
    fun `class`(representationClass: Class<*>) {
        representationClass(representationClass)
    }
    fun representationClass(representationClass: KClass<*>) {
        representationClass(representationClass.java)
    }
    fun representationClass(representationClass: Class<*>) {
        builder.addNode("class", representationClass.canonicalName)
    }
    fun mimetype(mimetype: String) {
        builder.addNode("mimetype", mimetype)
    }
    fun expired() {
        builder.addNode("expired", null)
    }
    fun notExpired() {
        builder.addNode("notExpired", null)
    }
    fun minTotalCost(minTotalCost: Int) {
        builder.addNode("minTotalCost", minTotalCost)
    }
    fun maxTotalCost(maxTotalCost: Int) {
        builder.addNode("maxTotalCost", maxTotalCost)
    }
    fun minLocalCost(minLocalCost: Int) {
        builder.addNode("minLocalCost", minLocalCost)
    }
    fun maxLocalCost(maxLocalCost: Int) {
        builder.addNode("maxLocalCost", maxLocalCost)
    }
    fun scope(scopeDepth: Int) {
        builder.addNode("scope", scopeDepth)
    }
    fun exception(exceptionId: String) {
        builder.addNode("exception", exceptionId)
    }
    fun exceptionMessage(exceptionMessage: String) {
        builder.addNode("exceptionMessage", exceptionMessage)
    }
    fun headerExists(headerExists: String) {
        builder.addNode("headerExists", headerExists)
    }
    fun custom(definition: AssertDefinition, init: CustomAssert.() -> Unit = {}) = custom(definition.name, init)
    fun custom(name: String, init: CustomAssert.() -> Unit = {})  = initNode(CustomAssert(builder, name), init)

    fun custom(definition: AssertDefinition, value: String) = custom(definition.name, value)
    fun custom(name: String, value: String)  = initNode(CustomAssert(builder, name)) {
        builder.setValue(value)
    }
}

class CustomAssert(builderToClone: IHDSMutator, name: String): LiteralBuilder(builderToClone, name)
