package net.raysforge.gst;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.security.GeneralSecurityException;
import java.util.List;

import javax.sound.sampled.UnsupportedAudioFileException;

import net.raysforge.commons.AudioLineOut;
import net.raysforge.commons.AudioUtils;
import net.raysforge.commons.Service;

public class PatternPlayer extends Thread {

	private SampleCache sampleCache;
	private SheetsCache sheetsCache;
	private volatile boolean keepPlaying = true;
	private String spreadsheetId;
	private String spreadsheetRange;

	public PatternPlayer(String spreadsheetId, String spreadsheetRange, SheetsCache sheetsCache, SampleCache sampleCache) {
		this.spreadsheetId = spreadsheetId;
		this.spreadsheetRange = spreadsheetRange;
		this.sheetsCache = sheetsCache;
		this.sampleCache = sampleCache;
	}

	public void run() {
		try {
			keepPlaying = true;
			ByteBuffer track = assembleSong();
			// AudioUtils.saveAsWav(track, "D:\\SheetBeats.wav");
			playSong(track.array(), 0);
		} catch (GeneralSecurityException | IOException | UnsupportedAudioFileException e) {
			e.printStackTrace();
		}
	}

	// TODO: make song length dynmaic
	private ByteBuffer assembleSong() throws GeneralSecurityException, IOException, UnsupportedAudioFileException {
		ByteBuffer track = ByteBuffer.allocate(12000000).order(ByteOrder.LITTLE_ENDIAN);

		List<List<Object>> values = sheetsCache.getValues(spreadsheetId, spreadsheetRange);

		int tick = 11025 / 2;
		int line = 0;

		for (List<Object> row : values) {
			for (Object obj : row) {
				if (obj instanceof String) {
					String sample = (String) obj;
					if (Service.hasLength(sample)) {
						System.out.print(sample);
						ByteBuffer sampleBytes = sampleCache.get(sample);
						AudioUtils.mix(track, sampleBytes, tick * line * 2, 0.6f);
					}
					System.out.print(",");
				}
			}
			line++;
			System.out.println();
		}

		return track;
	}

	public void playSong(byte[] sandman, int offset) throws IOException {
		AudioLineOut lineOut = new AudioLineOut().openstart();
		byte[] buffer = new byte[44100 * 2]; 
		ByteArrayInputStream bais = new ByteArrayInputStream(sandman);
		int total = offset;
		bais.skip(offset);

		while (keepPlaying) {
			int read = bais.read(buffer);
			if (read == -1)
				break;
			total += read;
			System.out.println(total);
			lineOut.write(buffer, read);
		}

		// lineOut.write(completeSong);
		lineOut.drain();
		lineOut.stopclose();
	}

	public void stop_() {
		keepPlaying = false;
	}
}
