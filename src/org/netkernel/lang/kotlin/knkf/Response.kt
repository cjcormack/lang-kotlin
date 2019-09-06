package org.netkernel.lang.kotlin.knkf

import org.netkernel.layer0.nkf.INKFResponse
import org.netkernel.layer0.nkf.INKFResponseReadOnly

class Response<T>(val nkResponse: INKFResponse)

class ReadOnlyResponse<T>(val nkResponse: INKFResponseReadOnly<T>)
