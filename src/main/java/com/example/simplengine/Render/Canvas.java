package com.example.simplengine.Render;

import javax.swing.*;

import com.example.simplengine.Camera.Camera;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferStrategy;
import java.util.function.Consumer;

public final class Canvas {

	private static volatile java.awt.Canvas SURFACE;

	private static final Camera CAMERA = Camera.getInstance();

	public static JFrame newCanvas(int width, int height) {
		return newCanvas(width, height, "SimplEngine");
	}

	public static JFrame newCanvas(int width, int height, String titulo) {
		if (width <= 0 || height <= 0) {
			throw new IllegalArgumentException("width e height devem ser maiores que 0");
		}

		final JFrame[] frameHolder = new JFrame[1];
		Runnable createTask = () -> {
			JFrame frame = new JFrame(titulo);
			frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

			java.awt.Canvas surface = new java.awt.Canvas();
			surface.setPreferredSize(new Dimension(width, height));
			surface.setBackground(Color.BLACK);
			surface.setIgnoreRepaint(true);

			frame.setLayout(new BorderLayout());
			frame.add(surface, BorderLayout.CENTER);
			frame.pack();
			frame.setLocationRelativeTo(null);
			frame.setVisible(true);

			SURFACE = surface;
			frameHolder[0] = frame;
		};

		if (SwingUtilities.isEventDispatchThread()) {
			createTask.run();
		} else {
			try {
				SwingUtilities.invokeAndWait(createTask);
			} catch (Exception e) {
				throw new RuntimeException("Falha ao criar a janela na EDT", e);
			}
		}

		return frameHolder[0];
	}

	public static java.awt.Canvas getSurface() {
		return SURFACE;
	}

	public static void render(Consumer<Graphics2D> painter) {
		java.awt.Canvas surface = SURFACE;
		if (surface == null) {
			throw new IllegalStateException("Canvas surface ainda não foi criado. Chame Canvas.newCanvas primeiro.");
		}

		BufferStrategy bs = surface.getBufferStrategy();
		if (bs == null) {
			surface.createBufferStrategy(2);
			bs = surface.getBufferStrategy();
		}

		do {
			Graphics2D g = (Graphics2D) bs.getDrawGraphics();
			try {

				Color old = g.getColor();
				g.setColor(surface.getBackground());
				g.fillRect(0, 0, surface.getWidth(), surface.getHeight());
				g.setColor(old);

				AffineTransform originalTransform = g.getTransform();
				applyCameraTransform(g, surface.getWidth(), surface.getHeight());

				painter.accept(g);

				g.setTransform(originalTransform);
			} finally {
				g.dispose();
			}
			bs.show();
			Toolkit.getDefaultToolkit().sync();
		} while (bs.contentsLost());
	}

	private static void applyCameraTransform(Graphics2D g, int screenWidth, int screenHeight) {
		Camera camera = CAMERA;

		g.translate(screenWidth / 2.0, screenHeight / 2.0);

		float zoom = camera.getZoom();
		g.scale(zoom, zoom);
		
		g.translate(-camera.getPosition().getX(), -camera.getPosition().getY());
	}

	public static Camera getCamera() {
		return CAMERA;
	}
}

