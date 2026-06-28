# Image Swipe Navigation Feature

## 📋 Overview

This feature adds **swipe gesture navigation** to the Image Viewer, allowing users to seamlessly navigate between images in the same directory by swiping left (next image) or right (previous image).

## ✨ What's New

### Features Implemented:
- ✅ **Horizontal Swipe Detection**: Detect left/right swipe gestures with a 50px threshold
- ✅ **Image Navigation**: Automatically navigate to next/previous image in the directory
- ✅ **State Management**: Maintains rotation and other viewer states separately per image
- ✅ **Intelligent Image Discovery**: Finds all images in the same directory using both DocumentFile and MediaStore APIs
- ✅ **Smooth Transitions**: Seamless image switching with automatic state reset

## 🔧 How It Works

### Components Added:

1. **ImageNavigationHelper.kt** - Core navigation logic
   - Scans directory for image files
   - Provides next/previous image URIs
   - Supports both DocumentFile (content://) and MediaStore URIs

2. **ImageGestureHandler.kt** - Gesture detection
   - `swipeGestureHandler()` modifier for detecting horizontal drags
   - `ImagePagerState` data class for state management
   - 50px threshold to prevent accidental swipes

3. **ImageViewerScreen.kt** - Updated UI integration
   - Added `currentImageUri` state to track current image
   - Integrated swipe handler on the image display
   - Auto-reset rotation angle when switching images
   - Re-load image dimensions on image change

## 🎮 User Experience

### Swipe Actions:
- **Swipe Left** → Next image (→)
- **Swipe Right** → Previous image (←)
- If no next/previous image exists, nothing happens

### Behavior:
- Rotation angle resets when switching images
- Image dimensions reload for new image
- All other controls remain functional
- Tap to toggle control visibility still works
- Edit, rotate, and zoom features per image

## 📦 Files Modified

```
app/src/main/java/com/raival/compose/file/explorer/screen/viewer/image/
├── ImageNavigationHelper.kt (NEW)
└── ui/
    ├── ImageGestureHandler.kt (NEW)
    └── ImageViewerScreen.kt (MODIFIED)
```

## 🚀 Building & Testing

### Prerequisites:
- JDK 17 or higher
- Android SDK API level 34+
- Kotlin 1.9+
- Jetpack Compose latest stable

### Build Steps:

```bash
# 1. Clone the repository (if you haven't already)
git clone https://github.com/Background-blue/Prism-File-Explorer.git
cd Prism-File-Explorer

# 2. Checkout the feature branch
git checkout feature/image-swipe-navigation

# 3. Build the APK
./gradlew assembleDebug

# 4. Find the APK
# Output: app/build/outputs/apk/debug/app-debug.apk

# Or build release APK
./gradlew assembleRelease
# Output: app/build/outputs/apk/release/app-release.apk
```

### Installation on Device/Emulator:

```bash
# Install debug APK
adb install app/build/outputs/apk/debug/app-debug.apk

# Or release APK
adb install app/build/outputs/apk/release/app-release.apk
```

## 🧪 Testing Instructions

### Manual Testing:

1. **Open Image Viewer**
   - Navigate to a folder with multiple images
   - Open any image in the viewer

2. **Test Swipe Left (Next)**
   - Place your finger on the image
   - Drag horizontally to the left (≥50px)
   - Image should change to the next one

3. **Test Swipe Right (Previous)**
   - Place your finger on the image
   - Drag horizontally to the right (≥50px)
   - Image should change to the previous one

4. **Test Edge Cases**
   - Swipe at first image → Should not navigate
   - Swipe at last image → Should not navigate
   - Rotate image → Swipe to next → Rotation should reset
   - Zoom in → Swipe → Should still navigate

5. **Test Control Functions**
   - Edit image (swipe to test)
   - Rotate image then swipe
   - Change background colors
   - View image info
   - Change content scale

## 🐛 Known Limitations

- Image discovery scans the directory every time (consider caching in future versions)
- Only works with images (filtered by MIME type image/*)
- Threshold is fixed at 50px (could be made configurable)

## 🔮 Future Enhancements

- [ ] Add animation transition between images
- [ ] Add counter (e.g., "3/10") to show position
- [ ] Add configuration option for swipe sensitivity
- [ ] Add circular navigation (loop at end)
- [ ] Add velocity-based swipe speed detection
- [ ] Cache image list for faster navigation

## 📝 Notes

- The swipe handler uses Compose's `detectHorizontalDragGestures` for smooth, native gesture handling
- Image navigation is directory-aware using Android's file system APIs
- Supports both modern (DocumentFile) and legacy (MediaStore) URI schemes

---

**Created**: 2026-06-28  
**Feature Branch**: `feature/image-swipe-navigation`  
**Status**: Ready for testing ✅
