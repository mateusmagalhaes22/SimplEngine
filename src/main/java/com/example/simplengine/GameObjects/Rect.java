package com.example.simplengine.GameObjects;

import com.example.simplengine.Vectors.Vector2;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

public class Rect extends GameObject {
    private Vector2 size;
    private Color color;

    public Rect(int width, int height, int posX, int posY) {
        super();
        this.size = new Vector2(width, height);
        setPosition(new Vector2(posX, posY));
        this.color = Color.WHITE;
    }

    public double getWidth() {
        return size.getX();
    }

    public double getHeight() {
        return size.getY();
    }

    public void setSize(int height, int width) {
        this.size = new Vector2(width, height);
    }

    public void setColor(int r, int g, int b) {
        this.color = new Color(r, g, b);
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    @Override
    public void render(Graphics2D g) {
        if (g == null) return;

        Vector2 pos = getPosition();
        int x = (int) pos.getX();
        int y = (int) pos.getY();
        int w = (int) getWidth();
        int h = (int) getHeight();

        Color old = g.getColor();
        g.setColor(color);
        g.fillRect(x, y, w, h);
        g.setColor(Color.BLACK);
        g.drawRect(x, y, w, h);
        g.setColor(old);
    }

    @Override
    public Rectangle2D.Float getBounds() {
        Vector2 pos = getPosition();
        return new Rectangle2D.Float(
            (float) pos.getX(),
            (float) pos.getY(),
            (float) getWidth(),
            (float) getHeight()
        );
    }
    
}
