package com.univ;

import com.univ.automata.Automata;
import com.univ.filesystem.AutomataReader;

import java.nio.file.Path;
import java.util.Scanner;

public class AutomataApplication {

    public static void main(String[] args) {
        String word = null;
        Automata automata = AutomataReader.read(Path.of("input.txt"));
        automata.determineReachableStates();
        try (Scanner sc = new Scanner(System.in)) {
            while (!"exit".equals(word)) {
                System.out.println("Please, enter the word to check:");
                word = sc.nextLine();
                System.out.println("Is word allowed: " + automata.isValid(word));
            }
        }
    }

}
