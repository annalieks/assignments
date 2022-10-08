package com.example.equationcalculator.math.dto;

import java.util.ArrayList;
import java.util.List;

public class Calculation {

    private int iterations = 0;
    private List<Point> tabulation = new ArrayList<>();
    private Point result;

    public Calculation(int iterations, List<Point> tabulation, Point result) {
        this.iterations = iterations;
        this.tabulation = tabulation;
        this.result = result;
    }

    public List<Point> getTabulation() {
        return tabulation;
    }

    public void setTabulation(List<Point> tabulation) {
        this.tabulation = tabulation;
    }

    public int getIterations() {
        return iterations;
    }

    public void setIterations(int iterations) {
        this.iterations = iterations;
    }

    public Point getResult() {
        return result;
    }

    public void setResult(Point result) {
        this.result = result;
    }
}
