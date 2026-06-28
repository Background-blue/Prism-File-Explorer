package com.raival.compose.file.explorer.screen.viewer.image

import android.net.Uri
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import com.raival.compose.file.explorer.common.ui.SafeSurface
import com.raival.compose.file.explorer.screen.viewer.ViewerActivity
import com.raival.compose.file.explorer.screen.viewer.ViewerInstance
import com.raival.compose.file.explorer.screen.viewer.image.ui.ImageViewerScreen
import com.raival.compose.file.explorer.theme.FileExplorerTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class ImageViewerActivity : ViewerActivity() {
    override fun onCreateNewInstance(uri: Uri, uid: String): ViewerInstance {
        return ImageViewerInstance(uri, uid, intent.extras?.let { bundle ->
            val parentPath = bundle.getString("parent_directory")
            if (parentPath != null) File(parentPath) else null
        })
    }

    override fun onReady(instance: ViewerInstance) {
        setContent {
            FileExplorerTheme {
                SafeSurface(enableStatusBarsPadding = false) {
                    val imageViewerInstance = instance as ImageViewerInstance
                    
                    // Load images from parent directory
                    LaunchedEffect(imageViewerInstance.parentDirectory) {
                        if (imageViewerInstance.imageList.isEmpty()) {
                            withContext(Dispatchers.Default) {
                                imageViewerInstance.parentDirectory?.let { dir ->
                                    val imageExtensions = setOf("jpg", "jpeg", "png", "gif", "webp", "bmp")
                                    val images = dir.listFiles { file ->
                                        file.isFile && file.extension.lowercase() in imageExtensions
                                    }?.sortedBy { it.name }?.map { Uri.fromFile(it) } ?: emptyList()
                                    
                                    withContext(Dispatchers.Main) {
                                        imageViewerInstance.setImages(images)
                                    }
                                }
                            }
                        }
                    }
                    
                    ImageViewerScreen(instance)
                }
            }
        }
    }
}
