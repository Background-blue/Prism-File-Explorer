package com.raival.compose.file.explorer.screen.viewer.image.ui

import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import kotlin.math.absoluteValue

/**
 * Detects horizontal swipe gestures to navigate between images
 * @param onSwipeLeft Callback when user swipes left (next image)
 * @param onSwipeRight Callback when user swipes right (previous image)
 */
fun Modifier.swipeGestureHandler(
    onSwipeLeft: () -> Unit,
    onSwipeRight: () -> Unit
): Modifier = pointerInput(Unit) {
    detectHorizontalDragGestures(
        onHorizontalDrag = { change, dragAmount ->
            change.consume()
            
            // Determine swipe direction based on drag amount
            // Negative dragAmount means swiping left, positive means swiping right
            when {
                dragAmount < -50 -> onSwipeLeft()  // Swipe left threshold: 50 pixels
                dragAmount > 50 -> onSwipeRight()   // Swipe right threshold: 50 pixels
            }
        }
    )
}

/**
 * Data class to hold image pager state
 */
data class ImagePagerState(
    val currentIndex: Int = 0,
    val imageCount: Int = 0,
    val canGoNext: Boolean = false,
    val canGoPrevious: Boolean = false
) {
    fun next(): ImagePagerState {
        return if (canGoNext) {
            copy(currentIndex = currentIndex + 1)
        } else {
            this
        }
    }

    fun previous(): ImagePagerState {
        return if (canGoPrevious) {
            copy(currentIndex = currentIndex - 1)
        } else {
            this
        }
    }
}
