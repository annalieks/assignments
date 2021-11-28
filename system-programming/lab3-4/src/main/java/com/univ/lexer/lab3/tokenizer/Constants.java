package com.univ.lexer.lab3.tokenizer;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Constants {

    public static final String DIRECTIVE = "#[ ]*(define|import|include|elif|else|ifndef|" +
            "error|if|ifdef|pragma|line|undef|using|endif)";
    public static final String KEYWORD = "(asm|auto|break|case|catch|class|const|const_cast|" +
            "continue|delete|do|new|sizeof|volatile|" +
            "goto|static|explicit|export|false|else|try|true|" +
            "this|throw|typeid|switch|struct|sizeof|enum|if|register|typedef|" +
            "operator|mutable|namespace|template|extern|inline|return|" +
            "union|using|virtual|while|default|for)";
    public static final String TYPE = "(bool|" +
            "char|signed char|unsigned char|" +
            "short|short int|signed short|signed short int|unsigned short|unsigned short int|" +
            "double|float|long double|" +
            "long|long int|signed long|signed long int|unsigned long|unsigned long int|" +
            "int|signed int|unsigned int|" +
            "long long|long long int|signed long long|" +
            "signed long long int|unsigned long long|unsigned long long int|" +
            "void)";
    public static final String HEX = "0x[0-9A-Fa-f]*";
    public static final String OCT = "0[1-7][0-7]*";
    public static final String HEADER_NAME = "(<[^>]+>|\"[^\"]+\")";
    public static final String COMMENT = "(/\\*|//)";
    public static final String OPERATOR = List.of(".", "->", "++", "--", "&", "*", "+", "-",
            "~", "!", "/", "%", "<<", ">>", "<", ">",
            "<=", ">=", "==", "!=", "^", "|", "&&",
            "||", "?", ":", ";", "...", "=", "*=", "/=",
            "%=", "+=", "-=", "<<=", ">>=", "&=", "^=",
            "|=", ",").stream()
            .map(Pattern::quote)
            .collect(Collectors.joining("|", "(", ")"));
    public static final String PUNCTUATION = List.of("[", "]", "(", ")", "{", "}", "#", "##", ";")
            .stream()
            .map(Pattern::quote)
            .collect(Collectors.joining("|", "(", ")"));
    public static final String IDENTIFIER = "[_A-Za-z][0-9A-Za-z_]*";
    public static final String NUMBER = "[-+ ]?[0-9]*[.]?[0-9]+([eE][-+]?[0-9]+)?";
    public static final String CHAR = "'[^']+'";
    public static final String STRING_LITERAL = "\"(?:\\\\\"|[^\"])*?\"";

    private Constants() {
    }

}
