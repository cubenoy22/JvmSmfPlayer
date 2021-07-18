import androidx.compose.desktop.Window
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.io.File
import java.util.*
import javax.sound.midi.*
import javax.swing.JOptionPane
import javax.swing.SwingUtilities

fun main() = Window(title = "MidiPlayer") {
    val padding = 16.dp
    val player by remember { mutableStateOf(SmfPlayer()) }
    var deviceInfoList by remember { mutableStateOf(emptyList<MidiDevice.Info>()) }
    var selectedFilePath by remember { mutableStateOf<String?>(null) }
    var sequenceLength by remember { mutableStateOf(0f) }
    var selectedDeviceIndex by remember { mutableStateOf(0) }
    var currentPosition by remember { mutableStateOf(0f) }
    var isPlaying by remember { mutableStateOf(false) }
    var updateTimer by remember { mutableStateOf<Timer?>(null) }

    val onClickTogglePlaying = fun () {
        if (player.isPlaying) {
            player.stop()
            isPlaying = false
        } else {
            runCatching {
                player.start()
                isPlaying = true
            }.onFailure {
                it.printStackTrace()
                JOptionPane.showMessageDialog(null, it.message)
            }
        }
    }

    val onCurrentPositionChanged = { pos: Float ->
        currentPosition = pos
        player.position = pos
    }

    DisposableEffect(null) {
        // TODO: デバイスの数が変わったら再取得したい
        deviceInfoList = MidiSystem.getMidiDeviceInfo().toList()
        onDispose {}
    }

    DisposableEffect(selectedDeviceIndex) {
        player.device = MidiSystem.getMidiDevice(deviceInfoList[selectedDeviceIndex])
        onDispose {}
    }

    DisposableEffect(selectedFilePath) {
        selectedFilePath?.let { path ->
            runCatching {
                player.sequence = MidiSystem.getSequence(File(path))
                sequenceLength = player.length
            }.onFailure {
                JOptionPane.showMessageDialog(null, it.message)
            }
        }
        onDispose {
            player.sequence = null
            isPlaying = false
        }
    }

    DisposableEffect(isPlaying) {
        if (isPlaying) {
            updateTimer = Timer().apply {
                scheduleAtFixedRate(object : TimerTask() {
                    override fun run() {
                        SwingUtilities.invokeLater {
                            currentPosition = player.position
                            isPlaying = player.isPlaying
                        }
                    }
                }, 0L, 100L)
            }
        }
        onDispose {
            updateTimer?.cancel()
            updateTimer = null
        }
    }

    MaterialTheme {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Box(modifier = Modifier.padding(padding)) {
                fileController(selectedFilePath) {
                    selectedFilePath = it
                }
            }
            Box(modifier = Modifier.padding(padding)) {
                playController(
                    sequenceLength,
                    isPlaying,
                    currentPosition,
                    onClickTogglePlaying,
                    onCurrentPositionChanged
                )
            }
            Box(modifier = Modifier.padding(padding)) {
                devicePicker(deviceInfoList, selectedDeviceIndex, !isPlaying) {
                    selectedDeviceIndex = it
                }
            }
        }
    }
}