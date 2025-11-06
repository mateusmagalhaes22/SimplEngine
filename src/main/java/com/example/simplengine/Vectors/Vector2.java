package com.example.simplengine.Vectors;

public class Vector2 {
    public Vector2(double x, double y) {
        this.x = x;
        this.y = y;
    }
    private double x;
    private double y;
    public double getX() {
        return x;
    }
    public void setX(double x) {
        this.x = x;
    }
    public double getY() {
        return y;
    }
    public void setY(double y) {
        this.y = y;
    }
}
