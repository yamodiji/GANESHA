# 🚀 Easy App Deployment Guide

## Method 1: Use Pre-built APK (Fastest)

If an APK already exists in `app/build/outputs/apk/debug/`:

```batch
# Install directly
adb install -r app\build\outputs\apk\debug\app-debug.apk

# Grant permissions
adb shell pm grant com.appdrawer.app android.permission.QUERY_ALL_PACKAGES

# Launch app
adb shell am start -n com.appdrawer.app/.MainActivity
```

## Method 2: GitHub Actions Build (Recommended)

1. **Push your code to GitHub**
2. **GitHub Actions will automatically build the APK**
3. **Download the APK from GitHub Actions artifacts**
4. **Install using ADB**

## Method 3: Manual Build (if needed)

Since gradlew has wrapper issues, you can:

1. **Open in Android Studio** (just for building, not running)
2. **Build** → **Build Bundle(s) / APK(s)** → **Build APK(s)**
3. **Use the generated APK** with ADB commands above

## 🎯 Quick Commands

```batch
# Check if phone is connected
adb devices

# Install your app
adb install -r app-debug.apk

# View logs
adb logcat -s "AppDrawer"

# Uninstall if needed
adb uninstall com.appdrawer.app
```

## ✅ Your App Features

Once installed, your app provides:
- **Real-time search** of all installed apps
- **Grid/List view** with smooth scrolling
- **Long-press menus** for app actions
- **Custom app renaming** 
- **Drag & drop** to pin favorites
- **Material 3 design** with animations

**Ready to test your app!** 🎉 