package com.example.equationcalculator.math.method;

public enum MethodType {
    BISECTION(0),
    NEWTON(1),
    SECANT(2);

    private final int num;

    private MethodType(int num) {
        this.num = num;
    }

    public int getNum() {
        return num;
    }
}
