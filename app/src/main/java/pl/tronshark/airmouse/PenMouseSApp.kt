package pl.tronshark.airmouse

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import pl.tronshark.airmouse.core.common.di.ActivityProvider
import javax.inject.Inject

@HiltAndroidApp
class AirMouseApp : Application() {
    @Inject
    lateinit var activityProvider: ActivityProvider

    override fun onCreate() {
        super.onCreate()
        registerActivityLifecycleCallbacks(activityProvider)
    }
}