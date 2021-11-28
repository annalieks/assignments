package com.univ.lexer.lab3.tokenizer;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum TokenType {
    DIRECTIVE(Constants.DIRECTIVE),
    HEX(Constants.HEX),
    OCT(Constants.OCT),
    KEYWORD(Constants.KEYWORD),
    PRIMITIVE_TYPE(Constants.TYPE),
    HEADER_NAME(Constants.HEADER_NAME),
    IDENTIFIER(Constants.IDENTIFIER),
    CHAR(Constants.CHAR),
    NUMBER_LITERAL(Constants.NUMBER),
    STRING_LITERAL(Constants.STRING_LITERAL),
    PUNCTUATION(Constants.PUNCTUATION),
    OPERATOR(Constants.OPERATOR),
    COMMENT(Constants.COMMENT),
    WHITESPACE("\\b\\B"),
    NEWLINE("\\b\\B"),
    UNRECOGNIZED(".");

    private final Pattern pattern;

    TokenType(String s) {
        pattern = Pattern.compile(s);
    }

    public static Token matchString(String s, boolean inDirective) {
        List<Token> tokens = new ArrayList<>();
        for (TokenType t : TokenType.values()) {
            if (!inDirective && t == HEADER_NAME)
                continue;
            Matcher m = t.pattern.matcher(s);
            if (m.lookingAt()) {
                tokens.add(new Token(t, m.group(0)));
            }
        }
        return tokens.stream().max(Comparator.comparingInt(x -> x.value.length())).orElse(null);
    }

}
