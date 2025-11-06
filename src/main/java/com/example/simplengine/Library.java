package com.example.simplengine;

public final class Library {
    private Library() {}

    public static String greet(String name) {
        String who = (name == null || name.isBlank()) ? "World" : name;
        return "Hello, " + who + "!";
    }
}
