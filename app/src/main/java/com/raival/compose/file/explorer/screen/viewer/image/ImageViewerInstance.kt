package com.raival.compose.file.explorer.screen.viewer.image

import android.net.Uri
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import com.raival.compose.file.explorer.screen.viewer.ViewerInstance
import java.io.File

class ImageViewerInstance(
    override val uri: Uri,
    override val id: String,
    val parentDirectory: File? = null
) : ViewerInstance {
    private val _imageList = mutableStateListOf<Uri>()
    val imageList: List<Uri> get() = _imageList

    private val _currentImageIndex = mutableIntStateOf(0)
    val currentImageIndex: Int get() = _currentImageIndex.value

    var isLoadingImages by mutableStateOf(false)
        private set

    fun setImages(uris: List<Uri>) {
        _imageList.clear()
        _imageList.addAll(uris)
        
        // Find and set current image index
        val currentIndex = _imageList.indexOfFirst { it == uri }
        if (currentIndex != -1) {
            _currentImageIndex.value = currentIndex
        }
    }

    fun setCurrentImageIndex(index: Int) {
        if (index in _imageList.indices) {
            _currentImageIndex.value = index
        }
    }

    fun getCurrentImage(): Uri = if (currentImageIndex in _imageList.indices) {
        _imageList[currentImageIndex]
    } else {
        uri
    }

    fun hasNextImage(): Boolean = currentImageIndex < _imageList.size - 1

    fun hasPreviousImage(): Boolean = currentImageIndex > 0

    fun nextImage(): Uri? = if (hasNextImage()) {
        _currentImageIndex.value++
        _imageList[currentImageIndex]
    } else null

    fun previousImage(): Uri? = if (hasPreviousImage()) {
        _currentImageIndex.value--
        _imageList[currentImageIndex]
    } else null

    override fun onClose() {
        _imageList.clear()
    }
}
