# SimplEngine (Java Library)

## Overview
SimplEngine is a minimal Java library you can reuse in other projects. It includes a simple window `Canvas` helper and a configurable `GameLoop` that can target 144 FPS by default.

## Features
- `Render.Canvas`: Utility to create a Swing window and drawing surface quickly.
- `GameLoop.GameLoop`: Lightweight game loop with start/stop, target FPS (default 144), delta-time, and FPS tracking.
- `Inputs.Keys`: Enum with main keyboard keys (A-Z, 0-9, arrows, SPACE, ENTER, etc.).
- `Inputs.InputManager`: Singleton to track keyboard state (pressed, released, held down).
- `Camera.Camera`: 2D camera with position, zoom, and automatic view transformation.
- `GameObjects`: Base classes for game objects with collision detection (AABB).
  - `GameObject`: Abstract base class with position, velocity, gravity, and physics
  - `Rect`: Rectangle rendering with customizable colors
  - `SpriteObject`: Image rendering with scaling, rotation support, and all GameObject features

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

### Using the Camera System

SimplEngine includes a 2D camera with position, zoom, and automatic coordinate transformation. The camera is automatically integrated into `Canvas.render()`.

#### Basic Camera Setup

```java
import com.example.simplengine.Camera.Camera;
import com.example.simplengine.Render.Canvas;

// Get the camera instance
Camera camera = Canvas.getCamera();

// Set initial position (world coordinates)
camera.setPosition(0, 0);

// Set zoom level (1.0 = normal, 2.0 = 2x zoomed in, 0.5 = zoomed out)
camera.setZoom(1.0f);
```

#### Camera Methods

**Position Control:**
- `setPosition(float x, float y)` - Set absolute camera position
- `setPosition(Vector2 position)` - Set position from vector
- `move(float dx, float dy)` - Move camera relatively
- `centerOn(float x, float y)` - Center camera on a point (instant, no smoothing)
- `centerOn(Vector2 target)` - Center camera on a vector (instant, no smoothing)
- `follow(float x, float y, double deltaTime)` - **Smoothly follow a target** (recommended for player tracking)
- `follow(Vector2 target, double deltaTime)` - Smoothly follow a vector target

**Zoom Control:**
- `setZoom(float zoom)` - Set zoom level (minimum 0.1)
- `getZoom()` - Get current zoom level

**Follow Speed Control:**
- `setFollowSpeed(float speed)` - Set smoothing speed (0.05 = very smooth, 0.1 = smooth, 0.5 = fast, 1.0 = almost instant)
- `getFollowSpeed()` - Get current follow speed

**Coordinate Conversion:**
- `worldToScreen(worldX, worldY, screenWidth, screenHeight)` - Convert world coordinates to screen coordinates
- `screenToWorld(screenX, screenY, screenWidth, screenHeight)` - Convert screen coordinates to world coordinates

#### Example: Smooth Camera Following Player (Recommended)

```java
import com.example.simplengine.Camera.Camera;
import com.example.simplengine.GameObjects.Rect;
import com.example.simplengine.Inputs.InputManager;
import com.example.simplengine.Inputs.Keys;

Camera camera = Canvas.getCamera();
camera.setFollowSpeed(0.1f); // Ajuste entre 0.05 (muito suave) e 1.0 (quase instantâneo)

Rect player = new Rect(50, 50, 0, 0);

GameLoop loop = new GameLoop(
    dt -> {
        InputManager input = InputManager.getInstance();
        double speed = 200;

        // Move player with WASD
        if (input.isKeyDown(Keys.W)) {
            player.getPosition().setY(player.getPosition().getY() - speed * dt);
        }
        if (input.isKeyDown(Keys.S)) {
            player.getPosition().setY(player.getPosition().getY() + speed * dt);
        }
        if (input.isKeyDown(Keys.A)) {
            player.getPosition().setX(player.getPosition().getX() - speed * dt);
        }
        if (input.isKeyDown(Keys.D)) {
            player.getPosition().setX(player.getPosition().getX() + speed * dt);
        }

        // Câmera segue o jogador SUAVEMENTE (sem tremor)
        // Use follow() ao invés de centerOn() para movimento suave
        camera.follow(player.getPosition(), dt);

        // Zoom control with Q/E
        if (input.isKeyDown(Keys.Q)) {
            camera.setZoom(camera.getZoom() - 0.5f * (float) dt);
        }
        if (input.isKeyDown(Keys.E)) {
            camera.setZoom(camera.getZoom() + 0.5f * (float) dt);
        }

        input.update();
    },
    () -> {
        Canvas.render(g -> {
            // All objects are drawn in WORLD coordinates
            // Camera transformation is applied automatically
            player.render(g);
        });
    }
);
```

#### Example: Manual Camera Control

```java
Camera camera = Canvas.getCamera();

GameLoop loop = new GameLoop(
    dt -> {
        InputManager input = InputManager.getInstance();
        float cameraSpeed = 200; // pixels per second

        // Move camera with arrow keys
        if (input.isKeyDown(Keys.ARROW_UP)) {
            camera.move(0, -cameraSpeed * (float) dt);
        }
        if (input.isKeyDown(Keys.ARROW_DOWN)) {
            camera.move(0, cameraSpeed * (float) dt);
        }
        if (input.isKeyDown(Keys.ARROW_LEFT)) {
            camera.move(-cameraSpeed * (float) dt, 0);
        }
        if (input.isKeyDown(Keys.ARROW_RIGHT)) {
            camera.move(cameraSpeed * (float) dt, 0);
        }

        input.update();
    },
    () -> {
        Canvas.render(g -> {
            // Draw your game world here
        });
    }
);
```

#### Camera Coordinate System

- **World Coordinates**: Objects are positioned in world space (e.g., player at `(1000, 500)`)
- **Screen Coordinates**: Where objects appear on the screen (pixels from top-left)
- **Camera Position**: The point in world space that appears at the center of the screen
- **Zoom**: Magnification factor (1.0 = normal, 2.0 = objects appear 2x larger)

**Example**: If camera is at `(100, 100)` with zoom `1.0`:
- World point `(100, 100)` appears at screen center
- World point `(150, 100)` appears 50 pixels to the right of center
- With zoom `2.0`, that same point appears 100 pixels to the right

**Important Notes**:
- Draw all objects in **world coordinates** - the camera transform is applied automatically
- Camera position represents the center of the view, not the top-left corner
- Zoom is clamped to minimum `0.1` to prevent division by zero

### Using Sprites (Images)

SimplEngine provides the `SpriteObject` class for rendering images in your game. Sprites inherit from `GameObject`, so they have built-in support for position, velocity, gravity, and collision detection.

#### Creating a Sprite

```java
import com.example.simplengine.GameObjects.SpriteObject;
import java.io.IOException;

// Load sprite from image file
try {
    SpriteObject player = new SpriteObject("assets/player.png", 100, 100);
    
    // The sprite will be rendered at position (100, 100)
    // with its original image dimensions
} catch (IOException e) {
    System.err.println("Failed to load image: " + e.getMessage());
}
```

#### Sprite Constructors

```java
// 1. Load image with original size
SpriteObject sprite = new SpriteObject("path/to/image.png", x, y);

// 2. Load image and resize to specific dimensions
SpriteObject sprite = new SpriteObject("path/to/image.png", x, y, width, height);

// 3. Create from existing BufferedImage
BufferedImage img = ...; // your image
SpriteObject sprite = new SpriteObject(img, x, y);
```

#### Sprite Methods

**Scaling:**
- `setScale(float scale)` - Set uniform scale (1.0 = 100%, 2.0 = 200%)
- `setScale(float scaleX, float scaleY)` - Set non-uniform scale
- `setSize(int width, int height)` - Set specific pixel dimensions

**Image Management:**
- `getImage()` - Get the BufferedImage
- `setImage(BufferedImage img)` - Change the sprite's image
- `getWidth()` / `getHeight()` - Get current dimensions (including scale)

**GameObject Features:**
- All standard GameObject methods work: `setPosition()`, `setSpeed()`, `setHasGravity()`, `update()`, `intersects()`, etc.

#### Example: Animated Player Sprite

```java
import com.example.simplengine.GameObjects.SpriteObject;
import com.example.simplengine.Inputs.InputManager;
import com.example.simplengine.Inputs.Keys;
import com.example.simplengine.Camera.Camera;
import com.example.simplengine.Vectors.Vector2;

try {
    // Create player sprite
    SpriteObject player = new SpriteObject("assets/player.png", 400, 300);
    
    // Optional: scale the sprite
    player.setScale(2.0f); // 2x larger
    
    // Optional: enable physics
    player.setHasGravity(true);
    
    Camera camera = Canvas.getCamera();
    camera.setFollowSpeed(0.1f);
    
    GameLoop loop = new GameLoop(
        dt -> {
            InputManager input = InputManager.getInstance();
            double speed = 200;
            
            // Move player with arrow keys
            if (input.isKeyDown(Keys.ARROW_LEFT)) {
                player.getSpeed().setX(-speed);
            } else if (input.isKeyDown(Keys.ARROW_RIGHT)) {
                player.getSpeed().setX(speed);
            } else {
                player.getSpeed().setX(0);
            }
            
            // Jump
            if (input.isKeyPressed(Keys.SPACE) && player.getPosition().getY() >= 500) {
                player.getSpeed().setY(-500); // Jump velocity
            }
            
            // Update player physics
            player.update(dt);
            
            // Keep player above ground
            if (player.getPosition().getY() > 500) {
                player.getPosition().setY(500);
                player.getSpeed().setY(0);
            }
            
            // Camera follows player smoothly
            camera.follow(player.getPosition(), dt);
            
            input.update();
        },
        () -> {
            Canvas.render(g -> {
                // Render player sprite
                player.render(g);
            });
        }
    );
    
    loop.start();
    
} catch (IOException e) {
    System.err.println("Failed to load player sprite: " + e.getMessage());
}
```

#### Sprite Collision Detection

Sprites inherit collision detection from GameObject:

```java
SpriteObject player = new SpriteObject("player.png", 100, 100);
SpriteObject enemy = new SpriteObject("enemy.png", 150, 100);

if (player.intersects(enemy)) {
    System.out.println("Player hit enemy!");
}

// Get bounding box for custom collision logic
Rectangle2D.Float bounds = player.getBounds();
```

#### Supported Image Formats

`SpriteObject` uses `javax.imageio.ImageIO`, which supports:
- PNG (recommended for transparency)
- JPG/JPEG
- BMP
- GIF (static images)

**Tips:**
- Use PNG for sprites with transparency
- Images are loaded once and cached in memory
- For animations, load multiple images and swap them with `setImage()`
- Sprites are rendered in world coordinates - camera transformation is automatic

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