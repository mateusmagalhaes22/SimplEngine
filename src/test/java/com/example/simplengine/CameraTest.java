package com.example.simplengine;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.simplengine.Camera.Camera;
import com.example.simplengine.Vectors.Vector2;

public class CameraTest {

    private Camera camera;

    @BeforeEach
    void setUp() {
        camera = Camera.getInstance();
        camera.setPosition(0, 0);
        camera.setZoom(1.0f);
        camera.setFollowSpeed(0.1f);
    }
    
    @Test
    void camera_initializationSingletonTest() {
        Camera first = Camera.getInstance();
        Camera second = Camera.getInstance();
        assertNotNull(first);
        assertSame(first, second);
    }

    @Test
    void camera_defaultPosition_isZero() {
        camera.setPosition(0, 0);
        Vector2 pos = camera.getPosition();
        assertEquals(0.0, pos.getX(), 0.001, "Posição X inicial deve ser 0");
        assertEquals(0.0, pos.getY(), 0.001, "Posição Y inicial deve ser 0");
    }

    @Test
    void camera_setPosition_updatesPosition() {
        camera.setPosition(100, 200);
        Vector2 pos = camera.getPosition();
        assertEquals(100.0, pos.getX(), 0.001, "Posição X deve ser 100");
        assertEquals(200.0, pos.getY(), 0.001, "Posição Y deve ser 200");
    }

    @Test
    void camera_setPositionVector_updatesPosition() {
        Vector2 newPos = new Vector2(50, 75);
        camera.setPosition(newPos);
        Vector2 pos = camera.getPosition();
        assertEquals(50.0, pos.getX(), 0.001, "Posição X deve ser 50");
        assertEquals(75.0, pos.getY(), 0.001, "Posição Y deve ser 75");
    }

    @Test
    void camera_move_addsToPosition() {
        camera.setPosition(100, 100);
        camera.move(50, -30);
        Vector2 pos = camera.getPosition();
        assertEquals(150.0, pos.getX(), 0.001, "Posição X deve ser 150");
        assertEquals(70.0, pos.getY(), 0.001, "Posição Y deve ser 70");
    }

    @Test
    void camera_defaultZoom_isOne() {
        assertEquals(1.0f, camera.getZoom(), 0.001f, "Zoom padrão deve ser 1.0");
    }

    @Test
    void camera_setZoom_updatesZoom() {
        camera.setZoom(2.0f);
        assertEquals(2.0f, camera.getZoom(), 0.001f, "Zoom deve ser 2.0");
    }

    @Test
    void camera_setZoom_clampsBelowMinimum() {
        camera.setZoom(0.05f);
        assertTrue(camera.getZoom() >= 0.1f, "Zoom deve ser limitado a no mínimo 0.1");
    }

    @Test
    void camera_centerOn_setsPosition() {
        camera.centerOn(300, 400);
        Vector2 pos = camera.getPosition();
        assertEquals(300.0, pos.getX(), 0.001, "Posição X deve ser 300");
        assertEquals(400.0, pos.getY(), 0.001, "Posição Y deve ser 400");
    }

    @Test
    void camera_centerOnVector_setsPosition() {
        Vector2 target = new Vector2(150, 250);
        camera.centerOn(target);
        Vector2 pos = camera.getPosition();
        assertEquals(150.0, pos.getX(), 0.001, "Posição X deve ser 150");
        assertEquals(250.0, pos.getY(), 0.001, "Posição Y deve ser 250");
    }

    @Test
    void camera_worldToScreen_convertsCorrectly() {
        int screenWidth = 800;
        int screenHeight = 600;
        
        camera.setPosition(0, 0);
        camera.setZoom(1.0f);
        
        Vector2 screen = camera.worldToScreen(0, 0, screenWidth, screenHeight);
        assertEquals(400.0, screen.getX(), 0.1, "X na tela deve ser 400 (centro)");
        assertEquals(300.0, screen.getY(), 0.1, "Y na tela deve ser 300 (centro)");
    }

    @Test
    void camera_worldToScreen_withCameraOffset() {
        int screenWidth = 800;
        int screenHeight = 600;
        
        camera.setPosition(100, 50);
        camera.setZoom(1.0f);
        
        Vector2 screen = camera.worldToScreen(100, 50, screenWidth, screenHeight);
        assertEquals(400.0, screen.getX(), 0.1, "Ponto na posição da câmera deve estar no centro X");
        assertEquals(300.0, screen.getY(), 0.1, "Ponto na posição da câmera deve estar no centro Y");
    }

    @Test
    void camera_worldToScreen_withZoom() {
        int screenWidth = 800;
        int screenHeight = 600;
        
        camera.setPosition(0, 0);
        camera.setZoom(2.0f);
        
        Vector2 screen = camera.worldToScreen(100, 0, screenWidth, screenHeight);
        assertEquals(600.0, screen.getX(), 0.1, "X deve ser 400 + 100*2 = 600");
        assertEquals(300.0, screen.getY(), 0.1, "Y deve permanecer no centro");
    }

    @Test
    void camera_screenToWorld_convertsCorrectly() {
        int screenWidth = 800;
        int screenHeight = 600;
        
        camera.setPosition(0, 0);
        camera.setZoom(1.0f);
        
        Vector2 world = camera.screenToWorld(400, 300, screenWidth, screenHeight);
        assertEquals(0.0, world.getX(), 0.1, "X do mundo deve ser 0");
        assertEquals(0.0, world.getY(), 0.1, "Y do mundo deve ser 0");
    }

    @Test
    void camera_screenToWorld_withCameraOffset() {
        int screenWidth = 800;
        int screenHeight = 600;
        
        camera.setPosition(100, 50);
        camera.setZoom(1.0f);
        
        Vector2 world = camera.screenToWorld(400, 300, screenWidth, screenHeight);
        assertEquals(100.0, world.getX(), 0.1, "X do mundo deve ser 100");
        assertEquals(50.0, world.getY(), 0.1, "Y do mundo deve ser 50");
    }

    @Test
    void camera_screenToWorld_withZoom() {
        int screenWidth = 800;
        int screenHeight = 600;
        
        camera.setPosition(0, 0);
        camera.setZoom(2.0f);
        
        Vector2 world = camera.screenToWorld(600, 300, screenWidth, screenHeight);
        assertEquals(100.0, world.getX(), 0.1, "X do mundo deve ser (600-400)/2 = 100");
        assertEquals(0.0, world.getY(), 0.1, "Y do mundo deve ser 0");
    }

    @Test
    void camera_worldToScreen_andBack_isReversible() {
        int screenWidth = 800;
        int screenHeight = 600;
        
        camera.setPosition(123, 456);
        camera.setZoom(1.5f);
        
        float originalWorldX = 789;
        float originalWorldY = 321;
        
        Vector2 screen = camera.worldToScreen(originalWorldX, originalWorldY, screenWidth, screenHeight);
        Vector2 worldBack = camera.screenToWorld((float) screen.getX(), (float) screen.getY(), screenWidth, screenHeight);
        
        assertEquals(originalWorldX, worldBack.getX(), 0.1, "X deve voltar ao original");
        assertEquals(originalWorldY, worldBack.getY(), 0.1, "Y deve voltar ao original");
    }

    @Test
    void camera_follow_movesTowardsTarget() {
        camera.setPosition(0, 0);
        camera.setFollowSpeed(0.1f);
        
        camera.follow(100, 100, 1.0 / 60.0);
        
        Vector2 pos = camera.getPosition();
        
        assertTrue(pos.getX() > 0 && pos.getX() < 100, "X deve estar entre 0 e 100, atual: " + pos.getX());
        assertTrue(pos.getY() > 0 && pos.getY() < 100, "Y deve estar entre 0 e 100, atual: " + pos.getY());
    }

    @Test
    void camera_follow_withVector_movesTowardsTarget() {

        camera.setPosition(0, 0);
        camera.setFollowSpeed(0.1f);
        
        Vector2 target = new Vector2(100, 100);
        camera.follow(target, 1.0 / 60.0);
        
        Vector2 pos = camera.getPosition();
        
        assertTrue(pos.getX() > 0 && pos.getX() < 100, "X deve estar entre 0 e 100, atual: " + pos.getX());
        assertTrue(pos.getY() > 0 && pos.getY() < 100, "Y deve estar entre 0 e 100, atual: " + pos.getY());
    }

    @Test
    void camera_follow_eventuallyReachesTarget() {
        camera.setPosition(0, 0);
        camera.setFollowSpeed(0.5f);
        
        for (int i = 0; i < 300; i++) {
            camera.follow(100, 100, 1.0 / 60.0);
        }
        
        Vector2 pos = camera.getPosition();
        
        assertEquals(100.0, pos.getX(), 1.0, "X deve estar próximo de 100");
        assertEquals(100.0, pos.getY(), 1.0, "Y deve estar próximo de 100");
    }

    @Test
    void camera_follow_withHighSpeed_movesFaster() {

        camera.setPosition(0, 0);
        camera.setFollowSpeed(0.1f);
        camera.follow(100, 100, 1.0 / 60.0);
        double slowDistance = camera.getPosition().getX();
        
        camera.setPosition(0, 0);
        camera.setFollowSpeed(0.5f);
        camera.follow(100, 100, 1.0 / 60.0);
        double fastDistance = camera.getPosition().getX();
        
        assertTrue(fastDistance > slowDistance, 
            "Velocidade maior deve resultar em maior distância percorrida. Lenta: " + slowDistance + ", Rápida: " + fastDistance);
    }

    @Test
    void camera_follow_respectsDeltaTime() {
        camera.setPosition(0, 0);
        camera.setFollowSpeed(0.1f);
        
        camera.follow(100, 100, 0.001);
        double smallDeltaDistance = camera.getPosition().getX();
        
        camera.setPosition(0, 0);
        
        camera.follow(100, 100, 0.1);
        double largeDeltaDistance = camera.getPosition().getX();
        
        assertTrue(largeDeltaDistance > smallDeltaDistance, "DeltaTime maior deve resultar em maior movimento");
    }

    @Test
    void camera_setFollowSpeed_clampsValues() {
        camera.setFollowSpeed(1.5f); // Acima do máximo
        assertTrue(camera.getFollowSpeed() <= 1.0f, "FollowSpeed deve ser limitado a 1.0");
        
        camera.setFollowSpeed(0.001f); // Abaixo do mínimo
        assertTrue(camera.getFollowSpeed() >= 0.01f, "FollowSpeed deve ser no mínimo 0.01");
        
        camera.setFollowSpeed(0.5f); // Valor válido
        assertEquals(0.5f, camera.getFollowSpeed(), 0.001f, "FollowSpeed válido deve ser mantido");
    }

    @Test
    void camera_follow_smoothsMovement() {
        camera.setPosition(0, 0);
        camera.setFollowSpeed(0.1f);
        
        double previousDistance = Double.MAX_VALUE;
        
        for (int i = 0; i < 10; i++) {
            camera.follow(100, 100, 1.0 / 60.0);
            
            double currentDistance = Math.sqrt(
                Math.pow(100 - camera.getPosition().getX(), 2) +
                Math.pow(100 - camera.getPosition().getY(), 2)
            );
            
            assertTrue(currentDistance < previousDistance, "Distância deve diminuir a cada frame");
            previousDistance = currentDistance;
        }
    }

}

