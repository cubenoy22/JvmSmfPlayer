import javax.sound.midi.MidiDevice
import javax.sound.midi.MidiSystem
import javax.sound.midi.Sequence

class SmfPlayer {

    private val sequencer = MidiSystem.getSequencer(false)

    val isPlaying: Boolean
        get() = sequencer.isRunning

    var position: Float
        get() = sequencer.microsecondPosition / 1000f
        set(value) {
            sequencer.microsecondPosition = (value * 1000f).toLong()
        }

    val length: Float
        get() = sequencer.microsecondLength / 1000f

    var device: MidiDevice? = null
        set(value) {
            if (field != value && field?.isOpen == true) {
                field?.close()
            }
            field = value
        }

    var sequence: Sequence?
        get() = sequencer.sequence
        set(value) {
            if (!sequencer.isOpen) {
                sequencer.open()
            }
            sequencer.sequence = value
        }

    fun start() {
        if (!sequencer.isOpen) {
            sequencer.open()
        }
        if (sequencer.transmitter.receiver != device?.receiver) {
            sequencer.transmitters.forEach { it.close() }
            sequencer.transmitter.receiver = device?.receiver
        }
        if (device?.isOpen == false) {
            device?.open()
        }
        sequencer.start()
    }

    fun stop() {
        sequencer.stop()
    }
}