package com.example.simplengine;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MainTest {

    @Test
    void greet_returnsHelloName() {
        String expectedGreeting = "Hello, World!";
        String actualGreeting = Library.greet("World");
        assertEquals(expectedGreeting, actualGreeting);
    }
}