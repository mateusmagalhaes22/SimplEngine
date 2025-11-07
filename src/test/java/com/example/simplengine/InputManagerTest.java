package com.example.simplengine;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.simplengine.Inputs.InputManager;
import com.example.simplengine.Inputs.Keys;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class InputManagerTest {

    private InputManager inputManager;
    private KeyListener listener;

    @BeforeEach
    void setUp() {
        inputManager = InputManager.getInstance();
        listener = inputManager.getKeyListener();
        
        inputManager.update();
    }

    @Test
    void inputManager_getInstance_returnsSingleton() {
        InputManager instance1 = InputManager.getInstance();
        InputManager instance2 = InputManager.getInstance();
        assertSame(instance1, instance2, "getInstance deve retornar a mesma instância");
    }

    @Test
    void inputManager_keyPressed_setsKeyDown() {
        KeyEvent pressEvent = createKeyEvent(KeyEvent.KEY_PRESSED, KeyEvent.VK_W);
        listener.keyPressed(pressEvent);

        assertTrue(inputManager.isKeyDown(Keys.W), "W deve estar pressionada");
        assertTrue(inputManager.isKeyPressed(Keys.W), "W deve estar marcada como 'just pressed'");
        assertFalse(inputManager.isKeyReleased(Keys.W), "W não deve estar marcada como 'just released'");
    }

    @Test
    void inputManager_keyReleased_setsKeyUp() {
        KeyEvent pressEvent = createKeyEvent(KeyEvent.KEY_PRESSED, KeyEvent.VK_SPACE);
        KeyEvent releaseEvent = createKeyEvent(KeyEvent.KEY_RELEASED, KeyEvent.VK_SPACE);
        
        listener.keyPressed(pressEvent);
        listener.keyReleased(releaseEvent);

        assertFalse(inputManager.isKeyDown(Keys.SPACE), "SPACE não deve estar pressionada");
        assertTrue(inputManager.isKeyReleased(Keys.SPACE), "SPACE deve estar marcada como 'just released'");
    }

    @Test
    void inputManager_update_clearsTransientStates() {

        KeyEvent pressEvent = createKeyEvent(KeyEvent.KEY_PRESSED, KeyEvent.VK_A);
        listener.keyPressed(pressEvent);

        inputManager.update();

        assertFalse(inputManager.isKeyPressed(Keys.A), "A não deve estar 'just pressed' após update");
        assertTrue(inputManager.isKeyDown(Keys.A), "A ainda deve estar pressionada (down)");
    }

    @Test
    void inputManager_multipleKeys_tracksIndependently() {

        listener.keyPressed(createKeyEvent(KeyEvent.KEY_PRESSED, KeyEvent.VK_W));
        listener.keyPressed(createKeyEvent(KeyEvent.KEY_PRESSED, KeyEvent.VK_D));

        assertTrue(inputManager.isKeyDown(Keys.W), "W deve estar pressionada");
        assertTrue(inputManager.isKeyDown(Keys.D), "D deve estar pressionada");
        assertFalse(inputManager.isKeyDown(Keys.S), "S não deve estar pressionada");

        listener.keyReleased(createKeyEvent(KeyEvent.KEY_RELEASED, KeyEvent.VK_W));

        assertFalse(inputManager.isKeyDown(Keys.W), "W não deve estar mais pressionada");
        assertTrue(inputManager.isKeyDown(Keys.D), "D ainda deve estar pressionada");
    }

    @Test
    void inputManager_isKeyUp_returnsOppositeOfKeyDown() {
        assertFalse(inputManager.isKeyDown(Keys.ENTER), "ENTER não deve estar pressionada inicialmente");
        assertTrue(inputManager.isKeyUp(Keys.ENTER), "ENTER deve estar 'up' inicialmente");

        listener.keyPressed(createKeyEvent(KeyEvent.KEY_PRESSED, KeyEvent.VK_ENTER));

        assertTrue(inputManager.isKeyDown(Keys.ENTER), "ENTER deve estar pressionada");
        assertFalse(inputManager.isKeyUp(Keys.ENTER), "ENTER não deve estar 'up'");
    }

    @Test
    void inputManager_unmappedKey_ignored() {

        KeyEvent unknownKeyPress = createKeyEvent(KeyEvent.KEY_PRESSED, KeyEvent.VK_PAUSE);
        listener.keyPressed(unknownKeyPress);

        assertFalse(inputManager.isKeyDown(Keys.SPACE), "Nenhuma tecla válida deve estar afetada");
    }

    @Test
    void inputManager_holdingKey_doesNotTriggerMultiplePressed() {

        listener.keyPressed(createKeyEvent(KeyEvent.KEY_PRESSED, KeyEvent.VK_A));
        
        inputManager.update();
        
        assertFalse(inputManager.isKeyPressed(Keys.A), "A não deve estar 'just pressed' após update");
        assertTrue(inputManager.isKeyDown(Keys.A), "A deve estar 'down'");

        listener.keyPressed(createKeyEvent(KeyEvent.KEY_PRESSED, KeyEvent.VK_A));
        
        assertFalse(inputManager.isKeyPressed(Keys.A), "A não deve estar 'just pressed' ao segurar");
        assertTrue(inputManager.isKeyDown(Keys.A), "A deve continuar 'down'");
    }

    private KeyEvent createKeyEvent(int id, int keyCode) {
        return new KeyEvent(
            new java.awt.Canvas(),
            id,
            System.currentTimeMillis(),
            0,                      // modifiers
            keyCode,
            KeyEvent.CHAR_UNDEFINED
        );
    }
}
