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

    public void setPosition(float x, float y) {
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

    public void move(float dx, float dy) {
        position.setX(position.getX() + dx);
        position.setY(position.getY() + dy);
    }

    public Vector2 worldToScreen(float worldX, float worldY, int screenWidth, int screenHeight) {
        float screenX = (float) ((worldX - position.getX()) * zoom + screenWidth / 2.0f);
        float screenY = (float) ((worldY - position.getY()) * zoom + screenHeight / 2.0f);
        return new Vector2(screenX, screenY);
    }

    public Vector2 screenToWorld(float screenX, float screenY, int screenWidth, int screenHeight) {
        float worldX = (float) ((screenX - screenWidth / 2.0f) / zoom + position.getX());
        float worldY = (float) ((screenY - screenHeight / 2.0f) / zoom + position.getY());
        return new Vector2(worldX, worldY);
    }

    public void centerOn(float x, float y) {
        setPosition(x, y);
    }

    public void centerOn(Vector2 target) {
        setPosition((float) target.getX(), (float) target.getY());
    }

    public void follow(double targetX, double targetY, double deltaTime) {

        float lerpFactor = Math.min(1.0f, followSpeed * (float) deltaTime * 60.0f);

        float newX = (float) (position.getX() + (targetX - position.getX()) * lerpFactor);
        float newY = (float) (position.getY() + (targetY - position.getY()) * lerpFactor);
        
        setPosition(newX, newY);
    }

    public void follow(Vector2 target, double deltaTime) {
        follow((float) target.getX(), (float) target.getY(), deltaTime);
    }

    public void setFollowSpeed(float speed) {
        this.followSpeed = Math.max(0.01f, Math.min(1.0f, speed));
    }

    public float getFollowSpeed() {
        return followSpeed;
    }
}
