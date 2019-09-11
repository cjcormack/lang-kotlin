/*
 * Java2XHTML.java  - No License Freeware
 *
 * A Very simple Java source to XHTML convertor.
 *
 * Thanks to  http://www.devsphere.com
 * Changes (C) 2003 1060 Research Limited
 *
 * Copied from org.netkernel.wiki.rm.endpoint.Java2XHTML and keywords replaced with those used in Kotlin.
 */

package org.netkernel.lang.kotlin.wiki;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Vector;

public class Kotlin2XHTML
{
	static final String keywords[] =
	{
		"as", "as?", "break", "class", "continue", "do", "else", "false", "for", "fun", "if", "in", "!in", "interface", "is",
		"!is", "null", "object", "package", "return", "super", "this", "throw", "true", "try", "typealias", "typeof", "val",
		"var", "when", "while", "by", "catch", "constructor", "delegate", "dynamic", "field", "file", "finally", "get",
		"import", "init", "param", "property", "receiver", "set", "setparam", "where", "actual", "abstract", "annotation",
		"companion", "const", "crossinline", "data", "enum", "expect", "external", "final", "infix", "inline", "inner",
		"internal", "lateinit", "noinline", "open", "operator", "out", "override", "private", "protected", "public", "reified",
		"sealed", "suspend", "tailrec", "vararg"
	};
	static Vector keyw = new Vector(keywords.length);
	static
	{
		for (int i = 0; i < keywords.length; i++)
			keyw.addElement(keywords[i]);
	}
	static int tabsize = 4;
	static String bgcolor = "FFFFFF";
	static String txcolor = "000000";
	static String kwcolor = "0000F0";
	static String cmcolor = "00A000";

	static String convert(StringReader source) throws IOException
	{
		Reader in = source;
		StringWriter out = new StringWriter(2048);
		out.write("<pre class='codeblock'>");
		StringBuffer buf = new StringBuffer(512);
		int c = 0, kwl = 0, bufl = 0;
		char ch = 0, lastch;
		int s_normal  = 0;
		int s_string  = 1;
		int s_char    = 2;
		int s_comline = 3;
		int s_comment = 4;
		int state = s_normal;
		while (c != -1)
		{
			c = in.read();
			lastch = ch;
			ch = c >= 0 ? (char) c : 0;
			if (state == s_normal)
				if (kwl == 0 && Character.isJavaIdentifierStart(ch)
							 && !Character.isJavaIdentifierPart(lastch)
					|| kwl > 0 && Character.isJavaIdentifierPart(ch))
				{
					buf.append(ch);
					bufl++;
					kwl++;
					continue;
				} else
					if (kwl > 0)
					{
						String kw = buf.toString().substring(buf.length() - kwl);
						if (keyw.contains(kw))
						{
							buf.insert(buf.length() - kwl,
								"<span style=\"color:#" + kwcolor + ";\">");
							buf.append("</span>");
						}
						kwl = 0;
					}
			switch (ch)
			{
				case '&':
					buf.append("&amp;");
					bufl++;
					break;
				case '\"':
					buf.append("&quot;");
					bufl++;
					if (state == s_normal)
						state = s_string;
					else
						if (state == s_string && lastch != '\\')
							state = s_normal;
					break;
				case '\'':
					buf.append("\'");
					bufl++;
					if (state == s_normal)
						state = s_char;
					else
						if (state == s_char && lastch != '\\')
							state = s_normal;
					break;
				case '\\':
					buf.append("\\");
					bufl++;
					if (lastch == '\\' && (state == s_string || state == s_char))
						lastch = 0;
					break;
				case '/':
					buf.append("/");
					bufl++;
					if (state == s_comment && lastch == '*')
					{
						buf.append("</span>");
						state = s_normal;
					}
					if (state == s_normal && lastch == '/')
					{
						buf.insert(buf.length() - 2,
							"<span style=\"color:#" + cmcolor + ";\">");
						state = s_comline;
					}
					break;
				case '*':
					buf.append("*");
					bufl++;
					if (state == s_normal && lastch == '/')
					{
						buf.insert(buf.length() - 2,
								"<span style=\"color:#" + cmcolor + ";\">");
						state = s_comment;
					}
					break;
				case '<':
					buf.append("&lt;");
					bufl++;
					break;
				case '>':
					buf.append("&gt;");
					bufl++;
					break;
				case '\t':
					int n = bufl / tabsize * tabsize + tabsize;
					while (bufl < n)
					{
						buf.append(' ');
						bufl++;
					}
					break;
				case '\r':
				case '\n':
					if (state == s_comline)
					{
						buf.append("</span>");
						state = s_normal;
					}
					buf.append(ch);
					if (buf.length() >= 1024)
					{
						out.write(buf.toString());
						buf.setLength(0);
					}
					bufl = 0;
					if (kwl != 0)
						kwl = 0; // This should never execute
					if (state != s_normal && state != s_comment)
						state = s_normal; // Sintax Error
					break;
				case 0:
					if (c < 0)
					{
						if (state == s_comline)
						{
							buf.append("</span>");
							state = s_normal;
						}
						out.write(buf.toString());
						buf.setLength(0);
						bufl = 0;
						if (state == s_comment)
						{
							// Sintax Error
							buf.append("</span>");
							state = s_normal;
						}
						break;
					}
				default:
					bufl++;
					buf.append(ch);
			}
		}
		out.write("</pre>");
		in.close();
		out.close();
		return out.getBuffer().toString();
	}
}
