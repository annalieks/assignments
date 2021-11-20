package com.univ.lexer;

import com.univ.lexer.processor.LineProcessor;
import com.univ.lexer.tokenizer.Token;
import com.univ.lexer.tokenizer.TokenProcessor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

class LexerApplication {

    public static void main(String[] args) {
        Stream<String> s = null;
        if (args.length == 0 || Objects.equals(args[0], "-")) {
            s = new BufferedReader(new InputStreamReader(System.in)).lines();
        } else {
            Path p = Paths.get(args[0]);
            if (!Files.isRegularFile(p)) {
                System.err.println(p + " is not a valid file!");
                System.exit(1);
            }
            try {
                s = Files.lines(p);
            } catch (IOException e) {
                System.err.println("Failed to open " + p);
                System.exit(2);
            }
        }
        processStream(s);
    }

    private static void processStream(Stream<String> s) {
        Stream<String> lines = LineProcessor.preprocess(s);
        Stream<Token> tokens = new TokenProcessor(lines).splitToTokens();
        tokens.forEach(token ->
            System.out.println("\"" + token.value + "\"" + " - " + token.type.name())
        );
    }
}