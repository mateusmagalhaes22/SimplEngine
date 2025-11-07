# SimplEngine (Java Library)

## Overview
SimplEngine is a minimal Java library you can reuse in other projects. It includes a simple window `Canvas` helper and a configurable `GameLoop` that can target 144 FPS by default.

## Features
- `Render.Canvas`: Utility to create a Swing window and drawing surface quickly.
- `GameLoop.GameLoop`: Lightweight game loop with start/stop, target FPS (default 144), delta-time, and FPS tracking.
- `Inputs.Key`: Enum with main keyboard keys (A-Z, 0-9, arrows, SPACE, ENTER, etc.).
- `Inputs.InputManager`: Singleton to track keyboard state (pressed, released, held down).

## Getting Started

### Prerequisites
- Java Development Kit (JDK) 21 (LTS)
- Apache Maven

On Ubuntu/Debian you can install JDK 21 with:

```bash
sudo apt-get update -y
sudo apt-get install -y openjdk-21-jdk
java -version   # should show 21.x
javac -version  # should show 21.x
```

### Installation (locally)
Build and install to your local Maven repository (~/.m2):

```bash
mvn -q test
mvn -q install
```

### Usage
1) Add the following dependency to your consumer project's `pom.xml`:

```xml
<dependency>
    <groupId>com.example</groupId>
    <artifactId>simplengine</artifactId>
    <version>0.1.0</version>
</dependency>
```

2) Create a window and run a 144 FPS loop:

```java
import javax.swing.JFrame;
import com.example.simplengine.Render.Canvas;
import com.example.simplengine.GameLoop.GameLoop;

public class Main {
    public static void main(String[] args) {
        // Create a window (EDT-safe)
        JFrame frame = Canvas.newCanvas(800, 600, "SimplEngine Demo");

        // Create a game loop (defaults to 144 FPS)
        GameLoop loop = new GameLoop(
            dt -> {
                // Update logic (dt is seconds since last frame)
                // e.g., update positions, physics, input, etc.
            },
            () -> {
                // Render logic (draw to your back buffer or component)
                // This sample keeps it simple; integrate your own renderer.
            }
        );

        loop.start();

        // Example: stop after 5 seconds
        new Thread(() -> {
            try { Thread.sleep(5000); } catch (InterruptedException ignored) {}
            loop.stop();
            frame.dispose();
        }).start();
    }
}
```

Notes:
- You can change the target FPS at runtime with `loop.setTargetFps(144);`.
- Query the measured FPS via `loop.getCurrentFps()`.
- For actual rendering, create and manage your own BufferStrategy/Graphics pipeline on the AWT canvas you add to the frame.

### Using Keyboard Input

SimplEngine provides a simple keyboard input system with the `InputManager` singleton and `Key` enum.

#### Setup

Register the input listener with your canvas:

```java
import javax.swing.JFrame;
import com.example.simplengine.Render.Canvas;
import com.example.simplengine.Inputs.InputManager;
import com.example.simplengine.Inputs.Key;

public class Main {
    public static void main(String[] args) {
        JFrame frame = Canvas.newCanvas(800, 600, "Input Demo");
        java.awt.Canvas surface = Canvas.getSurface();

        // Register keyboard listener
        InputManager input = InputManager.getInstance();
        surface.addKeyListener(input.getKeyListener());
        surface.setFocusable(true);
        surface.requestFocus();
    }
}
```

#### Polling Keys

The `InputManager` provides three main methods:

- **`isKeyDown(Key)`**: Returns `true` while the key is held down (continuous).
- **`isKeyPressed(Key)`**: Returns `true` only on the frame the key was pressed (single event).
- **`isKeyReleased(Key)`**: Returns `true` only on the frame the key was released (single event).

#### Example: Moving a Rectangle with WASD

```java
import com.example.simplengine.GameLoop.GameLoop;
import com.example.simplengine.GameObjects.Rect;
import com.example.simplengine.Inputs.InputManager;
import com.example.simplengine.Inputs.Key;

Rect player = new Rect(50, 50, 375, 275);

GameLoop loop = new GameLoop(
    dt -> {
        InputManager input = InputManager.getInstance();
        double speed = 200; // pixels per second

        // Continuous movement (while key is held)
        if (input.isKeyDown(Key.W)) {
            player.getPosition().setY(player.getPosition().getY() - speed * dt);
        }
        if (input.isKeyDown(Key.S)) {
            player.getPosition().setY(player.getPosition().getY() + speed * dt);
        }
        if (input.isKeyDown(Key.A)) {
            player.getPosition().setX(player.getPosition().getX() - speed * dt);
        }
        if (input.isKeyDown(Key.D)) {
            player.getPosition().setX(player.getPosition().getX() + speed * dt);
        }

        // Single action (e.g., jump)
        if (input.isKeyPressed(Key.SPACE)) {
            // Jump logic here
        }

        // IMPORTANT: Clear "just pressed/released" states at end of frame
        input.update();
    },
    () -> {
        Canvas.render(g -> {
            player.render(g);
        });
    }
);

loop.start();
```

#### Available Keys

The `Key` enum includes:
- **Letters**: `A` to `Z`
- **Numbers**: `NUM_0` to `NUM_9`
- **Arrows**: `ARROW_UP`, `ARROW_DOWN`, `ARROW_LEFT`, `ARROW_RIGHT`
- **Control**: `SPACE`, `ENTER`, `ESCAPE`, `BACKSPACE`, `TAB`
- **Modifiers**: `SHIFT`, `CTRL`, `ALT`
- **Function keys**: `F1` to `F12`

**Important**: Always call `input.update()` at the end of your update loop to clear transient key states (`isKeyPressed`/`isKeyReleased`).

## Running Tests
To run the tests included in this library, use the following Maven command:

```bash
mvn test
```

## Contributing
Contributions are welcome! Please feel free to submit a pull request or open an issue for any enhancements or bug fixes.

## Gradle consumer example

If your consumer project uses Gradle, point to the local Maven repo and add the dependency:

```gradle
repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    implementation 'com.example:simplengine:0.1.0'
}
```