# HelloGlasses 🥽

A learning-in-public log of how I'm building **Android apps for display glasses** with the Jetpack XR SDK.

Every step of the journey lives on its own branch, so you can check out any branch and see exactly what the app looked like at that point.

## How it works

A glasses app doesn't run *on* the glasses. It runs on the **phone**, and a *projected activity* draws its UI on the glasses display:

```
Phone (MainActivity)  ──startActivity + projected options──▶  Glasses (GlassesActivity)
Material 3 UI                                                  Jetpack Compose Glimmer UI
```

Everything here runs on the Android Studio **glasses emulator paired with a phone emulator**. I don't use real hardware.

## The journey

`main` always has the latest step I've finished. Each numbered branch is a snapshot that doesn't change after it's done.

| # | Branch | Status | What I built | What I learned |
|---|---|---|---|---|
| 1 | [`01-hello-glasses`](../../tree/01-hello-glasses) | ✅ Done | Phone screen with an **Open on Glasses** button that projects a Glimmer "Hello Glasses" screen. The button is enabled only while glasses are connected, and a status line updates live. | Projected activities, `ProjectedContext`, and collecting `isProjectedDeviceConnected` as a lifecycle-aware Flow |
| 2 | [`02-glasses-permissions`](../../tree/02-glasses-permissions) | ✅ Done | An **Ask for Camera** button on the glasses that requests CAMERA with `ProjectedActivityCompat` | The dialog appears on the **phone**, and the glasses show "check your phone". The grant belongs to the glasses only, so the phone's own CAMERA stays denied. The emulator's touchpad is the strip *under* the display. |
| 3 | [`03-count-contexts`](../../tree/03-count-contexts) | ✅ Done | A phone status line: device lifecycle state, a projected-context counter and the `deviceId` | Taking the glasses off keeps the same context. Only a reconnect makes a new one, with a new `deviceId`. The emulator can't detect wearing, so sleep stays `STARTED`. |
| 4 | [`04-touchpad-focus`](../../tree/04-touchpad-focus) | ✅ Done | A Glimmer `ButtonGroup` (Hello, Camera, Close) that you drive with the touchpad | Tap confirms the focused button, and a swipe moves focus one button with no wrap-around. The wake button belongs to the system. Initial focus goes on the cheapest action. |
| 5 | `05-display-state` | 🚧 In progress | A seconds counter that pauses while the glasses display is off | |

## Tech stack

- Kotlin + Jetpack Compose
- [Jetpack XR Projected](https://developer.android.com/develop/xr/jetpack-xr-sdk): launches the activity on the glasses and checks whether they're connected
- [Jetpack Compose Glimmer](https://developer.android.com/develop/xr/jetpack-xr-sdk/glimmer): the UI toolkit for glasses

Projected and Glimmer are **alpha** libraries, so their APIs may change. Exact versions are in [`gradle/libs.versions.toml`](gradle/libs.versions.toml).

## Run it yourself

1. Install an Android Studio build that has glasses AVDs. As of 2026-11, the glasses setup pages say to use Canary.
2. In Device Manager, create:
   - a **display glasses** AVD
   - a **phone** AVD using the *API CANARY Preview* image
3. In the glasses AVD's overflow menu, choose **Pair Glasses** and pick the phone AVD.
4. Start the phone AVD first, then the glasses AVD.
5. Run `app` on the **phone** and tap **Open on Glasses**.

📖 [Run on the glasses emulator (official docs)](https://developer.android.com/develop/xr/jetpack-xr-sdk/run/emulator/glasses)

## Requirements

- compileSdk 37, minSdk 36
- Apple Silicon or x86 machine with 16 GB RAM or more. Running two emulators at once uses a lot of memory.
