package com.univ.lexer.lab3;

import com.univ.lexer.lab3.processor.LineProcessor;
import com.univ.lexer.lab3.tokenizer.Token;
import com.univ.lexer.lab3.tokenizer.TokenProcessor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

class LexerApplication {

    public static void main(String[] args) {
        Path p = Paths.get(args[0]);
        if (!Files.isRegularFile(p)) {
            System.err.println(p + " is not a valid file!");
            System.exit(1);
        }
        try {
            processStream(Files.lines(p));
        } catch (IOException e) {
            System.err.println("Failed to open " + p);
            System.exit(2);
        }
    }

    private static void processStream(Stream<String> s) {
        Stream<String> lines = LineProcessor.preprocess(s);
        Stream<Token> tokens = new TokenProcessor(lines).splitToTokens();
        tokens.forEach(token ->
                System.out.println("\"" + token.value + "\"" + " - " + token.type.name())
        );
    }
}