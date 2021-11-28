package com.univ.lexer.lab4.tokenizer;

public interface Automata {

    void processCommentState(String line);

    void processStringState(String line);

    void processNumberState(String line);

    void processPunctuationState(String line);

}
