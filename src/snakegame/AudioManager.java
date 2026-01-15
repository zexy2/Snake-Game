package snakegame;

import java.io.File;
import java.io.IOException;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * Manages audio playback for the Snake game.
 * Handles background music and sound effects.
 */
public class AudioManager {
    
    private Clip backgroundClip;
    
    /**
     * Plays a sound file once.
     * 
     * @param filePath Path to the audio file
     */
    public void playSound(String filePath) {
        try {
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(new File(filePath));
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();
        } catch (IOException | LineUnavailableException | UnsupportedAudioFileException e) {
            System.err.println("Error playing sound: " + filePath);
            e.printStackTrace();
        }
    }
    
    /**
     * Plays background music (stores reference for later stopping).
     * 
     * @param filePath Path to the audio file
     */
    public void playBackgroundMusic(String filePath) {
        try {
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(new File(filePath));
            backgroundClip = AudioSystem.getClip();
            backgroundClip.open(audioStream);
            backgroundClip.start();
        } catch (IOException | LineUnavailableException | UnsupportedAudioFileException e) {
            System.err.println("Error playing background music: " + filePath);
            e.printStackTrace();
        }
    }
    
    /**
     * Stops the background music.
     */
    public void stopBackgroundMusic() {
        if (backgroundClip != null && backgroundClip.isRunning()) {
            backgroundClip.stop();
        }
    }
    
    /**
     * Checks if background music is currently playing.
     * 
     * @return true if background music is playing
     */
    public boolean isBackgroundMusicPlaying() {
        return backgroundClip != null && backgroundClip.isRunning();
    }
}
