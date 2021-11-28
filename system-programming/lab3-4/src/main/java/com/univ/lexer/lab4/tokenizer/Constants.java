package com.univ.lexer.lab4.tokenizer;

import java.util.List;

public class Constants {

    private Constants() {
    }

    public static final List<String> KEYWORDS = List.of(
            "boolean", "break", "byte", "case", "true", "try", "void", "default", "do", "double", "else",
            "enum", "false", "final", "finally", "float", "for", "goto", "if", "int", "long", "new", "null",
            "return", "short", "switch", "throw"
    );

    public static final List<String> OPERATORS = List.of(
            "<<=", ">>=", "...", "%=", "+=", "-=", "&=", "^=",
            "->", "++", "--", "<=", ">=", "==", "!=", "<<", ">>",
            "&&", "||", "*=", "/=", "|=", "~", "!", "/", "%", "<", ">",
            "^", "|", "?", ":", ";", "=",
            ".", "&", "*", "+", "-", ",");

    public static final List<String> TYPES = List.of(
            "bool", "char", "signed char", "unsigned char", "short",
            "short int", "signed short", "signed short int", "unsigned short",
            "unsigned short int", "double", "float", "long double", "long",
            "long int", "signed long", "signed long int", "unsigned long",
            "unsigned long int", "int", "signed int", "unsigned int", "long long",
            "long long int", "signed long long", "signed long long int",
            "unsigned long long", "unsigned long long int", "void"
    );

}
