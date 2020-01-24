package tv.justplay.android

import android.util.Log
import androidx.multidex.MultiDexApplication
import tech.posit.android.posit.Posit

class JpApplication : MultiDexApplication() {

    private val LOG_TAG = "JpApplication"

    // Called when the application is starting, before any other application objects have been created.
    // Overriding this method is totally optional!
    override fun onCreate() {

        val app = this

        super.onCreate()
        // Required initialization logic here!

//        startKoin {
//            // declare used Android context
//            androidContext(app)
//            // declare modules
//            modules()
//        }

//      Development Credentials
        val clientId = "jp-tv-007"
        val accessKey = "b86e7b38-dd70-11e9-b615-ceb1c7aefb05"
        val secretKey = "b86e7dcc-dd70-11e9-b615-ceb1c7aefb05"

        // Testing Credentials
//        val clientId = "jp-tv-007"
//        val accessKey = "ae2aff8c-bd9c-11e9-bb3d-560d0e73f093"
//        val secretKey = "ae2b01a8-bd9c-11e9-bb3d-560d0e73f093"
//        val apiPath = "https://api.posit.tech/testing/"

        Log.d(LOG_TAG, "Initializing Posit from Justplay")
        Posit.init(app, clientId, accessKey, secretKey)

    }
}