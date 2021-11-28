package com.univ.lexer.lab4;

import com.univ.lexer.lab4.tokenizer.TokenAutomata;

import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class LexerApplication {

    public static void main(String[] args) {
        Path path = Paths.get(args[0]);
        try (Scanner sc = new Scanner(path.toFile())) {
            TokenAutomata tokenAutomata = new TokenAutomata();

            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                tokenAutomata.processLine(line);
            }

            tokenAutomata.printTokens();
        } catch (FileNotFoundException error) {
            System.out.println("File " + args[0] + "not found");
        }
    }

}
