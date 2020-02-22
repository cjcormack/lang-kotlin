package org.netkernel.lang.kotlin.util

import java.net.URLEncoder

fun String.urlEncode(enc: String = "UTF-8"): String {
    return URLEncoder.encode(this, enc)
}
