# RxDownload

A multi-threaded download tool written with RxJava and Kotlin

## How to Use

### Preparation

Step 1. Add the JitPack repository to your build file

```gradle
allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}
```

Step 2. Add the dependency

```gradle
dependencies{
    implementation 'com.github.Deishelon:RxDownload:1.3.0'
}
```

Step 3. Add permissions

```xml
<uses-permission android:name="android.permission.INTERNET"/>
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
```


### Usage

Step 1. Create a mission

```kotlin
val mission = Mission(URL, FILE_NAME, SAVE_PATH, IS_OVERRIDE)
```


Step 2. Subscribe to the mission updates

```kotlin
val disposable = RxDownload.create(mission, autoStart = false)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { status ->

                }
```

> Note: The mission status is received here
> Repeated calls **DO NOT** cause the task to be created more than once
> Don't forget to dispose `disposable` in `onDestroy()` or similar method (To dispose call `dispose(disposable)` )


Step 3. Start download

```kotlin
RxDownload.start(mission).subscribe()
```

Stop download

```kotlin
RxDownload.stop(mission).subscribe()
```


### AutoStart Download

There are two simple ways:

- Add the autoStart parameter to the **create** method，this only take effect for the current mission.

```kotlin
RxDownload.create(mission,autoStart)
       .subscribe{
           ...
       }
```

- Enable AutoStart config, this will take effect for all missions.
```kotlin
DownloadConfig.Builder.create(context)
                  .enableAutoStart(true)

DownloadConfig.init(builder)
```

For more APIs please see RxDownload.kt


### Override
You can configure `Mission` so that if file exists it will be re-downloaded.
If not specified - false

```kotlin
val mission = Mission(...)
mission.overwrite = Boolen (true/false)
```

### Pretty format
Ypu can download status with n decimal point

Default is 1 decimal point

```kotlin
val percent = status.percentPretty(INT_DECIMAL_POINT_FORMAT) // returns `String` i.e 12.2

val rawPercent = status.percent() // returns `Double`

val totalSize = status.formatTotalSize(INT_DECIMAL_POINT_FORMAT) // returns `String` i.e 22.2 MB

val dwnSoFarSize = status.formatDownloadSize(INT_DECIMAL_POINT_FORMAT) // returns `String` i.e 44.4 MB

val progress = status.formatString(INT_DECIMAL_POINT_FORMAT) // returns `String` i.e 44.4 MB / 102.3 MB
```


### Configuration (optional)

Add your configuration when APP starts up, like this:

```kotlin
class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        val builder = DownloadConfig.Builder.create(this)
                .enableDb(true)
                .enableNotification(true)
				...

        DownloadConfig.init(builder)
    }
}
```

or

```kotlin
DownloadConfig.Builder.create(this)
                .setDefaultPath("custom download path")     //Set the default download address
                .enableDb(true)     //Enable the database
                .setDbActor(CustomSqliteActor(this))    //Customize the database
                .enableService(true)    //Enable Service
                .useHeadMethod(true)    //Use http HEAD method.
                .setMaxRange(10)       // Maximum concurrency for each mission.
                .setRangeDownloadSize(4*1000*1000) //The size of each Range，unit byte
                .setMaxMission(3)      // The number of mission downloaded at the same time
                .enableNotification(true)   //Enable Notification
                .setNotificationFactory(NotificationFactoryImpl())      //Custom notification
                .setOkHttpClientFacotry(OkHttpClientFactoryImpl())      //Custom OKHTTP
                .addExtension(ApkInstallExtension::class.java)    //Add extension
```

### Extension

Customize your exclusive operation

```kotlin
class CustomExtension:Extension {
    override fun init(mission: RealMission) {
        //Init
    }

    override fun action(): Maybe<Any> {
        //Your action
    }
}
```

> Refer to the ApkInstallExtension code

### Proguard

Add Retrofit and OKHTTP can be proguard

```gradle
-dontnote retrofit2.Platform
-dontwarn retrofit2.Platform$Java8
-keepattributes Signature
-keepattributes Exceptions

-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn javax.annotation.**
```

### Credit
A huge thanks to [ssseasonnn/RxDownload](https://github.com/ssseasonnn/RxDownload) who is the original author of this awesome library.
I did some modification to the library, that can satisfy my special use case.
> Show your support to the original library :)