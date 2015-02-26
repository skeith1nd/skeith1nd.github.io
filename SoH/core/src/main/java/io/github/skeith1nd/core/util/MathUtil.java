package io.github.skeith1nd.core.util;

public class MathUtil {
    public static int distance(int x1, int y1, int x2, int y2) {
        return (int)Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    }
}
