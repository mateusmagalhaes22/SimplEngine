package com.example.simplengine.SimpleSound;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import javazoom.jl.player.Player;

public class SoundPlayer {

    private static Map<String, String> loadedMp3Paths = new HashMap<>();
    private static Map<String, Clip> loadedWavClips = new HashMap<>();
    
    /**
     * Toca um arquivo de áudio (suporta MP3 e WAV)
     * @param filePath Caminho do arquivo de áudio
     */
    public static void playSound(String filePath) {
        if (filePath == null) {
            System.err.println("SoundPlayer error: File path cannot be null");
            return;
        }
        
        String lowerPath = filePath.toLowerCase();
        
        // Se for MP3
        if (lowerPath.endsWith(".mp3")) {
            playMp3(filePath);
        } 
        // Se for WAV
        else if (lowerPath.endsWith(".wav")) {
            playWav(filePath);
        } 
        else {
            System.err.println("SoundPlayer error: Unsupported audio format. Use .mp3 or .wav");
        }
    }
    
    /**
     * Toca um arquivo MP3 em uma thread separada
     * @param filePath Caminho do arquivo MP3
     */
    private static void playMp3(String filePath) {
        Thread mp3Thread = new Thread(() -> {
            try {
                FileInputStream fis = new FileInputStream(filePath);
                BufferedInputStream bis = new BufferedInputStream(fis);
                Player player = new Player(bis);
                player.play();
                bis.close();
                fis.close();
            } catch (Exception e) {
                System.err.println("SoundPlayer error: " + e.getMessage());
            }
        });
        mp3Thread.start();
    }
    
    /**
     * Toca um arquivo WAV
     * @param filePath Caminho do arquivo WAV
     */
    private static void playWav(String filePath) {
        try {
            File audioFile = new File(filePath);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.err.println("SoundPlayer error: " + e.getMessage());
        }
    }

    /**
     * Pré-carrega um arquivo de áudio (MP3 ou WAV)
     * @param soundName Identificador do som
     * @param filePath Caminho do arquivo de áudio
     */
    public static void loadSound(String soundName, String filePath) {
        if (soundName == null) {
            System.err.println("SoundPlayer error: Sound name cannot be null");
            return;
        }
        
        if (filePath == null) {
            System.err.println("SoundPlayer error: File path cannot be null");
            return;
        }
        
        String lowerPath = filePath.toLowerCase();
        
        // Se for MP3, armazena o caminho
        if (lowerPath.endsWith(".mp3")) {
            loadedMp3Paths.put(soundName, filePath);
        } 
        // Se for WAV, carrega o Clip
        else if (lowerPath.endsWith(".wav")) {
            try {
                File audioFile = new File(filePath);
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
                Clip clip = AudioSystem.getClip();
                clip.open(audioStream);
                loadedWavClips.put(soundName, clip);
            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
                System.err.println("SoundPlayer error: " + e.getMessage());
            }
        } 
        else {
            System.err.println("SoundPlayer error: Unsupported audio format. Use .mp3 or .wav");
        }
    }

    /**
     * Reproduz um som previamente carregado (MP3 ou WAV)
     * @param soundName Identificador do som
     */
    public static void playLoadedSound(String soundName) {
        // Verifica se é um MP3 carregado
        String mp3Path = loadedMp3Paths.get(soundName);
        if (mp3Path != null) {
            playMp3(mp3Path);
            return;
        }
        
        // Verifica se é um WAV carregado
        Clip wavClip = loadedWavClips.get(soundName);
        if (wavClip != null) {
            if (wavClip.isRunning()) {
                wavClip.stop();
            }
            wavClip.setFramePosition(0);
            wavClip.start();
            return;
        }
        
        System.err.println("SoundPlayer error: Sound not loaded - " + soundName);
    }
}
