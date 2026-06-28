package com.raival.compose.file.explorer.screen.viewer.image

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import androidx.documentfile.provider.DocumentFile

/**
 * Helper class to navigate between images in a directory
 */
class ImageNavigationHelper(private val context: Context) {

    /**
     * Get the list of image files from the same directory as the current image
     */
    fun getImagesInDirectory(currentUri: Uri): Pair<List<Uri>, Int> {
        return try {
            val images = mutableListOf<Uri>()
            var currentIndex = -1

            // Try to get from DocumentFile (content:// URIs)
            val docFile = DocumentFile.fromSingleUri(context, currentUri)
            if (docFile != null && docFile.exists()) {
                val parentUri = docFile.parentFile?.uri
                if (parentUri != null) {
                    val parentDocFile = DocumentFile.fromTreeUri(context, parentUri)
                    parentDocFile?.listFiles()?.forEach { file ->
                        if (file.type?.startsWith("image/") == true && file.isFile) {
                            val index = images.size
                            images.add(file.uri)
                            if (file.uri == currentUri) {
                                currentIndex = index
                            }
                        }
                    }
                }
            }

            // If no images found, try MediaStore
            if (images.isEmpty()) {
                images.addAll(getImagesFromMediaStore(currentUri))
                currentIndex = images.indexOf(currentUri)
            }

            Pair(images.sorted(), maxOf(0, currentIndex))
        } catch (e: Exception) {
            e.printStackTrace()
            Pair(listOf(currentUri), 0)
        }
    }

    /**
     * Get images from MediaStore
     */
    private fun getImagesFromMediaStore(currentUri: Uri): List<Uri> {
        val images = mutableListOf<Uri>()
        try {
            val projection = arrayOf(MediaStore.Images.Media._ID)
            val cursor = context.contentResolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection,
                null,
                null,
                "${MediaStore.Images.Media.DATE_MODIFIED} DESC"
            )

            cursor?.use {
                val columnIndex = it.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
                while (it.moveToNext()) {
                    val id = it.getLong(columnIndex)
                    val uri = Uri.withAppendedPath(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        id.toString()
                    )
                    images.add(uri)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return images
    }

    /**
     * Get the next image URI
     */
    fun getNextImage(currentUri: Uri): Uri? {
        val (images, currentIndex) = getImagesInDirectory(currentUri)
        return if (currentIndex < images.size - 1) {
            images[currentIndex + 1]
        } else {
            null
        }
    }

    /**
     * Get the previous image URI
     */
    fun getPreviousImage(currentUri: Uri): Uri? {
        val (images, currentIndex) = getImagesInDirectory(currentUri)
        return if (currentIndex > 0) {
            images[currentIndex - 1]
        } else {
            null
        }
    }
}
