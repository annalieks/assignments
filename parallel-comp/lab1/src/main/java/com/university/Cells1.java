package com.university;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Synchronized version of the program
 */
public class Cells1 {

    private int N; // number of cells
    private int K; // number of atoms
    private double p; // the probability of movement left/right

    private final static int TIME_UNIT_MS = 100; // time unit

    int[] cells; // cells[i] - number of atoms in the i-th crystal cell

    public static void main(String[] args) throws InterruptedException {
        Cells1 cells = new Cells1(args);
        ExecutorService executorService = Executors.newFixedThreadPool(cells.K);

        System.out.println("Starting threads. Number of threads: " + cells.K);
        for (int i = 0; i < cells.K; i++) {
            executorService.execute(cells.new Particle());
        }

        System.out.println("Waiting for 10 seconds...");
        TimeUnit.SECONDS.sleep(10);
        executorService.shutdownNow();

        System.out.println("Threads have finished the execution.");
        int newK = 0;
        for (int i = 0; i < cells.N; i++) {
            newK += cells.cells[i];
        }
        System.out.println("Old atoms number: " + cells.K);
        System.out.println("New atoms number: " + newK);
    }

    public Cells1(String[] args) {
        parseParameters(args);
        this.cells = new int[N];
        cells[0] = K;
    }

    public void parseParameters(String[] args) {
        try {
            this.N = Integer.parseInt(args[0]);
            this.K = Integer.parseInt(args[1]);
            this.p = Double.parseDouble(args[2]);
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            System.err.println("Please check that N, K, p are specified correctly");
            System.exit(1);
        }
        if (N <= 0 || K <= 0 || p > 0.5 || p <= 0) {
            System.err.println("Incorrect N, K, or p values");
            System.exit(1);
        }

    }

    public synchronized void moveParticle(int from, int to) {
        cells[from]--;
        cells[to]++;
        System.out.println("Atom has moved from cell no." + from + " to cell no." + to);
    }

    private class Particle implements Runnable {

        private final Random random = new Random();

        private int cell = 0;

        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                double probability = getProbability();
                if (probability < p && cell != 0) { // go left
                    moveParticle(cell, cell - 1);
                    cell--;
                } else if (probability < 2 * p && cell != N - 1) { // go right
                    moveParticle(cell, cell + 1);
                    cell++;
                } // position remains the same otherwise

                try {
                    TimeUnit.MILLISECONDS.sleep(TIME_UNIT_MS);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }

        }

        private double getProbability() {
            return random.nextDouble();
        }
    }

}
