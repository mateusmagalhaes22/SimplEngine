package com.example.simplengine.GameObjects;

import com.example.simplengine.Vectors.Vector2;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

public abstract class GameObject {

    public GameObject() {
        this.hasGravity = false;
        this.position = new Vector2(0, 0);
        this.speed = new Vector2(0, 0);
    }

    private Boolean hasGravity;

    private Vector2 position;

    private Vector2 speed;

    public Vector2 getSpeed() {
        return speed;
    }

    public void setSpeed(Vector2 speed) {
        this.speed = speed;
    }

    public void update(double dt){
        if (position == null) position = new com.example.simplengine.Vectors.Vector2(0f, 0f);
        if (speed == null) speed = new com.example.simplengine.Vectors.Vector2(0f, 0f);

        double gravity = 981d;

        if (hasGravity) {
            speed.setY(speed.getY() + gravity * dt);
        }

        position.setX(position.getX() + speed.getX() * dt);
        position.setY(position.getY() + speed.getY() * dt);
    }

    public abstract void render(Graphics2D g);

    public abstract Rectangle2D.Float getBounds();
    
    public boolean intersects(GameObject other) {
        Rectangle2D.Float a = this.getBounds();
        Rectangle2D.Float b = other.getBounds();
        return a.x <= b.x + b.width &&
               a.x + a.width >= b.x &&
               a.y <= b.y + b.height &&
               a.y + a.height >= b.y;
    }

    public Boolean getHasGravity() {
        return hasGravity;
    }

    public void setHasGravity(Boolean hasGravity) {
        this.hasGravity = hasGravity;
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

}
