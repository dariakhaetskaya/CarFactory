package ru.nsu.fit.daria.carfactory.util;

import java.util.function.Predicate;

public class Utils {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    private Utils() {}

    public static class ValidationException extends RuntimeException {
    }

    public static <T> T ensuring(T t, Predicate<T> p) {
        if (!p.test(t)) {
            throw new ValidationException();
        }
        return t;
    }

    public static <T> T ensuringNotNull(T t) {
        if (t == null) {
            throw new ValidationException();
        }
        return t;
    }


}
