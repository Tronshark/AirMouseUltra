<p align="center">
  <img alt="AirMouse Ultra logo" src="app/src/main/res/mipmap-xxxhdpi/ic_launcher.webp" width="128" height="128" />
</p>

# AirMouse Ultra

AirMouse Ultra is an app that allows you to use the S Pen available on certain Galaxy devices to
control the phone's on-screen cursor using air gestures.

## How does it work?

The app uses the gyroscope built into the S Pen to track its movements. It then displays an
on-screen mouse cursor overlay using Android's Accessibility API. When the S Pen button is pressed, the
app simulates a finger tap on the screen.

One important note: due to limitations of the Accessibility API, all actions are deferred - meaning
they are only executed after the S Pen gesture is completed. For example, if you hold the S Pen
button and draw a circle in the air, the app will simulate that motion only after you release the
button, not while you're drawing it.

### Features

In addition to the mouse control mentioned earlier, the app also allows you to:

- Adjust the cursor size
- Change the mouse sensitivity
- Automatically hide the cursor after a period of inactivity so it doesn't get in the way
- Turn off the S Pen gyroscope after a period of inactivity to save battery
- Change the cursor icon — the app includes three default icons, and you can also set a custom one

The app also includes a detailed setup guide with videos explaining how to prepare your phone for
using the S Pen as a mouse. This is especially useful because some of the S Pen's built-in features
need to be disabled to prevent them from interfering with AirMouse Ultra.

## Supported devices

<ins>**This app works ONLY on Samsung phones that have an S Pen
with [Air Actions](https://www.samsung.com/us/support/answer/ANS10003221/) functionality.**</ins>
Even if your device has a stylus but is not a Samsung device, AirMouse Ultra will not work, as it
uses Samsung's SDK specifically for S Pen.

- Samsung Galaxy Note Series: **Galaxy Note10**
- Samsung Galaxy S Ultra Series: **Galaxy S20-S24 Ultra**
- Samsung Galaxy Tab Series: **Galaxy Tab S6** and newer *(Air Actions depend on the specific S Pen
  model)*

It also requires Android 12 or newer.

The S Pen SDK is quite a niche tool and has long been forgotten by Samsung. In fact, in their latest
flagships phones like **Samsung S25 Ultra and S26 Ultra**, they removed both Bluetooth connectivity and 
the gyroscope from the S Pen. So keep in mind that the app might work poorly, have bugs, or **not work** at all. Unfortunately,
it's a bit random and depends on both the pen and the phone - don't say I didn't warn you!

## Download

You can download AirMouse Ultra from
the [Releases](https://github.com/Tronshark/AirMouseUltra/releases) section here on GitHub.

## Building the Project

1. Clone the repository:
   ```bash
   git clone https://github.com/Tronshark/AirMouseUltra.git
   ```
2. Open Android Studio and click `File > New > Import Project` and select the AirMouseUltra
   repository folder.
3. Select the app module from the run configurations dropdown and click the Run button

> The app uses Firebase for analytics and crash tracking. To run the app from source, you need to
> place the `google-services.json` config file in the `app` folder or remove the Firebase dependencies
> from the app.

### Used Libraries

- Jetpack Compose - Declarative UI toolkit for building native Android apps
- Hilt - Dependency injection framework
- [Haze](https://github.com/chrisbanes/haze) by [@chrisbanes](https://github.com/chrisbanes) -
  Real-time blur effects for Jetpack Compose
- [S Pen Remote SDK](https://developer.samsung.com/galaxy-spen-remote/overview.html) - Samsung's SDK
  for accessing motion and button input from the S Pen
- [Compose Richtext](https://github.com/halilozercan/compose-richtext)
  by [Halil Ozercan](https://github.com/halilozercan)
- Android Accessibility Service API - Used to simulate touch events and show custom overlays

## Legal

"Samsung" and "S Pen" are registered trademarks of Samsung Electronics Co., Ltd.</br>
This app is not affiliated with, endorsed, or sponsored by Samsung Electronics Co., Ltd.
 
## inspired and source code collected from git clone 
   ```bash
   git clone https://github.com/jojczak/PenMouseS.git
   ```
you can find his app from playstore

but I added and removed some feature from his app according to my need. 

## Speciallity
I added an Instagram, YouTube, TikTok reels scrolling option by using only s pen as a remote control,
without directly interacting to the display, only pressing the button in the s pen
1.     single press action
2.     double press action
3.     long press action
using this option, you can customize to 
* 1. scroll up, 
* 2. scroll down, 
* 3. click on the center screen (pause the reels)

 since ultra phone are heavy, I place them on the table, using the s pen to watch insta reels.

you won't find this app on playstore right now!
if you have any suggestion to improve these apps' functionality. leave your feedback inside the apps
suggestion box 
 
## donate
<a href="https://www.buymeacoffee.com/tronshark" target="_blank"><img src="https://cdn.buymeacoffee.com/buttons/v2/default-yellow.png" alt="Buy Me a Coffee" style="height: 30px !important;width: 108px !important;" ></a>



**[Bye me a coffee](https://buymeacoffee.com/tronshark)**