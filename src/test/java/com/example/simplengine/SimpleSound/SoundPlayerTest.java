package com.example.simplengine.SimpleSound;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.ByteArrayInputStream;

public class SoundPlayerTest {

    @TempDir
    Path tempDir;
    
    private String validMp3Path;
    private String validWavPath;
    private String invalidAudioPath;

    @BeforeEach
    void setUp() throws Exception {
        // Cria arquivos de teste
        validMp3Path = createTestMp3File(tempDir, "test_sound.mp3");
        validWavPath = createTestWavFile(tempDir, "test_sound.wav");
        invalidAudioPath = tempDir.resolve("nonexistent.mp3").toString();
        
        // Limpa sons carregados antes de cada teste
        clearLoadedSounds();
    }

    @AfterEach
    void tearDown() {
        // Limpa sons carregados após cada teste
        clearLoadedSounds();
    }

    /**
     * Cria um arquivo MP3 fake para testes
     */
    private String createTestMp3File(Path directory, String fileName) throws IOException {
        File mp3File = directory.resolve(fileName).toFile();
        try (FileOutputStream fos = new FileOutputStream(mp3File)) {
            fos.write(new byte[]{0, 0, 0, 0});
        }
        return mp3File.getAbsolutePath();
    }

    /**
     * Cria um arquivo WAV válido para testes
     */
    private String createTestWavFile(Path directory, String fileName) throws IOException {
        File audioFile = directory.resolve(fileName).toFile();
        AudioFormat format = new AudioFormat(8000.0f, 8, 1, true, true);
        byte[] audioData = new byte[8000];
        ByteArrayInputStream bais = new ByteArrayInputStream(audioData);
        AudioInputStream audioInputStream = new AudioInputStream(bais, format, audioData.length);
        AudioSystem.write(audioInputStream, AudioFileFormat.Type.WAVE, audioFile);
        return audioFile.getAbsolutePath();
    }

    /**
     * Limpa os mapas de sons carregados
     */
    private void clearLoadedSounds() {
        try {
            java.lang.reflect.Field mp3Field = SoundPlayer.class.getDeclaredField("loadedMp3Paths");
            mp3Field.setAccessible(true);
            @SuppressWarnings("unchecked")
            java.util.Map<String, String> mp3Map = (java.util.Map<String, String>) mp3Field.get(null);
            mp3Map.clear();

            java.lang.reflect.Field wavField = SoundPlayer.class.getDeclaredField("loadedWavClips");
            wavField.setAccessible(true);
            @SuppressWarnings("unchecked")
            java.util.Map<String, javax.sound.sampled.Clip> wavMap = 
                (java.util.Map<String, javax.sound.sampled.Clip>) wavField.get(null);
            wavMap.clear();
        } catch (Exception e) {
            fail("Não foi possível limpar sons carregados: " + e.getMessage());
        }
    }

    @Test
    void playSound_withValidMp3_doesNotThrow() {
        assertDoesNotThrow(() -> SoundPlayer.playSound(validMp3Path),
            "playSound não deve lançar exceção com MP3");
    }

    @Test
    void playSound_withValidWav_doesNotThrow() {
        assertDoesNotThrow(() -> SoundPlayer.playSound(validWavPath),
            "playSound não deve lançar exceção com WAV");
    }

    @Test
    void playSound_withInvalidFile_doesNotThrow() {
        assertDoesNotThrow(() -> SoundPlayer.playSound(invalidAudioPath),
            "playSound não deve lançar exceção mesmo com arquivo inválido");
    }

    @Test
    void playSound_withNullPath_doesNotThrow() {
        assertDoesNotThrow(() -> SoundPlayer.playSound(null),
            "playSound não deve lançar exceção com path null");
    }

    @Test
    void playSound_withUnsupportedFormat_printsError() {
        String txtPath = tempDir.resolve("test.txt").toString();
        assertDoesNotThrow(() -> SoundPlayer.playSound(txtPath),
            "playSound não deve lançar exceção com formato não suportado");
    }

    @Test
    void loadSound_withValidMp3_loadsSuccessfully() throws Exception {
        String soundName = "testMp3";
        
        assertDoesNotThrow(() -> SoundPlayer.loadSound(soundName, validMp3Path),
            "loadSound não deve lançar exceção com MP3 válido");
        
        java.lang.reflect.Field field = SoundPlayer.class.getDeclaredField("loadedMp3Paths");
        field.setAccessible(true);
        @SuppressWarnings("unchecked")
        java.util.Map<String, String> map = (java.util.Map<String, String>) field.get(null);
        
        assertTrue(map.containsKey(soundName), "MP3 deve estar carregado no mapa");
        assertEquals(validMp3Path, map.get(soundName), "Caminho do MP3 deve estar correto");
    }

    @Test
    void loadSound_withValidWav_loadsSuccessfully() throws Exception {
        String soundName = "testWav";
        
        assertDoesNotThrow(() -> SoundPlayer.loadSound(soundName, validWavPath),
            "loadSound não deve lançar exceção com WAV válido");
        
        java.lang.reflect.Field field = SoundPlayer.class.getDeclaredField("loadedWavClips");
        field.setAccessible(true);
        @SuppressWarnings("unchecked")
        java.util.Map<String, javax.sound.sampled.Clip> map = 
            (java.util.Map<String, javax.sound.sampled.Clip>) field.get(null);
        
        assertTrue(map.containsKey(soundName), "WAV deve estar carregado no mapa");
        assertNotNull(map.get(soundName), "Clip do WAV não deve ser null");
    }

    @Test
    void loadSound_withNullName_doesNotThrow() {
        assertDoesNotThrow(() -> SoundPlayer.loadSound(null, validMp3Path),
            "loadSound não deve lançar exceção com nome null");
    }

    @Test
    void loadSound_withNullPath_doesNotThrow() {
        assertDoesNotThrow(() -> SoundPlayer.loadSound("nullTest", null),
            "loadSound não deve lançar exceção com path null");
    }

    @Test
    void loadSound_multipleSounds_allLoaded() throws Exception {
        String mp3Sound = "mp3_sound";
        String wavSound = "wav_sound";
        
        SoundPlayer.loadSound(mp3Sound, validMp3Path);
        SoundPlayer.loadSound(wavSound, validWavPath);
        
        java.lang.reflect.Field mp3Field = SoundPlayer.class.getDeclaredField("loadedMp3Paths");
        mp3Field.setAccessible(true);
        @SuppressWarnings("unchecked")
        java.util.Map<String, String> mp3Map = (java.util.Map<String, String>) mp3Field.get(null);
        
        java.lang.reflect.Field wavField = SoundPlayer.class.getDeclaredField("loadedWavClips");
        wavField.setAccessible(true);
        @SuppressWarnings("unchecked")
        java.util.Map<String, javax.sound.sampled.Clip> wavMap = 
            (java.util.Map<String, javax.sound.sampled.Clip>) wavField.get(null);
        
        assertEquals(1, mp3Map.size(), "Deve haver 1 MP3 carregado");
        assertEquals(1, wavMap.size(), "Deve haver 1 WAV carregado");
        assertTrue(mp3Map.containsKey(mp3Sound), "MP3 deve estar carregado");
        assertTrue(wavMap.containsKey(wavSound), "WAV deve estar carregado");
    }

    @Test
    void loadSound_sameName_replacesOldSound() throws Exception {
        String soundName = "replaceable";
        String firstPath = validMp3Path;
        String secondPath = tempDir.resolve("another.mp3").toString();
        
        SoundPlayer.loadSound(soundName, firstPath);
        
        java.lang.reflect.Field field = SoundPlayer.class.getDeclaredField("loadedMp3Paths");
        field.setAccessible(true);
        @SuppressWarnings("unchecked")
        java.util.Map<String, String> map = 
            (java.util.Map<String, String>) field.get(null);
        
        assertEquals(firstPath, map.get(soundName));
        
        // Carrega novamente com o mesmo nome
        SoundPlayer.loadSound(soundName, secondPath);
        
        assertEquals(1, map.size(), "Deve haver apenas 1 som carregado");
        assertEquals(secondPath, map.get(soundName), "O caminho deve ter sido substituído");
    }

    @Test
    void playLoadedSound_withLoadedMp3_doesNotThrow() {
        String soundName = "loadedMp3";
        SoundPlayer.loadSound(soundName, validMp3Path);
        
        assertDoesNotThrow(() -> SoundPlayer.playLoadedSound(soundName),
            "playLoadedSound não deve lançar exceção com MP3 carregado");
    }

    @Test
    void playLoadedSound_withLoadedWav_doesNotThrow() {
        String soundName = "loadedWav";
        SoundPlayer.loadSound(soundName, validWavPath);
        
        assertDoesNotThrow(() -> SoundPlayer.playLoadedSound(soundName),
            "playLoadedSound não deve lançar exceção com WAV carregado");
    }

    @Test
    void playLoadedSound_withUnloadedSound_doesNotThrow() {
        assertDoesNotThrow(() -> SoundPlayer.playLoadedSound("nonexistent"),
            "playLoadedSound não deve lançar exceção com som não carregado");
    }

    @Test
    void playLoadedSound_withNullName_doesNotThrow() {
        assertDoesNotThrow(() -> SoundPlayer.playLoadedSound(null),
            "playLoadedSound não deve lançar exceção com nome null");
    }

    @Test
    void playLoadedSound_multipleConsecutiveCalls_doesNotThrow() {
        String soundName = "consecutive";
        SoundPlayer.loadSound(soundName, validMp3Path);
        
        assertDoesNotThrow(() -> {
            SoundPlayer.playLoadedSound(soundName);
            SoundPlayer.playLoadedSound(soundName);
            SoundPlayer.playLoadedSound(soundName);
        }, "Múltiplas chamadas consecutivas não devem lançar exceção");
    }

    @Test
    void loadSound_emptyName_loadsWithEmptyKey() throws Exception {
        String emptyName = "";
        SoundPlayer.loadSound(emptyName, validMp3Path);
        
        java.lang.reflect.Field field = SoundPlayer.class.getDeclaredField("loadedMp3Paths");
        field.setAccessible(true);
        @SuppressWarnings("unchecked")
        java.util.Map<String, String> map = 
            (java.util.Map<String, String>) field.get(null);
        
        assertTrue(map.containsKey(emptyName), "Deve permitir nome vazio como chave");
    }

    @Test
    void soundPlayer_multipleOperations_workCorrectly() throws Exception {
        // Carrega múltiplos sons
        SoundPlayer.loadSound("bg_music", validMp3Path);
        SoundPlayer.loadSound("sfx_jump", validWavPath);
        SoundPlayer.loadSound("sfx_coin", validWavPath);
        
        // Toca sons carregados
        assertDoesNotThrow(() -> {
            SoundPlayer.playLoadedSound("bg_music");
            SoundPlayer.playLoadedSound("sfx_jump");
            SoundPlayer.playLoadedSound("sfx_coin");
        });
        
        // Toca sons direto (sem pré-carregar)
        assertDoesNotThrow(() -> {
            SoundPlayer.playSound(validMp3Path);
            SoundPlayer.playSound(validWavPath);
        });
        
        // Verifica estado dos mapas
        java.lang.reflect.Field mp3Field = SoundPlayer.class.getDeclaredField("loadedMp3Paths");
        mp3Field.setAccessible(true);
        @SuppressWarnings("unchecked")
        java.util.Map<String, String> mp3Map = (java.util.Map<String, String>) mp3Field.get(null);
        
        java.lang.reflect.Field wavField = SoundPlayer.class.getDeclaredField("loadedWavClips");
        wavField.setAccessible(true);
        @SuppressWarnings("unchecked")
        java.util.Map<String, javax.sound.sampled.Clip> wavMap = 
            (java.util.Map<String, javax.sound.sampled.Clip>) wavField.get(null);
        
        assertEquals(1, mp3Map.size(), "Deve haver 1 MP3 pré-carregado");
        assertEquals(2, wavMap.size(), "Devem haver 2 WAV pré-carregados");
    }
}
