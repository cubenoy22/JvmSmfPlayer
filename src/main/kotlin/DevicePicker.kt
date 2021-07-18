import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import javax.sound.midi.MidiDevice

@Composable
fun devicePicker(
    devices: List<MidiDevice.Info>,
    selectedDeviceIndex: Int,
    enabled: Boolean = true,
    onSelected: ((Int) -> Unit)? = null
) {
    var expanded by remember { mutableStateOf(false) }
    Row {
        Text("Output Device: ", modifier = Modifier.align(Alignment.CenterVertically))
        when {
            devices.isEmpty() -> Unit
            else -> {
                Column(modifier = Modifier.align(Alignment.CenterVertically)) {
                    OutlinedButton(
                        onClick = { expanded = !expanded },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = enabled
                    )  {
                        Text(devices[selectedDeviceIndex].name)
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        devices.forEachIndexed { index, s ->
                            DropdownMenuItem(onClick = {
                                onSelected?.invoke(index)
                                expanded = false
                            }) {
                                Text(s.name)
                            }
                        }
                    }
                }
            }
        }
    }
}