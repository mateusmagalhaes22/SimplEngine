# SimplEngine (Java Library)

## Overview
SimplEngine is a minimal Java library you can reuse in other projects. It currently provides a friendly greeting API and is set up to be consumed as a JAR dependency.

## Features
- `Library.greet(String name)`: Returns a greeting message for the specified name.

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
1. Add the following dependency to your consumer project's `pom.xml`:

```xml
<dependency>
    <groupId>com.example</groupId>
    <artifactId>simplengine</artifactId>
    <version>0.1.0</version>
</dependency>
```

2. Import the Library class in your Java code:

```java
import com.example.simplengine.Library;
```

3. Use the `greet` method:

```java
String message = Library.greet("World");
System.out.println(message); // Outputs: Hello, World!
```

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