package com.univ.lexer.tokenizer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class TokenProcessor {
    private final Stream<String> lines;
    private final List<Token> tokens = new ArrayList<>();
    private boolean inCStyleComment = false;

    public TokenProcessor(Stream<String> lines) {
        this.lines = lines;
    }

    private void processLine(String s) {
        if (inCStyleComment) {
            int i = s.indexOf("*/");
            if (i == -1)
                return;
            inCStyleComment = false;
            tokens.add(new Token(TokenType.WHITESPACE, " "));
            s = s.substring(i + 2);

        }
        boolean inDirective = false;
        if (s.startsWith("#")) {
            inDirective = true;
        }
        iterate(s, inDirective);
        tokens.add(new Token(TokenType.NEWLINE, "\n"));
    }

    private void iterate(String s, boolean inDirective) {
        Token t;
        while (s.length() > 0) {
            int i = 0;
            while (i < s.length() && Character.isWhitespace(s.charAt(i)))
                i++;
            if (i > 0)
                tokens.add(new Token(TokenType.WHITESPACE, " "));
            s = s.substring(i);
            t = TokenType.matchString(s, inDirective);
            if (t == null)
                return;
            if (t.type == TokenType.CPP_COMMENT) {
                tokens.add(new Token(TokenType.WHITESPACE, " "));
                return;
            }
            if (t.type == TokenType.C_COMMENT) {
                inCStyleComment = true;
                processLine(s.substring(2));
                return;
            }
            tokens.add(t);
            s = s.substring(t.value.length());
        }
    }

    public Stream<Token> splitToTokens() {
        lines.forEach(this::processLine);
        return tokens.stream();
    }

}
