package com.example.simplengine.Inputs;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import com.example.simplengine.Vectors.Vector2;

public class MouseManager implements MouseListener, MouseMotionListener {

    public static MouseManager instance;
    private boolean leftPressed, rightPressed, middlePressed;
    private Vector2 mousePosition;

    private MouseManager() {
        this.mousePosition = new Vector2(0, 0);
    }

    public static MouseManager getInstance() {
        if (instance == null) {
            instance = new MouseManager();
        }
        return instance;
    }

    public Vector2 getMousePosition() {
        return mousePosition;
    }

    public double getMouseX() {
        return mousePosition.getX();
    }

    public double getMouseY() {
        return mousePosition.getY();
    }

    public boolean isLeftPressed() {
        return leftPressed;
    }
    
    public boolean isRightPressed() {
        return rightPressed;
    }
    
    public boolean isMiddlePressed() {
        return middlePressed;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            leftPressed = true;
        } else if (e.getButton() == MouseEvent.BUTTON2) {
            middlePressed = true;
        } else if (e.getButton() == MouseEvent.BUTTON3) {
            rightPressed = true;
        }
    }
    
    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            leftPressed = false;
        } else if (e.getButton() == MouseEvent.BUTTON2) {
            middlePressed = false;
        } else if (e.getButton() == MouseEvent.BUTTON3) {
            rightPressed = false;
        }
    }
    
    @Override
    public void mouseClicked(MouseEvent e) {
        // Pode adicionar lógica para cliques únicos
    }
    
    @Override
    public void mouseEntered(MouseEvent e) {
        // Mouse entrou no componente
    }
    
    @Override
    public void mouseExited(MouseEvent e) {
        // Mouse saiu do componente
    }
    
    @Override
    public void mouseMoved(MouseEvent e) {
        mousePosition.setX(e.getX());
        mousePosition.setY(e.getY());
    }
    
    @Override
    public void mouseDragged(MouseEvent e) {
        mousePosition.setX(e.getX());
        mousePosition.setY(e.getY());
    }
}