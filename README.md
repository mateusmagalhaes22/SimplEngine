# SimplEngine (Java Library)

## Overview
SimplEngine is a minimal Java library you can reuse in other projects. It includes a simple window `Canvas` helper and a configurable `GameLoop` that can target 144 FPS by default.

## Features
- `Render.Canvas`: Utility to create a Swing window and drawing surface quickly.
- `GameLoop.GameLoop`: Lightweight game loop with start/stop, target FPS (default 144), delta-time, and FPS tracking.

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

## Running Tests
To run the tests included in this library, use the following Maven command:

```bash
mvn test
```

## Contributing
Contributions are welcome! Please feel free to submit a pull request or open an issue for any enhancements or bug fixes.

## License
This project is licensed under the MIT License. See the LICENSE file for more details.

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