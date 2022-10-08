package com.example.equationcalculator.math.parser;

import com.example.equationcalculator.exception.InvalidExpressionException;

import org.mariuszgromada.math.mxparser.Function;

public class ExpressionParser {

    public static Function parse(String expr) throws InvalidExpressionException {
        return parse(expr, "x");
    }

    public static Function parse(String expr, String varName) throws InvalidExpressionException {
        Function f = new Function(String.format("f(%s)=%s", varName, expr));
        if (!f.checkSyntax()) {
            throw new InvalidExpressionException("The provided function is invalid");
        }
        return f;
    }

}
