package com.example.equationcalculator;

import org.junit.Assert;
import org.junit.Test;
import org.mariuszgromada.math.mxparser.Function;

import com.example.equationcalculator.exception.InvalidExpressionException;
import com.example.equationcalculator.math.parser.ExpressionParser;

public class ExpressionParserTest {
    @Test
    public void correctString_isParsed() throws InvalidExpressionException {
        String str = "x + sin(x) + x^2";
        Function f = ExpressionParser.parse(str);
        Assert.assertTrue(f.checkSyntax());
    }

    @Test
    public void invalidString_throwsException() {
        String str = "x + sin(x) + cosabs(x)";
        Assert.assertThrows(InvalidExpressionException.class,
                () -> ExpressionParser.parse(str, "x"));
    }
}