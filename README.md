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

#### Permissions

The Posit Android SDK needs the following permissions in order to work. Please add the following lines in your AndroidManifest.xml file. This permission allows the app to connect to network services.

```
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
```

#### Initialization

The SDK has to be initialized with your application keys before it can be used. Please add the relevant lines to your app’s base Activity class.

```

class JpApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        val app = this
        // Required initialization logic here!
        val clientId = "jp-tv-007"
        val accessKey = "ae2aff8c-bd9c-11e9-bb3d-560d0e73f093"
        val secretKey = "ae2b01a8-bd9c-11e9-bb3d-560d0e73f093"
        val apiPath = "https://api.posit.tech/testing/"

      /** @param context The application context object
        * @param clientId The ID issued to the VoD Client
        * @param clientAccessKey The access key issued to the SDK
        * @param clientSecretKey The secret key issued to the SDK */
        Posit.init(app, clientId, accessKey, secretKey)
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

##### Register Video

To make a video shoppable it has to be registered with Posit first. The `scanVideo()` method accepts a callback and scans the target video for objects. 

To register, on Kotlin

```
fun PlayerView.scanVideo(
    clientVideoId: String, // The ID of the video in your database
    fps: Int, // fps = frames per second of the video
    resultCallback: Posit.PositCallback // callback for video and products information from posit
) 
```

The method can be invoked from java as a static util function as 
```
PositUtils.scanVideo(
    PlayerView playerView, //The current exoplayer instance 
    String clientVideoId, // The ID of the video in your database
    Int fps, // fps = frames per second of the video
    Posit.PositCallback resultCallback // callback for video and products information from posit
)
```

The Posit SDK enables the following functionalities - 

* Get notifed when an apparel object is detected in the frame during video playback
* Get notified when a shoppable video is played
* Fetch a list of apparel objects in a video 

##### Notification Callbacks

The posit callback interface  `PositCallback` should be implemented by the client to register for product updates from the Posit library. The following functions notify when the user watches a shoppable video and when an object is detected during playback respectively.

```
 interface PositCallback {
        /**
         * This function will be initially invoked during the start of the video.
         * 
         * important: The function is not a one shot operation, the flag will be updated at any point in time if required.
         * @return [isShoppable]: a flag which will inform if the current video is shoppable or not 
         * */
        fun onVideoShoppable(isShoppable: Boolean)
        
        /**
         * This function will be invoked when a new products are available in the currently playing frame of the video
         * @return [product]: list of products on the currently visible frame of the video
         * */
        fun onNewProduct(product: List<Product>)
    }
}
``` 

##### List of products in a video

Use the following method to get a list of all products in a video. Please pass the ID of a video that is registered with Posit.

```
/**
     * This function can be used to get all the products that appear in any video (which are already indexed by posit)
     *
     * @param [clientVideoId] videoId for which the products should be returned
     * */
    fun Posit.getAllProducts(
        clientVideoId: String
    ): List<Product> {
        TODO()
    }
```
    
The `product` object returned by the `PositCallback` methods and  `getAllProducts()` contains the following details
```

/**
 * The data class representing product info. 
 * This is a standard data object which will be returned by the Posit layer for product information
 * */
data class Product(
    val productId: Int, // The product Id as maintained by Posit
    val name: String, // The general category of the product eg.., shirt, pants
    val imageUrl: String, //The image url of the product obtained from the market place  
    val firstAppearance: Int, // Represents the frame of the video at which at the product initially appeared
    val productUrl: List<String>, // List of urls from where the product can be purchased
    val price: Int // the average price of the product
)
```

That’s it! All your videos are now tracked for apparel objects. You can enable/disable annotations for certain videos from your developer dashboard.
  
