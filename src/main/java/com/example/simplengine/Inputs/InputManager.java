package com.example.simplengine.Inputs;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;
import java.util.Set;

public class InputManager {

    private static InputManager instance;

    private final Set<Keys> keysDown = new HashSet<>();

    private final Set<Keys> keysPressed = new HashSet<>();

    private final Set<Keys> keysReleased = new HashSet<>();

    private final KeyListener keyListener;

    private InputManager() {
        keyListener = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                Keys key = Keys.fromKeyCode(e.getKeyCode());
                if (key != null) {
                    if (!keysDown.contains(key)) {
                        keysPressed.add(key);
                    }
                    keysDown.add(key);
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                Keys key = Keys.fromKeyCode(e.getKeyCode());
                if (key != null) {
                    keysDown.remove(key);
                    keysReleased.add(key);
                }
            }
        };
    }

    public static InputManager getInstance() {
        if (instance == null) {
            instance = new InputManager();
        }
        return instance;
    }

    public KeyListener getKeyListener() {
        return keyListener;
    }

    public void update() {
        keysPressed.clear();
        keysReleased.clear();
    }

    public boolean isKeyPressed(Keys key) {
        return keysPressed.contains(key);
    }

    public boolean isKeyReleased(Keys key) {
        return keysReleased.contains(key);
    }

    public boolean isKeyDown(Keys key) {
        return keysDown.contains(key);
    }

    public boolean isKeyUp(Keys key) {
        return !keysDown.contains(key);
    }
}
