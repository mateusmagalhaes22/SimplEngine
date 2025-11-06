package com.example.simplengine.GameObjects;

import com.example.simplengine.Vectors.Vector2;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

public abstract class GameObject {

    public GameObject() {
        this.hasGravity = false;
        this.position = new Vector2(0, 0);
        this.velocity = new Vector2(0, 0);
    }

    private Boolean hasGravity;

    private Vector2 position;

    private Vector2 velocity;

    public Vector2 getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector2 velocity) {
        this.velocity = velocity;
    }

    public void update(double dt){
        if (position == null) position = new com.example.simplengine.Vectors.Vector2(0f, 0f);
        if (velocity == null) velocity = new com.example.simplengine.Vectors.Vector2(0f, 0f);

        double gravity = 981d;

        if (hasGravity) {
            velocity.setY(velocity.getY() + gravity * dt);
        }

        position.setX(position.getX() + velocity.getX() * dt);
        position.setY(position.getY() + velocity.getY() * dt);
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
