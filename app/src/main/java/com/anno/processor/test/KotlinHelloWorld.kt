package com.anno.processor.test

import android.media.MediaDrm
import com.lib.anno.KotlinFieldAnn
import com.lib.anno.KotlinHelloWorldAnn
import com.lib.anno.KotlinMethodAnn
import org.jetbrains.annotations.NotNull


@KotlinHelloWorldAnn
class KotlinHelloWorld(var param: String? = null) : MediaDrm.OnEventListener {

    @KotlinFieldAnn
    private val aa = "xyz"

    @KotlinMethodAnn
    private fun print(@NotNull test: String= "xx"): Boolean {
        println(test)
        return true
    }

    override fun onEvent(
        md: MediaDrm,
        sessionId: ByteArray?,
        event: Int,
        extra: Int,
        data: ByteArray?
    ) {

    }
}

