package com.univ.lexer.lab4.tokenizer;

import org.apache.commons.lang3.math.NumberUtils;

import java.util.ArrayList;
import java.util.List;

import static com.univ.lexer.lab4.tokenizer.Constants.KEYWORDS;
import static com.univ.lexer.lab4.tokenizer.Constants.TYPES;

public class TokenAutomata implements Automata {
    private final List<Token> tokens = new ArrayList<>();

    public void printTokens() {
        for (Token t : tokens) {
            System.out.println(t);
        }
    }

    public void processCommentState(String line) {
        if (line.startsWith("//") || line.startsWith("/*")) {
            tokens.add(new Token(TokenType.COMMENT, line));
        }
    }

    public void processStringState(String line) {
        StringBuilder word = new StringBuilder();
        word.append('\"');
        for (int i = 0; i < line.length(); i++) {
            if (line.charAt(i) == '\"') {
                word.append('\"');
                tokens.add(new Token(TokenType.STRING_LITERAL, word.toString()));
                processLine(line.substring(i + 1));
                return;
            } else {
                word.append(line.charAt(i));
            }
        }
        tokens.add(new Token(TokenType.UNRECOGNIZED, word.toString()));
    }

    public void processNumberState(String line) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < line.length(); i++) {
            if (Character.isDigit(line.charAt(i))
                    || Character.isAlphabetic(line.charAt(i)) || line.charAt(i) == '.') {
                builder.append(line.charAt(i));
            } else {
                tokens.add(extractToken(builder.toString()));
                processLine(line.substring(i));
                return;
            }
        }
        tokens.add(extractToken(builder.toString()));
    }

    private Token extractToken(String word) {
        if (isNumber(word))
            return new Token(TokenType.NUMBER_LITERAL, word);
        else if (isNumber(word, 16))
            return new Token(TokenType.HEX, word);
        else if (isNumber(word, 8))
            return new Token(TokenType.OCT, word);
        else
            return extractType(word);

    }

    private void processWord(String line) {
        StringBuilder word = new StringBuilder();
        for (int i = 0; i < line.length(); i++) {
            if (Character.isAlphabetic(line.charAt(i)) ||
                    Character.isDigit(line.charAt(i)) ||
                    line.charAt(i) == '_' || line.charAt(i) == '\'') {
                word.append(line.charAt(i));
            } else {
                Token token = extractToken(word.toString());
                tokens.add(token);
                if (token.getType() == TokenType.DIRECTIVE)
                    processDirectiveState(line.substring(i));
                else
                    processLine(line.substring(i));
                return;
            }
        }
        tokens.add(extractType(word.toString()));
    }

    private Token extractType(String word) {
        if (KEYWORDS.contains(word))
            return new Token(TokenType.KEYWORD, word);
        else if (TYPES.contains(word))
            return new Token(TokenType.PRIMITIVE_TYPE, word);
        else if (word.length() == 3 && isChar(word))
            return new Token(TokenType.CHAR, word);
        else if (isIdentifier(word))
            return new Token(TokenType.IDENTIFIER, word);
        else
            return new Token(TokenType.UNRECOGNIZED, word);
    }

    public void processDirectiveState(String line) {
        String[] elements = line.split(" ", 2);
        tokens.add(new Token(TokenType.DIRECTIVE, elements[0].strip()));
        tokens.add(new Token(TokenType.HEADER_NAME, elements[1].strip()));
    }

    public void processPunctuationState(String line) {
        tokens.add(new Token(TokenType.PUNCTUATION, line));
    }

    private boolean isNumber(String number) {
        return NumberUtils.isCreatable(number);
    }

    private boolean isNumber(String number, int radix) {
        try {
            Long.parseLong(number, radix);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isIdentifier(String strIdent) {
        char first = strIdent.charAt(0);
        if (first == '_' || Character.isAlphabetic(first)) {
            for (int i = 1; i < strIdent.length(); i++) {
                if (!Character.isAlphabetic(strIdent.charAt(i)) &&
                        !Character.isDigit(strIdent.charAt(i)) && strIdent.charAt(i) != '_')
                    return false;

            }
            return true;
        } else return false;
    }

    private boolean isChar(String strChar) {
        return strChar.charAt(0) == '\'' && strChar.charAt(2) == '\'';
    }

    public void processLine(String line) {
        line = line.trim();
        int length = line.length();
        for (int i = 0; i < length; i++) {
            char curr = line.charAt(i);
            Token token;

            switch (curr) {
                case '>': {
                    if (i != length - 1 && line.charAt(i + 1) == '=') {
                        token = new Token(TokenType.OPERATOR, ">=");
                        i++;
                    } else {
                        token = new Token(TokenType.OPERATOR, ">");
                    }
                    tokens.add(token);
                    break;
                }
                case '&': {
                    Token lv;
                    if (i != length - 1 && line.charAt(i + 1) == '&') {
                        lv = new Token(TokenType.OPERATOR, "&&");
                        i++;
                    } else {
                        lv = new Token(TokenType.OPERATOR, "&");
                    }
                    tokens.add(lv);
                    break;
                }
                case '|': {
                    if (i != length - 1 && line.charAt(i + 1) == '|') {
                        token = new Token(TokenType.OPERATOR, "||");
                        i++;
                    } else {
                        token = new Token(TokenType.OPERATOR, "|");
                    }
                    tokens.add(token);
                    break;
                }
                case '<': {
                    if (i != length - 1 && line.charAt(i + 1) == '=') {
                        token = new Token(TokenType.OPERATOR, "<=");
                        i++;
                    } else {
                        token = new Token(TokenType.OPERATOR, "<");
                    }
                    tokens.add(token);
                    break;
                }
                case '=': {
                    if (i != length - 1 && line.charAt(i + 1) == '=') {
                        token = new Token(TokenType.OPERATOR, "==");
                        i++;
                    } else {
                        token = new Token(TokenType.OPERATOR, "=");
                    }
                    tokens.add(token);
                    break;
                }
                case '!': {
                    if (i != length - 1 && line.charAt(i + 1) == '=') {
                        token = new Token(TokenType.OPERATOR, "!=");
                        i++;
                    } else {
                        token = new Token(TokenType.OPERATOR, "!");
                    }
                    tokens.add(token);
                    break;
                }
                case '*': {
                    if (i != length - 1 && line.charAt(i + 1) == '=') {
                        token = new Token(TokenType.OPERATOR, "*=");
                        i++;
                    } else {
                        token = new Token(TokenType.OPERATOR, "*");
                    }
                    tokens.add(token);
                    break;
                }
                case '%': {
                    if (i != length - 1 && line.charAt(i + 1) == '=') {
                        token = new Token(TokenType.OPERATOR, "%=");
                        i++;
                    } else {
                        token = new Token(TokenType.OPERATOR, "%");
                    }
                    tokens.add(token);
                    break;
                }
                case '/': {
                    if (i != length - 1 && line.charAt(i + 1) == '=') {
                        token = new Token(TokenType.OPERATOR, "/=");
                        tokens.add(token);
                        i++;
                        break;
                    } else if (i != length - 1 && (line.charAt(i + 1) == '*' || line.charAt(i + 1) == '/')) {
                        processCommentState(line.substring(i));
                        return;
                    } else {
                        token = new Token(TokenType.OPERATOR, "/");
                        tokens.add(token);
                        break;
                    }
                }
                case '+': {
                    if (i != length - 1 && line.charAt(i + 1) == '=') {
                        token = new Token(TokenType.OPERATOR, "+=");
                        i++;
                    } else if (i != length - 1 && line.charAt(i + 1) == '+') {
                        token = new Token(TokenType.OPERATOR, "++");
                        i++;
                    } else {
                        token = new Token(TokenType.OPERATOR, "+");
                    }
                    tokens.add(token);
                    break;
                }
                case '-': {
                    if (i != length - 1 && line.charAt(i + 1) == '=') {
                        token = new Token(TokenType.OPERATOR, "-=");
                        i++;
                    } else if (i != length - 1 && line.charAt(i + 1) == '-') {
                        token = new Token(TokenType.OPERATOR, "--");
                        i++;
                    } else {
                        token = new Token(TokenType.OPERATOR, "-");
                    }
                    tokens.add(token);
                    break;
                }
                case '(':
                case ')':
                case '{':
                case '}':
                case '[':
                case ']':
                case ',':
                case '.':
                case ';': {
                    processPunctuationState(String.valueOf(curr));
                    break;
                }
                case '\"': {
                    processStringState(line.substring(i + 1));
                    return;
                }
                case '#': {
                    processDirectiveState(line);
                    return;
                }
                default: {
                    if (Character.isDigit(curr)) {
                        processNumberState(line.substring(i));
                        return;
                    } else if (Character.isLetter(curr) || curr == '_' || curr == '\'') {
                        processWord(line.substring(i));
                        return;
                    } else if (Character.isWhitespace(curr)) {
                        break;
                    } else {
                        Token lv = new Token(TokenType.UNRECOGNIZED, Character.toString(curr));
                        tokens.add(lv);
                        break;
                    }
                }
            }
        }
    }

}
