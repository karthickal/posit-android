# Posit for Android
This repository contains an example application that uses the Posit Android SDK.

## About Posit

Posit allows video publisher platforms to gain insights about their videos by leveraging deep learning in near real time.

## Integration Steps

### Installation

#### Dependencies

Include the following dependencies using Gradle

```
implementation 'tech.posit.android:posit:0.3.0'

repositories {
    maven {
        url  "https://dl.bintray.com/posit/posit-android-sdk" 
    }
}
```   

To ensure successful compilation, add the following lines in the build.gradle file under app module 

```
android.useAndroidX=true
android.enableJetifier=true
```

Ensure Exoplayer, AWS SDK and Jwt dependencies are added in your build.gradle file 

```
implementation 'com.amazonaws:aws-android-sdk-s3:2.13.+'
implementation ('com.amazonaws:aws-android-sdk-mobile-client:2.13.+@aar') { transitive = true }
implementation 'com.google.android.exoplayer:exoplayer:2.10.0'

api 'io.jsonwebtoken:jjwt-api:0.10.5'
runtimeOnly 'io.jsonwebtoken:jjwt-impl:0.10.5'
runtimeOnly('io.jsonwebtoken:jjwt-orgjson:0.10.5') {
  exclude group: 'org.json', module: 'json' //provided by Android natively
}
```

#### Permissions

The Posit Android SDK needs the following permissions in order to work. Please add the following lines in your AndroidManifest.xml file. This permission allows the app to connect to network services.

```
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
```

#### Initialization

The SDK has to be initialized with your application keys before it can be used. Please add the relevant lines to your app’s base Activity class.

```

import android.app.Application
import tech.posit.android.Posit

class JpApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        val app = this
        // Required initialization logic here!
        val clientId = "jp-tv-007"
        val accessKey = "ae2aff8c-bd9c-11e9-bb3d-560d0e73f093"
        val secretKey = "ae2b01a8-bd9c-11e9-bb3d-560d0e73f093"
        val apiPath = "https://api.posit.tech/testing/"

        Posit.Manager.build(app, clientId, accessKey, secretKey, apiPath)
    }
}
```
Add the following attribute to your ExoPlayerView UI element

```
<com.google.android.exoplayer2.ui.SimpleExoPlayerView
	...
app:surface_type="texture_view"
	...
/>
```

#### Implementation

For the default implementation after creating the Exoplayer instance in your activity, register with Posit like this 

```
Posit.register(this, playerView, videoId, fps)
```

videoId = the ID of the video in your database; fps = frames per second of the video

That’s it! All your videos now display information when the user pauses. You can enable/disable annotations for certain videos from your developer dashboard.



  
