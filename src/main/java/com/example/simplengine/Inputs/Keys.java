package com.example.simplengine.Inputs;

import java.awt.event.KeyEvent;

public enum Keys {

    // Letras
    A(KeyEvent.VK_A),
    B(KeyEvent.VK_B),
    C(KeyEvent.VK_C),
    D(KeyEvent.VK_D),
    E(KeyEvent.VK_E),
    F(KeyEvent.VK_F),
    G(KeyEvent.VK_G),
    H(KeyEvent.VK_H),
    I(KeyEvent.VK_I),
    J(KeyEvent.VK_J),
    K(KeyEvent.VK_K),
    L(KeyEvent.VK_L),
    M(KeyEvent.VK_M),
    N(KeyEvent.VK_N),
    O(KeyEvent.VK_O),
    P(KeyEvent.VK_P),
    Q(KeyEvent.VK_Q),
    R(KeyEvent.VK_R),
    S(KeyEvent.VK_S),
    T(KeyEvent.VK_T),
    U(KeyEvent.VK_U),
    V(KeyEvent.VK_V),
    W(KeyEvent.VK_W),
    X(KeyEvent.VK_X),
    Y(KeyEvent.VK_Y),
    Z(KeyEvent.VK_Z),

    // Números
    NUM_0(KeyEvent.VK_0),
    NUM_1(KeyEvent.VK_1),
    NUM_2(KeyEvent.VK_2),
    NUM_3(KeyEvent.VK_3),
    NUM_4(KeyEvent.VK_4),
    NUM_5(KeyEvent.VK_5),
    NUM_6(KeyEvent.VK_6),
    NUM_7(KeyEvent.VK_7),
    NUM_8(KeyEvent.VK_8),
    NUM_9(KeyEvent.VK_9),

    // Setas
    ARROW_UP(KeyEvent.VK_UP),
    ARROW_DOWN(KeyEvent.VK_DOWN),
    ARROW_LEFT(KeyEvent.VK_LEFT),
    ARROW_RIGHT(KeyEvent.VK_RIGHT),

    // Funções/controle
    SPACE(KeyEvent.VK_SPACE),
    ENTER(KeyEvent.VK_ENTER),
    ESCAPE(KeyEvent.VK_ESCAPE),
    BACKSPACE(KeyEvent.VK_BACK_SPACE),
    TAB(KeyEvent.VK_TAB),

    // Modificadores
    SHIFT(KeyEvent.VK_SHIFT),
    CTRL(KeyEvent.VK_CONTROL),
    ALT(KeyEvent.VK_ALT),

    // Teclas F
    F1(KeyEvent.VK_F1),
    F2(KeyEvent.VK_F2),
    F3(KeyEvent.VK_F3),
    F4(KeyEvent.VK_F4),
    F5(KeyEvent.VK_F5),
    F6(KeyEvent.VK_F6),
    F7(KeyEvent.VK_F7),
    F8(KeyEvent.VK_F8),
    F9(KeyEvent.VK_F9),
    F10(KeyEvent.VK_F10),
    F11(KeyEvent.VK_F11),
    F12(KeyEvent.VK_F12);

    private final int keyCode;

    Keys(int keyCode) {
        this.keyCode = keyCode;
    }

    public int getKeyCode() {
        return keyCode;
    }

    public static Keys fromKeyCode(int keyCode) {
        for (Keys k : values()) {
            if (k.keyCode == keyCode) return k;
        }
        return null;
    }
}

