package net.raysforge.midi;

import java.io.File;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Synthesizer;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

import com.sun.media.sound.AudioSynthesizer;

public class SaveOneNoteAsWave {
	
	public static AudioInputStream getOneNoteAsAudioInputStream(int note, int velocity, double notePlayTimeInSeconds, double totalTimeInSeconds) {
		try {
			return getOneNoteAsAudioInputStream_(note, velocity, notePlayTimeInSeconds, totalTimeInSeconds);
		} catch (MidiUnavailableException | InvalidMidiDataException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static AudioInputStream getOneNoteAsAudioInputStream_(int note, int velocity, double notePlayTimeInSeconds, double totalTimeInSeconds) throws MidiUnavailableException, InvalidMidiDataException
	{
	    // Play the note Middle C (60) moderately loud
	    // (velocity = 93)on channel 4 (zero-based).

		ShortMessage noteOn = new ShortMessage(ShortMessage.NOTE_ON, 4, note, velocity);
		ShortMessage noteOff = new ShortMessage(ShortMessage.NOTE_OFF, 4, note, velocity);
	    Synthesizer synth = MidiSystem.getSynthesizer();
	    
	    // synth.openStream // https://github.com/JorenSix/Tarsos/issues/2
	    
	    //AudioSynthesizer synth = new SoftSynthesizer();
	    
	    AudioSynthesizer as = (AudioSynthesizer) synth ;
	    AudioInputStream stream = as.openStream(null, null);
	    
	    double totalTime = totalTimeInSeconds;
	    long len = (long) (stream.getFormat().getFrameRate() * totalTime);
	    stream = new AudioInputStream(stream, stream.getFormat(), len);
	    
	    //synth.open();
	    Receiver synthRcvr = synth.getReceiver();
	    synthRcvr.send(noteOn, -1); // -1 means no time stamp
	    synthRcvr.send(noteOff, (long)(notePlayTimeInSeconds*1_000_000)); // microseconds
	    
	    return stream;
	}
	
	public static void main(String[] args) throws Exception {
		
		AudioInputStream ais = getOneNoteAsAudioInputStream(60, 93, 2, 3);
	    AudioSystem.write(ais, AudioFileFormat.Type.WAVE, new File("D:\\test1.wav"));
	    ais.close();
	}

}
