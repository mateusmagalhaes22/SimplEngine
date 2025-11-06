package com.example.simplengine;

import org.junit.jupiter.api.Test;

import com.example.simplengine.Render.Canvas;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import javax.swing.JFrame;

public class MainTest {

    @Test
    void Canvas_newCanvas_createsNewCanvas() {
        JFrame frame = Canvas.newCanvas(800, 600);
        assertNotNull(frame);
        assertEquals(800, frame.getContentPane().getWidth());
        assertEquals(600, frame.getContentPane().getHeight());
    }
    @Test
    void Canvas_newCanvasWithTitle_createsNewCanvasWithTitle() {
        JFrame frame = Canvas.newCanvas(800, 600, "Test Title");
        assertNotNull(frame);
        assertEquals("Test Title", frame.getTitle());
    }
    @Test
    void Canvas_newCanvas_invalidDimensions_throwsException() {
        try {
            Canvas.newCanvas(0, 600);
        } catch (IllegalArgumentException e) {
            assertEquals("width e height devem ser maiores que 0", e.getMessage());
        }
    }
}