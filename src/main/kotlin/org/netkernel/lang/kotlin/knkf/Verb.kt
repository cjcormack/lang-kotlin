package org.netkernel.lang.kotlin.knkf

import org.netkernel.layer0.nkf.INKFRequestReadOnly

enum class Verb(val nkfVerb: Int) {
    SOURCE(INKFRequestReadOnly.VERB_SOURCE),
    SINK(INKFRequestReadOnly.VERB_SINK),
    EXISTS(INKFRequestReadOnly.VERB_EXISTS),
    DELETE(INKFRequestReadOnly.VERB_DELETE),
    NEW(INKFRequestReadOnly.VERB_NEW);

    companion object {
        private val nkfValuesMap = Verb.values().map { it.nkfVerb to it }.toMap()

        fun fromNkf(nkfVerb: Int): Verb {
            return checkNotNull(nkfValuesMap[nkfVerb])
        }
    }
}
