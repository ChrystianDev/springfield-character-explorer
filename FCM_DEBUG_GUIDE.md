# FCM Push Notifications Debugging Guide

## 🚨 Most Common Issue: Background vs Foreground

### **THE KEY PROBLEM:**
When your app is in the **BACKGROUND** and you send a **notification payload** from Firebase Console, `onMessageReceived()` is **NOT called**!

### How FCM Works:

| App State | Notification Type | onMessageReceived Called? | Where Handled? |
|-----------|------------------|---------------------------|----------------|
| **FOREGROUND** | Notification payload | ✅ YES | Your code |
| **FOREGROUND** | Data-only payload | ✅ YES | Your code |
| **BACKGROUND** | Notification payload | ❌ NO | System tray |
| **BACKGROUND** | Data-only payload | ✅ YES | Your code |

## 🔍 Step-by-Step Debugging

### Step 1: Verify FCM Token

1. **Build and run the app**
2. **Check Logcat** for:
   ```
   SimpsonsInfoApp: === FCM TOKEN RETRIEVED SUCCESSFULLY ===
   SimpsonsInfoApp: Token: [your-token-here]
   ```
3. **Alternative**: Tap "Show Token" button in app
4. **Copy the token** - you'll need it!

### Step 2: Test with App in FOREGROUND

#### Test A: Firebase Console (Notification Composer)

1. Keep your app **OPEN** and in foreground
2. Go to Firebase Console → Cloud Messaging
3. Click "Send your first message"
4. Enter:
   - **Notification title**: "Test Foreground"
   - **Notification text**: "This is a foreground test"
5. Click "Send test message"
6. Paste your FCM token
7. Click "Test"

**Expected Logcat output:**
```
SimpsonsMessagingService: ========================================
SimpsonsMessagingService: === MESSAGE RECEIVED ===
SimpsonsMessagingService: ========================================
SimpsonsMessagingService: From: [sender-id]
SimpsonsMessagingService: --- Notification Payload ---
SimpsonsMessagingService: Title: Test Foreground
SimpsonsMessagingService: Body: This is a foreground test
SimpsonsMessagingService: Showing notification from notification payload...
SimpsonsMessagingService: sendNotification() called
SimpsonsMessagingService: Notification displayed successfully!
```

### Step 3: Test with App in BACKGROUND

#### Test B: Send Notification Payload (Will NOT call onMessageReceived)

1. **Close your app** or press Home button
2. Send same notification from Firebase Console
3. **Expected behavior:**
   - Notification appears in system tray automatically
   - `onMessageReceived()` is **NOT called**
   - **NO logs in Logcat**

This is **NORMAL BEHAVIOR** - not a bug!

#### Test C: Send Data-Only Payload (WILL call onMessageReceived)

1. **Close your app** or press Home button
2. Go to Firebase Console → Cloud Messaging
3. Click "New campaign" → "Firebase Notification messages"
4. Enter notification details
5. Click "Next" until you reach "Additional options"
6. Under "Custom data", add:
   ```
   Key: title, Value: Background Test
   Key: body, Value: This is a data-only test
   Key: type, Value: test
   ```
7. **IMPORTANT:** Delete the notification title and body fields (leave them empty)
8. Send to your device token

**Expected Logcat output:**
```
SimpsonsMessagingService: === MESSAGE RECEIVED ===
SimpsonsMessagingService: No notification payload
SimpsonsMessagingService: --- Data Payload ---
SimpsonsMessagingService: title: Background Test
SimpsonsMessagingService: body: This is a data-only test
SimpsonsMessagingService: type: test
SimpsonsMessagingService: Showing notification from data payload...
```

## 🧪 Logcat Filters

Use these filters to see FCM logs:

```bash
# All FCM related logs
adb logcat | grep -E "(SimpsonsMessagingService|SimpsonsInfoApp|FCM|Firebase)"

# Just your service
adb logcat | grep "SimpsonsMessagingService"

# Just token retrieval
adb logcat | grep "FCM TOKEN"

# All Firebase
adb logcat -s FirebaseMessaging
```

## ✅ Verification Checklist

### 1. Service Registration
Check if service is in AndroidManifest.xml:
```xml
<service
    android:name=".notification.SimpsonsMessagingService"
    android:exported="false">
    <intent-filter>
        <action android:name="com.google.firebase.MESSAGING_EVENT" />
    </intent-filter>
</service>
```

### 2. Google Play Services
- Ensure Google Play Services is installed on device
- Update to latest version if needed

### 3. Notification Permission (Android 13+)
- Check if POST_NOTIFICATIONS permission is granted
- Go to Settings → Apps → Your App → Permissions → Notifications

### 4. FCM Enabled in Firebase Console
- Go to Firebase Console → Project Settings → Cloud Messaging
- Verify Cloud Messaging API is enabled

### 5. google-services.json
- File must be in `app/` directory
- Must match your Firebase project
- Rebuild after any changes

## 🐛 Common Issues & Solutions

### Issue 1: Token is null or empty
**Solution:**
- Check internet connection
- Verify google-services.json is correct
- Clear app data and reinstall
- Check Google Play Services is up to date

### Issue 2: onMessageReceived not called (background)
**Solution:**
- **This is expected!** Use data-only messages or keep app in foreground

### Issue 3: Notification not showing
**Solution:**
- Check notification permission granted
- Verify notification channels created
- Check notification is not being blocked by system

### Issue 4: Wrong token being sent
**Solution:**
- Clear app data
- Uninstall and reinstall
- Get fresh token from Logcat

## 📱 Testing on Different Android Versions

| Android Version | Notes |
|-----------------|-------|
| 13+ (API 33+) | Requires POST_NOTIFICATIONS permission |
| 8.0+ (API 26+) | Requires notification channels |
| 7.0 and below | Works without special permissions |

## 🔧 Manual Testing Script

```bash
# 1. Clear app data
adb shell pm clear com.mobilitydevai.sce

# 2. Install app
./gradlew installDebug

# 3. Start app
adb shell am start -n com.mobilitydevai.sce/.MainActivity

# 4. Get token from logcat
adb logcat | grep "FCM TOKEN RETRIEVED"

# 5. Monitor for messages
adb logcat | grep "SimpsonsMessagingService"
```

## 🎯 Expected Behavior Summary

1. **App launches** → Token is generated and logged
2. **Foreground + Notification** → `onMessageReceived` called ✅
3. **Background + Notification** → System handles, NO `onMessageReceived` ❌
4. **Background + Data-only** → `onMessageReceived` called ✅
5. **Notification shown** → Logs confirm display ✅

## 📊 Debugging Logs Reference

Look for these specific log patterns:

### Service Created:
```
SimpsonsMessagingService: === SimpsonsMessagingService CREATED ===
```

### Token Retrieved:
```
SimpsonsInfoApp: === FCM TOKEN RETRIEVED SUCCESSFULLY ===
SimpsonsInfoApp: Token: [token-string]
```

### Message Received:
```
SimpsonsMessagingService: === MESSAGE RECEIVED ===
SimpsonsMessagingService: From: [sender]
SimpsonsMessagingService: --- Notification Payload ---
SimpsonsMessagingService: Title: [title]
SimpsonsMessagingService: Body: [body]
```

### Notification Displayed:
```
SimpsonsMessagingService: sendNotification() called
SimpsonsMessagingService: Notification displayed successfully!
```

## 💡 Pro Tips

1. **Always test in foreground first** - easier to debug
2. **Use data-only messages for background** - more control
3. **Check token matches** - copy from Logcat, not UI
4. **Clear app data** if token seems wrong
5. **Use Logcat filters** to reduce noise
6. **Test on real device** - emulator may have issues

## 🚀 Next Steps

Once debugging:
1. Implement server-side token storage
2. Add notification topics for broadcasting
3. Implement deep linking from notifications
4. Add notification actions (reply, dismiss, etc.)
5. Implement notification grouping
