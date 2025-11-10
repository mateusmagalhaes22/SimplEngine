package com.example.simplengine.Vectors;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class Vector2Test {

    private static final double DELTA = 0.0001;

    @Test
    public void testConstructor() {
        Vector2 v = new Vector2(5.0, 10.0);
        assertEquals(5.0, v.getX(), DELTA);
        assertEquals(10.0, v.getY(), DELTA);
    }

    @Test
    public void testConstructorWithNegativeValues() {
        Vector2 v = new Vector2(-3.5, -7.2);
        assertEquals(-3.5, v.getX(), DELTA);
        assertEquals(-7.2, v.getY(), DELTA);
    }

    @Test
    public void testConstructorWithZero() {
        Vector2 v = new Vector2(0.0, 0.0);
        assertEquals(0.0, v.getX(), DELTA);
        assertEquals(0.0, v.getY(), DELTA);
    }

    @Test
    public void testSetX() {
        Vector2 v = new Vector2(1.0, 2.0);
        v.setX(10.0);
        assertEquals(10.0, v.getX(), DELTA);
        assertEquals(2.0, v.getY(), DELTA);
    }

    @Test
    public void testSetY() {
        Vector2 v = new Vector2(1.0, 2.0);
        v.setY(20.0);
        assertEquals(1.0, v.getX(), DELTA);
        assertEquals(20.0, v.getY(), DELTA);
    }

    @Test
    public void testSetXWithNegative() {
        Vector2 v = new Vector2(5.0, 5.0);
        v.setX(-15.5);
        assertEquals(-15.5, v.getX(), DELTA);
    }

    @Test
    public void testSetYWithNegative() {
        Vector2 v = new Vector2(5.0, 5.0);
        v.setY(-25.3);
        assertEquals(-25.3, v.getY(), DELTA);
    }

    @Test
    public void testAddVector() {
        Vector2 v1 = new Vector2(3.0, 4.0);
        Vector2 v2 = new Vector2(1.0, 2.0);
        Vector2 result = v1.add(v2);
        
        assertEquals(4.0, result.getX(), DELTA);
        assertEquals(6.0, result.getY(), DELTA);
        
        // Original vectors should be unchanged
        assertEquals(3.0, v1.getX(), DELTA);
        assertEquals(4.0, v1.getY(), DELTA);
        assertEquals(1.0, v2.getX(), DELTA);
        assertEquals(2.0, v2.getY(), DELTA);
    }

    @Test
    public void testAddVectorWithNegative() {
        Vector2 v1 = new Vector2(5.0, 10.0);
        Vector2 v2 = new Vector2(-2.0, -3.0);
        Vector2 result = v1.add(v2);
        
        assertEquals(3.0, result.getX(), DELTA);
        assertEquals(7.0, result.getY(), DELTA);
    }

    @Test
    public void testAddVectorResultingInZero() {
        Vector2 v1 = new Vector2(5.0, 5.0);
        Vector2 v2 = new Vector2(-5.0, -5.0);
        Vector2 result = v1.add(v2);
        
        assertEquals(0.0, result.getX(), DELTA);
        assertEquals(0.0, result.getY(), DELTA);
    }

    @Test
    public void testAddDoubles() {
        Vector2 v = new Vector2(3.0, 4.0);
        Vector2 result = v.add(2.0, 3.0);
        
        assertEquals(5.0, result.getX(), DELTA);
        assertEquals(7.0, result.getY(), DELTA);
        
        assertEquals(3.0, v.getX(), DELTA);
        assertEquals(4.0, v.getY(), DELTA);
    }

    @Test
    public void testAddDoublesWithNegative() {
        Vector2 v = new Vector2(10.0, 20.0);
        Vector2 result = v.add(-5.0, -10.0);
        
        assertEquals(5.0, result.getX(), DELTA);
        assertEquals(10.0, result.getY(), DELTA);
    }

    @Test
    public void testAddDoublesResultingInZero() {
        Vector2 v = new Vector2(7.0, 3.0);
        Vector2 result = v.add(-7.0, -3.0);
        
        assertEquals(0.0, result.getX(), DELTA);
        assertEquals(0.0, result.getY(), DELTA);
    }

    @Test
    public void testSubtractVector() {
        Vector2 v1 = new Vector2(5.0, 8.0);
        Vector2 v2 = new Vector2(2.0, 3.0);
        Vector2 result = v1.subtract(v2);
        
        assertEquals(3.0, result.getX(), DELTA);
        assertEquals(5.0, result.getY(), DELTA);
        
        assertEquals(5.0, v1.getX(), DELTA);
        assertEquals(8.0, v1.getY(), DELTA);
        assertEquals(2.0, v2.getX(), DELTA);
        assertEquals(3.0, v2.getY(), DELTA);
    }

    @Test
    public void testSubtractVectorWithNegative() {
        Vector2 v1 = new Vector2(3.0, 4.0);
        Vector2 v2 = new Vector2(-2.0, -5.0);
        Vector2 result = v1.subtract(v2);
        
        assertEquals(5.0, result.getX(), DELTA);
        assertEquals(9.0, result.getY(), DELTA);
    }

    @Test
    public void testSubtractVectorResultingInNegative() {
        Vector2 v1 = new Vector2(2.0, 3.0);
        Vector2 v2 = new Vector2(5.0, 8.0);
        Vector2 result = v1.subtract(v2);
        
        assertEquals(-3.0, result.getX(), DELTA);
        assertEquals(-5.0, result.getY(), DELTA);
    }

    @Test
    public void testSubtractVectorResultingInZero() {
        Vector2 v1 = new Vector2(5.0, 5.0);
        Vector2 v2 = new Vector2(5.0, 5.0);
        Vector2 result = v1.subtract(v2);
        
        assertEquals(0.0, result.getX(), DELTA);
        assertEquals(0.0, result.getY(), DELTA);
    }

    @Test
    public void testSubtractDoubles() {
        Vector2 v = new Vector2(10.0, 15.0);
        Vector2 result = v.subtract(3.0, 5.0);
        
        assertEquals(7.0, result.getX(), DELTA);
        assertEquals(10.0, result.getY(), DELTA);
        
        assertEquals(10.0, v.getX(), DELTA);
        assertEquals(15.0, v.getY(), DELTA);
    }

    @Test
    public void testSubtractDoublesWithNegative() {
        Vector2 v = new Vector2(5.0, 3.0);
        Vector2 result = v.subtract(-2.0, -4.0);
        
        assertEquals(7.0, result.getX(), DELTA);
        assertEquals(7.0, result.getY(), DELTA);
    }

    @Test
    public void testSubtractDoublesResultingInNegative() {
        Vector2 v = new Vector2(3.0, 2.0);
        Vector2 result = v.subtract(5.0, 6.0);
        
        assertEquals(-2.0, result.getX(), DELTA);
        assertEquals(-4.0, result.getY(), DELTA);
    }

    @Test
    public void testSubtractDoublesResultingInZero() {
        Vector2 v = new Vector2(8.0, 12.0);
        Vector2 result = v.subtract(8.0, 12.0);
        
        assertEquals(0.0, result.getX(), DELTA);
        assertEquals(0.0, result.getY(), DELTA);
    }

    @Test
    public void testChainedOperations() {
        Vector2 v = new Vector2(10.0, 20.0);
        Vector2 result = v.add(5.0, 10.0).subtract(3.0, 5.0);
        
        assertEquals(12.0, result.getX(), DELTA);
        assertEquals(25.0, result.getY(), DELTA);
        
        assertEquals(10.0, v.getX(), DELTA);
        assertEquals(20.0, v.getY(), DELTA);
    }

    @Test
    public void testComplexChainedOperations() {
        Vector2 v1 = new Vector2(1.0, 2.0);
        Vector2 v2 = new Vector2(3.0, 4.0);
        Vector2 v3 = new Vector2(5.0, 6.0);
        
        Vector2 result = v1.add(v2).subtract(v3).add(2.0, 2.0);
        
        assertEquals(1.0, result.getX(), DELTA);
        assertEquals(2.0, result.getY(), DELTA);
    }

    @Test
    public void testImmutability() {
        Vector2 original = new Vector2(5.0, 10.0);
        Vector2 added = original.add(1.0, 1.0);
        Vector2 subtracted = original.subtract(1.0, 1.0);
        
        assertEquals(5.0, original.getX(), DELTA);
        assertEquals(10.0, original.getY(), DELTA);
        
        assertEquals(6.0, added.getX(), DELTA);
        assertEquals(11.0, added.getY(), DELTA);
        
        assertEquals(4.0, subtracted.getX(), DELTA);
        assertEquals(9.0, subtracted.getY(), DELTA);
    }
}
