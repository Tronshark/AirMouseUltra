package pl.tronshark.airmouse.core.common.spen.listener

fun interface AirMotionEventListener {
    fun onEvent(deltaX: Float, deltaY: Float, timeStamp: Long)
}