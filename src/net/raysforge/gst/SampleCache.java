package net.raysforge.gst;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.UnsupportedAudioFileException;

import net.raysforge.commons.AudioUtils;
import net.raysforge.midi.SaveOneNoteAsWave;

public class SampleCache {
	
	HashMap<String, ByteBuffer> samples = new HashMap<String, ByteBuffer>();
	private String samplesFolder;
	
	public SampleCache(String samplesFolder) {
		this.samplesFolder = samplesFolder;
	}
	
	public ByteBuffer get(String name) throws UnsupportedAudioFileException, IOException {
		
		if (!samples.containsKey(name)) {
			
			ByteBuffer bb;
			if(name.startsWith("MIDI:")) {
				AudioInputStream ais = SaveOneNoteAsWave.getOneNoteAsAudioInputStream(60, 93, 2, 2.1);
				bb = AudioUtils.readAudioFile(ais);
			} else {
				bb = AudioUtils.readAudioFile(new File(samplesFolder, name));
			}
			samples.put(name, bb);
		}

		return samples.get(name);
		
	}


}
