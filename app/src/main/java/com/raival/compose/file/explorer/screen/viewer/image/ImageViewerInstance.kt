package com.raival.compose.file.explorer.screen.viewer.image

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.raival.compose.file.explorer.screen.viewer.ViewerInstance
import java.io.File

class ImageViewerInstance(
    override val uri: Uri,
    override val id: String,
    val parentDirectory: File? = null
) : ViewerInstance() { 

    private val _imageList = mutableStateListOf<Uri>()
    val imageList: List<Uri> get() = _imageList

    private var _currentImageIndex by mutableIntStateOf(0)
    val currentImageIndex: Int get() = _currentImageIndex

    var isLoadingImages by mutableStateOf(false)
        private set

    fun setImages(uris: List<Uri>) {
        _imageList.clear()
        _imageList.addAll(uris)

        val currentIndex = _imageList.indexOfFirst { it == uri }
        if (currentIndex != -1) {
            _currentImageIndex = currentIndex
        }
    }

    fun setCurrentImageIndex(index: Int) {
        if (index in _imageList.indices) {
            _currentImageIndex = index
        }
    }

    fun getCurrentImage(): Uri = if (_currentImageIndex in _imageList.indices) {
        _imageList[_currentImageIndex]
    } else {
        uri
    }

    fun hasNextImage(): Boolean = _currentImageIndex < _imageList.size - 1

    fun hasPreviousImage(): Boolean = _currentImageIndex > 0

    fun nextImage(): Uri? = if (hasNextImage()) {
        _currentImageIndex++
        _imageList[_currentImageIndex]
    } else null

    fun previousImage(): Uri? = if (hasPreviousImage()) {
        _currentImageIndex--
        _imageList[_currentImageIndex]
    } else null

    override fun onClose() {
        _imageList.clear()
    }
}