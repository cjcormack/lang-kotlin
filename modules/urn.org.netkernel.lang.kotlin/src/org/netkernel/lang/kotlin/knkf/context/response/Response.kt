package org.netkernel.lang.kotlin.knkf.context.response

import org.netkernel.lang.kotlin.knkf.context.ContextBuilderMarker
import org.netkernel.layer0.nkf.INKFResponse
import org.netkernel.layer0.nkf.INKFResponseReadOnly

@ContextBuilderMarker
sealed class BaseResponse<R>

// While INKFResponse extends INKFResponseReadOnly, it does so without the '<R>'. This prevents us putting the nkfContext
// in the supertype, which would have made the uses of BaseResponse nicer.
class Response<R>(val nkfResponse: INKFResponse): BaseResponse<R>() {
    var mimeType: String
        get() = nkfResponse.mimeType
        set(value) {
            nkfResponse.mimeType = value
        }
}

class ReadOnlyResponse<R>(val nkfResponse: INKFResponseReadOnly<R>): BaseResponse<R>() {
    val mimeType: String
        get() = nkfResponse.mimeType
}
