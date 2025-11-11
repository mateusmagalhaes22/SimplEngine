package com.example.simplengine.Camera;

import com.example.simplengine.Vectors.Vector2;

public final class Camera {
    private static volatile Camera instance;

    private Vector2 position;
    private float zoom;
    private float followSpeed;

    private Camera() {
        position = new Vector2(0, 0);
        zoom = 1.0f;
        followSpeed = 0.1f;
    }

    public static Camera getInstance() {
        if (instance == null) {
            synchronized (Camera.class) {
                if (instance == null) {
                    instance = new Camera();
                }
            }
        }
        return instance;
    }

    public void setPosition(double x, double y) {
        this.position.setX(x);
        this.position.setY(y);
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setZoom(float zoom) {
        this.zoom = Math.max(0.1f, zoom);
    }

    public float getZoom() {
        return zoom;
    }

    public void move(double dx, double dy) {
        position.setX(position.getX() + dx);
        position.setY(position.getY() + dy);
    }

    public Vector2 worldToScreen(double worldX, double worldY, int screenWidth, int screenHeight) {
        double screenX = (worldX - position.getX()) * zoom + screenWidth / 2.0f;
        double screenY = (worldY - position.getY()) * zoom + screenHeight / 2.0f;
        return new Vector2(screenX, screenY);
    }

    public Vector2 screenToWorld(double screenX, double screenY, int screenWidth, int screenHeight) {
        double worldX = (screenX - screenWidth / 2.0f) / zoom + position.getX();
        double worldY = (screenY - screenHeight / 2.0f) / zoom + position.getY();
        return new Vector2(worldX, worldY);
    }

    public void centerOn(double x, double y) {
        setPosition(x, y);
    }

    public void centerOn(Vector2 target) {
        setPosition((double) target.getX(), (double) target.getY());
    }

    public void follow(double targetX, double targetY, double deltaTime) {

        double lerpFactor = Math.min(1.0f, followSpeed * (double) deltaTime * 60.0f);

        double newX = (double) (position.getX() + (targetX - position.getX()) * lerpFactor);
        double newY = (double) (position.getY() + (targetY - position.getY()) * lerpFactor);
        
        setPosition(newX, newY);
    }

    public void follow(Vector2 target, double deltaTime) {
        follow(target.getX(), target.getY(), deltaTime);
    }

    public void setFollowSpeed(double speed) {
        this.followSpeed = (float) Math.max(0.01f, Math.min(1.0f, speed));
    }

    public float getFollowSpeed() {
        return followSpeed;
    }
}
