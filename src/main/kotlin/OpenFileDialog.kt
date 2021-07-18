import java.awt.FileDialog
import java.awt.Frame
import java.io.File

// https://www.reddit.com/r/Kotlin/comments/n16u8z/desktop_compose_file_picker/gwceshv?utm_source=share&utm_medium=web2x&context=3
fun openFileDialog(frame: Frame, title: String, allowedExtensions: List<String>, allowMultiSelection: Boolean = true): Set<File> {
    return FileDialog(frame, title, FileDialog.LOAD).apply {
        isMultipleMode = allowMultiSelection

        // windows
        file = allowedExtensions.joinToString(";") { "*$it" } // e.g. '*.jpg'

        // linux
        setFilenameFilter { _, name ->
            allowedExtensions.any {
                name.endsWith(it)
            }
        }

        isVisible = true
    }.files.toSet()
}
