package com.example.equationcalculator.math.dto;

import com.example.equationcalculator.exception.MethodInapplicableException;

import java.util.ArrayList;
import java.util.List;

public class Calculation {

    private int iterations = 0;
    private List<Point> tabulation = new ArrayList<>();
    private Point result;
    private String inapplableReason;

    public Calculation() {

    }

    public Calculation(int iterations, List<Point> tabulation, Point result, String inapplableReason) {
        this.iterations = iterations;
        this.tabulation = tabulation;
        this.result = result;
        this.inapplableReason = inapplableReason;
    }

    public Calculation(int iterations, List<Point> tabulation, Point result) {
        this.iterations = iterations;
        this.tabulation = tabulation;
        this.result = result;
    }

    public Calculation(String inapplableReason) {
        this.inapplableReason = inapplableReason;
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

    public String getInapplableReason() {
        return inapplableReason;
    }

    public void setInapplableReason(String inapplableReason) {
        this.inapplableReason = inapplableReason;
    }
}
