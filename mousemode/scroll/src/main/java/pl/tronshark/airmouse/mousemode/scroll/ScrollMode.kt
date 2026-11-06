package pl.tronshark.airmouse.mousemode.scroll

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.accessibilityservice.GestureDescription.StrokeDescription
import android.content.Context
import android.graphics.Path
import android.hardware.display.DisplayManager
import android.os.Handler
import android.util.Log
import android.view.Display
import com.samsung.android.sdk.penremote.ButtonEvent
import pl.tronshark.airmouse.core.common.notifications.NotificationsManager
import pl.tronshark.airmouse.core.common.spen.SPenManager
import pl.tronshark.airmouse.core.common.spen.listener.ButtonAction
import pl.tronshark.airmouse.core.common.spen.listener.ConnectionResultCallback
import pl.tronshark.airmouse.core.common.utils.PrefKeys
import pl.tronshark.airmouse.core.common.utils.PreferencesManager
import pl.tronshark.airmouse.core.common.utils.ScrollAction
import pl.tronshark.airmouse.core.common.utils.getDisplaySize
import pl.tronshark.airmouse.mousemode.base.BaseMode
import pl.tronshark.airmouse.mousemode.base.PenConst

class ScrollMode(
    dispatchGesture: (GestureDescription, AccessibilityService.GestureResultCallback?, Handler?) -> Unit,
    notificationsManager: NotificationsManager,
    preferences: PreferencesManager,
    sPenManager: SPenManager,
    stopService: () -> Unit,
    context: Context,
) : BaseMode(
    notificationsManager = notificationsManager,
    dispatchGesture = dispatchGesture,
    preferences = preferences,
    sPenManager = sPenManager,
    stopService = stopService,
    context = context,
) {
    private val displayManager = context.getSystemService(Context.DISPLAY_SERVICE) as DisplayManager
    private var singlePressAction: ScrollAction = ScrollAction.Default
    private var doublePressAction: ScrollAction = ScrollAction.None
    private var longPressAction: ScrollAction = ScrollAction.None
    private var scrollDistance = 60f
    private var deltaY = 0f

    private var lastClickTime = 0L
    private var pendingSinglePressRunnable: Runnable? = null
    private var pendingLongPressRunnable: Runnable? = null
    private var isLongPressTriggered = false
    private var isDoublePressTriggered = false

    override fun start() {
        super.start()
        preferencesUpdated()

        sPenManager.connect(object : ConnectionResultCallback() {
            override fun onSuccess() {
                mainHandler.postDelayed({
                    sPenManager.registerButtonEventListener(
                        ::onButtonEvent,
                        ::showErrorAndStopService
                    )
                }, PenConst.DELAY_TO_EVENT_REGISTER_MS)
            }
        })
    }

    private fun onButtonEvent(@ButtonAction type: Int, timeStamp: Long) {
        Log.d(tagName, "Button action: $type")

        if (type == ButtonEvent.ACTION_DOWN) {
            isLongPressTriggered = false
            isDoublePressTriggered = false
            pendingLongPressRunnable?.let { mainHandler.removeCallbacks(it) }
            pendingLongPressRunnable = null
            pendingSinglePressRunnable?.let { mainHandler.removeCallbacks(it) }
            pendingSinglePressRunnable = null

            if (longPressAction != ScrollAction.None) {
                pendingLongPressRunnable = Runnable {
                    isLongPressTriggered = true
                    sPenManager.unregisterAirMotionEventListener()
                    executeScrollAction(longPressAction)
                }
                mainHandler.postDelayed(pendingLongPressRunnable!!, LONG_PRESS_TIMEOUT_MS)
            }

            val now = System.currentTimeMillis()
            val timeSinceLastClick = now - lastClickTime

            if (timeSinceLastClick < DOUBLE_PRESS_TIMEOUT_MS) {
                // Double press detected
                lastClickTime = 0L
                isDoublePressTriggered = true
                pendingLongPressRunnable?.let { mainHandler.removeCallbacks(it) }
                pendingLongPressRunnable = null
                pendingSinglePressRunnable?.let { mainHandler.removeCallbacks(it) }
                pendingSinglePressRunnable = null

                executeScrollAction(doublePressAction)
            } else {
                lastClickTime = now
                deltaY = 0f

                if (singlePressAction == ScrollAction.Default) {
                    sPenManager.registerAirMotionEventListener(
                        ::onAirMotionEvent,
                        ::showErrorAndStopService
                    )
                }
            }
        } else if (type == ButtonEvent.ACTION_UP) {
            pendingLongPressRunnable?.let { mainHandler.removeCallbacks(it) }
            pendingLongPressRunnable = null

            if (isLongPressTriggered || isDoublePressTriggered) {
                return
            }

            if (singlePressAction == ScrollAction.Default) {
                sPenManager.unregisterAirMotionEventListener()
                performScrollGesture()
                Log.d(tagName, "DeltaY: $deltaY")
            } else if (singlePressAction != ScrollAction.None) {
                pendingSinglePressRunnable = Runnable {
                    executeScrollAction(singlePressAction)
                }
                mainHandler.postDelayed(pendingSinglePressRunnable!!, DOUBLE_PRESS_TIMEOUT_MS)
            }
        }
    }

    private fun onAirMotionEvent(deltaX: Float, deltaY: Float, timeStamp: Long) {
        this@ScrollMode.deltaY += deltaY
    }

    private fun executeScrollAction(action: ScrollAction) {
        when (action) {
            ScrollAction.ScrollDown -> performFixedScroll(isDown = true)
            ScrollAction.ScrollUp -> performFixedScroll(isDown = false)
            ScrollAction.ClickCenter -> performCenterClick()
            else -> Unit
        }
    }

    private fun performCenterClick() {
        val (screenWidth, screenHeight) = getDisplaySize(displayManager.getDisplay(Display.DEFAULT_DISPLAY))
        val centerX = screenWidth / 2f
        val centerY = screenHeight / 2f

        val path = Path().apply {
            moveTo(centerX, centerY)
        }
        val stroke = StrokeDescription(path, 0L, 50L)
        val gesture = GestureDescription.Builder()
            .addStroke(stroke)
            .build()

        dispatchGesture(gesture, null, null)
    }

    private fun performFixedScroll(isDown: Boolean) {
        val (screenWidth, screenHeight) = getDisplaySize(displayManager.getDisplay(Display.DEFAULT_DISPLAY))
        val centerX = screenWidth / 2f
        val centerY = screenHeight / 2f

        val halfDistance = screenHeight * (scrollDistance / 200f)

        val startY = if (isDown) centerY - halfDistance else centerY + halfDistance
        val endY = if (isDown) centerY + halfDistance else centerY - halfDistance

        val scrollPath = Path().apply {
            moveTo(centerX, startY)
            lineTo(centerX, endY)
        }

        val gesture = GestureDescription.Builder()
            .addStroke(StrokeDescription(scrollPath, 0L, GESTURE_TIME_MS))
            .build()

        dispatchGesture(gesture, null, null)
    }

    private fun performScrollGesture() {
        val isScrollingUp = deltaY > 0

        val (screenWidth, screenHeight) = getDisplaySize(displayManager.getDisplay(Display.DEFAULT_DISPLAY))
        val centerX = screenWidth / 2f
        val centerY = screenHeight / 2f

        val halfDistance = screenHeight * (scrollDistance / 200f)

        val startY = if (isScrollingUp) centerY + halfDistance else centerY - halfDistance
        val endY = if (isScrollingUp) centerY - halfDistance else centerY + halfDistance

        val scrollPath = Path().apply {
            moveTo(centerX, startY)
            lineTo(centerX, endY)
        }

        val gesture = GestureDescription.Builder()
            .addStroke(StrokeDescription(scrollPath, 0L, GESTURE_TIME_MS))
            .build()

        dispatchGesture(gesture, null, null)
    }

    override fun stop() {
        super.stop()
        sPenManager.disconnect()
    }

    override fun preferencesUpdated() {
        super.preferencesUpdated()
        singlePressAction = preferences.get(PrefKeys.SCROLL_SINGLE_PRESS_ACTION)
        doublePressAction = preferences.get(PrefKeys.SCROLL_DOUBLE_PRESS_ACTION)
        longPressAction = preferences.get(PrefKeys.SCROLL_LONG_PRESS_ACTION)
        scrollDistance = preferences.get(PrefKeys.SCROLL_DISTANCE)
    }

    companion object {
        private const val GESTURE_TIME_MS = 150L
        private const val DOUBLE_PRESS_TIMEOUT_MS = 350L
        private const val LONG_PRESS_TIMEOUT_MS = 500L
    }
}
