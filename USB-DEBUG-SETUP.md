# 📱 USB Debugging Setup Guide

## 🚀 Quick Start (5 minutes)

### Step 1: Enable USB Debugging on Your Phone
1. **Open Settings** → **About Phone**
2. **Tap "Build Number" 7 times** (until "Developer mode enabled")
3. **Go back** → **Developer Options**
4. **Enable "USB Debugging"**
5. **Enable "Install via USB"** (if available)

### Step 2: Install ADB (if not installed)
Download Android SDK Platform Tools:
- **Windows**: https://dl.google.com/android/repository/platform-tools-latest-windows.zip
- Extract and add to PATH, or put `adb.exe` in this project folder

### Step 3: Connect Your Phone
1. **Connect phone via USB cable**
2. **Select "File Transfer" mode** when prompted
3. **Allow USB Debugging** when dialog appears on phone
4. **Trust this computer** (check "Always allow")

### Step 4: Test Connection
```batch
adb devices
```
You should see your device listed (not "unauthorized")

---

## 🎯 Build & Run Your App

### Option 1: Use the Auto Builder (Recommended)
```batch
build-and-run.bat
```

### Option 2: Manual Steps
```batch
# Build APK
build-project.bat

# Install on device  
adb install -r app\build\outputs\apk\debug\app-debug.apk

# Grant permissions
adb shell pm grant com.appdrawer.app android.permission.QUERY_ALL_PACKAGES

# Launch app
adb shell am start -n com.appdrawer.app/.MainActivity
```

---

## 🔧 Useful Commands

### App Management
```batch
# Install APK (replace existing)
adb install -r app-debug.apk

# Uninstall app
adb uninstall com.appdrawer.app

# Stop app
adb shell am force-stop com.appdrawer.app

# Clear app data
adb shell pm clear com.appdrawer.app
```

### Debugging
```batch
# View app logs
adb logcat -s "AppDrawer"

# View all logs (filtered)
adb logcat | findstr "AppDrawer"

# Monitor device activity
adb shell dumpsys activity activities
```

### Device Info
```batch
# List devices
adb devices -l

# Device properties
adb shell getprop

# Screen recording (Android 4.4+)
adb shell screenrecord /sdcard/demo.mp4
```

---

## 🚨 Troubleshooting

### Device Not Detected
- **Try different USB cable** (data cable, not charging-only)
- **Try different USB port**
- **Restart ADB**: `adb kill-server` then `adb start-server`
- **Check USB mode**: Should be "File Transfer" or "MTP"

### "Unauthorized" Device
- **Check phone for USB debugging dialog**
- **Revoke authorizations**: Developer Options → Revoke USB Debugging Authorizations
- **Reconnect and approve again**

### Installation Failed
- **Enable "Install via USB"** in Developer Options
- **Check available storage space**
- **Try uninstalling existing version first**

### App Crashes
- **Check logs**: `adb logcat -s "AppDrawer"`
- **Verify permissions were granted**
- **Try clearing app data and reinstalling**

---

## ✅ Your App Features

Your Android App Drawer includes:
- **Real-time app search** with fuzzy matching
- **Grid/List view** of all installed apps  
- **Long-press context menus** (rename, app info, etc.)
- **Custom app renaming** with persistent storage
- **Drag & drop** functionality for pinning apps
- **Material 3 design** with smooth animations
- **Optimized performance** with LazyVerticalGrid
- **Small APK size** with R8/ProGuard optimization

## 🎉 Ready to Test!

Run `build-and-run.bat` and your app will be built and installed automatically! 