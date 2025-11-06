package com.example.simplengine;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.example.simplengine.GameObjects.Rect;

public class CollisionTest {

    @Test
    void rects_intersect_true_when_edges_touch() {
        Rect a = new Rect(100, 50, 0, 0);
        Rect b = new Rect(50, 50, 100, 0);
        assertTrue(a.intersects(b));
    }

    @Test
    void rects_intersect_true_when_overlap() {
        Rect a = new Rect(100, 50, 0, 0);
        Rect b = new Rect(50, 50, 90, 10);
        assertTrue(a.intersects(b));
    }

    @Test
    void rects_intersect_false_when_separate() {
        Rect a = new Rect(100, 50, 0, 0);
        Rect b = new Rect(50, 50, 151, 0);
        assertFalse(a.intersects(b));
    }
}
