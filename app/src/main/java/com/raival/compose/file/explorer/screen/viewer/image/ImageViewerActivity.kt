package com.raival.compose.file.explorer.screen.viewer.image

import android.net.Uri
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import androidx.core.content.FileProvider
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
        return ImageViewerInstance(
            uri = uri,
            id = uid,
            parentDirectory = intent.extras?.getString("parent_directory")?.let { File(it) }
        )
    }

    override fun onReady(instance: ViewerInstance) {
        setContent {
            FileExplorerTheme {
                SafeSurface(enableStatusBarsPadding = false) {
                    val imageViewerInstance = instance as ImageViewerInstance

                    LaunchedEffect(imageViewerInstance.parentDirectory) {
                        if (imageViewerInstance.imageList.isEmpty()) {
                            withContext(Dispatchers.IO) {  // ✅ IO en vez de Default para I/O de disco
                                imageViewerInstance.parentDirectory?.let { dir ->
                                    val imageExtensions = setOf(
                                        "jpg", "jpeg", "png", "gif", "webp", "bmp", "heic", "heif"
                                    )
                                    val images: List<Uri> = dir
                                        .listFiles { file ->
                                            file.isFile && file.extension.lowercase() in imageExtensions
                                        }
                                        ?.sortedBy { it.name.lowercase() }
                                        ?.map { file ->
                                            // ✅ FileProvider en vez de Uri.fromFile() (deprecated)
                                            FileProvider.getUriForFile(
                                                this@ImageViewerActivity,
                                                "${packageName}.provider",
                                                file
                                            )
                                        }
                                        ?: emptyList()

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