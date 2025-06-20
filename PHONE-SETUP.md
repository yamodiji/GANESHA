# 📱 Phone Setup for USB Debugging

## Enable Developer Options
1. **Settings** → **About Phone**
2. **Tap "Build Number" 7 times** (until you see "Developer mode enabled")
3. **Go back** to main Settings

## Enable USB Debugging  
1. **Settings** → **Developer Options**
2. **Turn ON "USB Debugging"**
3. **Turn ON "Install via USB"** (if available)
4. **Turn ON "Stay awake"** (optional - keeps screen on while charging)

## Connect Your Phone
1. **Connect via USB cable** (must be data cable, not charging-only)
2. **Select "File Transfer (MTP)" mode** when prompted
3. **Allow USB Debugging** when dialog appears
4. **Check "Always allow from this computer"**

## Test Connection
Run this in your project folder:
```
adb devices
```

You should see your device ID listed (not "unauthorized")

✅ **Ready for app installation!** 