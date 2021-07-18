import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Slider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun playController(
    sequenceLength: Float,
    isPlaying: Boolean,
    currentPosition: Float,
    onClickTogglePlaying: () -> Unit,
    onCurrentPositionChanged: (Float) -> Unit
) {
    Row {
        Button(onClickTogglePlaying) {
            Text(if (isPlaying) "Stop" else "Play")
        }
        Slider(
            value = currentPosition,
            onValueChange = onCurrentPositionChanged,
            valueRange = 0f..sequenceLength,
            modifier = Modifier.align(Alignment.CenterVertically)
        )
    }
}