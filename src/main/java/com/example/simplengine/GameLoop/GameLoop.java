package com.example.simplengine.GameLoop;

public class GameLoop {
	private volatile int targetFps = 144;

	private volatile long targetFrameNanos = 1_000_000_000L / targetFps;

	private volatile boolean running = false;
	private Thread loopThread;

	public interface Updater { void update(double dtSeconds); }

	public interface Renderer { void render(); }

	private Updater updater;
	private Renderer renderer;

	private volatile int currentFps = 0;

	public GameLoop() { }

	public GameLoop(Updater updater, Renderer renderer) {
		this.updater = updater;
		this.renderer = renderer;
	}

	public GameLoop(int targetFps, Updater updater, Renderer renderer) {
		setTargetFps(targetFps);
		this.updater = updater;
		this.renderer = renderer;
	}

	public synchronized void start() {
		if (running) return;
		running = true;
		loopThread = new Thread(this::runLoop, "SimplEngine-GameLoop");
		loopThread.setDaemon(true);
		loopThread.start();
	}

	public synchronized void stop() {
		running = false;
		if (loopThread != null) {
			try {
				loopThread.join(1000);
			} catch (InterruptedException ignored) {
				Thread.currentThread().interrupt();
			} finally {
				loopThread = null;
			}
		}
	}

	public boolean isRunning() { return running; }

	public void setUpdater(Updater updater) { this.updater = updater; }
	public void setRenderer(Renderer renderer) { this.renderer = renderer; }

	public void setTargetFps(int fps) {
		if (fps <= 0) return;
		this.targetFps = fps;
		this.targetFrameNanos = 1_000_000_000L / Math.max(1, fps);
	}

	public int getTargetFps() { return targetFps; }
	public int getCurrentFps() { return currentFps; }

	private void runLoop() {
		long lastTime = System.nanoTime();
		long lastFpsTick = lastTime;
		int frames = 0;

		while (running) {
			final long frameStart = System.nanoTime();
			final double dtSeconds = (frameStart - lastTime) / 1_000_000_000.0;
			lastTime = frameStart;

			if (updater != null) {
				try { updater.update(dtSeconds); } catch (Throwable t) {}
			}

			if (renderer != null) {
				try { renderer.render(); } catch (Throwable t) {}
			}

			final long afterWork = System.nanoTime();
			final long workNanos = afterWork - frameStart;
			long remaining = targetFrameNanos - workNanos;

			if (remaining > 0) {
				final long sleepMs = remaining / 1_000_000L;
				final int sleepNs = (int) (remaining % 1_000_000L);
				try {
					if (sleepMs > 0 || sleepNs > 0) {
						Thread.sleep(sleepMs, sleepNs);
					}
				} catch (InterruptedException ignored) {
					Thread.currentThread().interrupt();
				}
			} else {
				Thread.yield();
			}

			frames++;
			final long now = System.nanoTime();
			if (now - lastFpsTick >= 1_000_000_000L) {
				currentFps = frames;
				frames = 0;
				lastFpsTick = now;
			}
		}
	}
}
