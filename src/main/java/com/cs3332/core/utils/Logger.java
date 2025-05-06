package com.cs3332.core.utils;

public final class Logger {
    private static final String RESET = "\u001B[0m";
    private static final String RED = "\u001B[31m";
    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String CYAN = "\u001B[36m";
    private static final String GRAY = "\u001B[90m";

    private Logger() {
        // Prevent instantiation
    }

    public static void info(String message, Object... args) {
        log(GREEN, "INFO", message, args);
    }

    public static void warn(String message, Object... args) {
        log(YELLOW, "WARN", message, args);
    }

    public static void error(String message, Object... args) {
        log(RED, "ERROR", message, args);
    }

    public static void debug(String message, Object... args) {
        log(CYAN, "DEBUG", message, args);
    }

    private static void log(String color, String level, String message, Object... args) {
        String formatted = format(message, args);
        System.out.println(String.format("%s[%s] %s%s", color, level, formatted, RESET));
    }

    private static String format(String message, Object... args) {
        if (args == null || args.length == 0) return message;
        StringBuilder result = new StringBuilder();
        int argIndex = 0;
        for (int i = 0; i < message.length(); i++) {
            if (i + 1 < message.length() && message.charAt(i) == '{' && message.charAt(i + 1) == '}') {
                if (argIndex < args.length) {
                    result.append(args[argIndex++]);
                    i++; // skip '}'
                } else {
                    result.append("{}");
                }
            } else {
                result.append(message.charAt(i));
            }
        }
        return result.toString();
    }
}
