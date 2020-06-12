
// Java program to play an Audio
// file using Clip Object
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Sound {
	private Clip clip;
	private AudioInputStream audioInputStream;
	private String filePath;

	public Sound(String FilePath) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		this.filePath = FilePath;
		audioInputStream = AudioSystem.getAudioInputStream(new File(filePath).getAbsoluteFile());
		clip = AudioSystem.getClip();
		clip.open(audioInputStream);
		clip.loop(Clip.LOOP_CONTINUOUSLY);
		this.play();
	}

	public void play() {
		try {
			clip.start();
			Scanner sc = new Scanner(System.in);
//			while (true) {
//				int c = sc.nextInt();
//				if (c == 4)
//					break;
//			}
			sc.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static void main(String[] args) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
	}

}