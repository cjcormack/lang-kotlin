package org.netkernel.lang.kotlin.knkf

import org.netkernel.layer0.nkf.INKFLocale

enum class LogLevel(internal val nkLevel: Int) {
    SEVERE(INKFLocale.LEVEL_SEVERE),
    WARNING(INKFLocale.LEVEL_WARNING),
    INFO(INKFLocale.LEVEL_INFO),
    FINE(INKFLocale.LEVEL_FINE),
    FINER(INKFLocale.LEVEL_FINER),
    DEBUG(INKFLocale.LEVEL_DEBUG)
}
