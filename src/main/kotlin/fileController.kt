import androidx.compose.desktop.AppManager
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun fileController(selectedFilePath: String?, onSelected: (path: String) -> Unit) {

    val onClickOpen = fun () {
        val frame = AppManager.focusedWindow?.window ?: return
        val result = openFileDialog(frame, "", listOf(".mid", ".MID"))
        if (result.isNotEmpty()) {
            onSelected(result.first().absolutePath)
        }
    }

    Row() {
        Button(onClickOpen) {
            Text("Open")
        }
        Text(selectedFilePath ?: "<not selected>", modifier = Modifier.align(Alignment.CenterVertically))
    }
}