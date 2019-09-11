package org.netkernel.lang.kotlin.wiki

import org.netkernel.lang.kotlin.knkf.KotlinAccessor
import org.netkernel.lang.kotlin.knkf.LogLevel
import org.netkernel.lang.kotlin.knkf.context.SourceRequestContext
import java.io.StringReader

class KotlinMacroAccessor: KotlinAccessor() {
    val ESCAPE_CDATA_IN_CDATA_FROM = "\\]\\]>"
    val ESCAPE_CDATA_IN_CDATA_TO = "\\]\\]\\]\\]><!\\[CDATA\\[>"

    init {
        declareThreadSafe()
    }
    override fun SourceRequestContext.onSource() {
        val type = argumentValue("activeType")
        val operand = source<String>("arg:operand")

        response {
            when (type) {
                "wikiTemplateEngine/KOTLIN/XHTML" -> operand.kotlinToXhtml()
                "wikiTemplateEngine/KOTLIN/DOCBOOK" -> operand.kotlinToDocbook()
                else -> {
                    log(LogLevel.WARNING, "Unrecognised macro output '$type'")
                    operand
                }
            }
        }
    }
    private fun String.kotlinToDocbook(): String {
        return "<programlisting><![CDATA[${replace(ESCAPE_CDATA_IN_CDATA_FROM, ESCAPE_CDATA_IN_CDATA_TO) }]]></programlisting>";

    }

    private fun String.kotlinToXhtml(): String {
        return Kotlin2XHTML.convert(StringReader(this))
    }
}
