package com.example.equationcalculator;

import com.example.equationcalculator.exception.InvalidExpressionException;
import com.example.equationcalculator.exception.MethodInapplicableException;
import com.example.equationcalculator.math.parser.ExpressionParser;
import com.example.equationcalculator.math.method.NewtonMethod;
import com.example.equationcalculator.math.dto.Calculation;
import com.example.equationcalculator.math.dto.Point;

import org.junit.Assert;
import org.junit.Test;
import org.mariuszgromada.math.mxparser.Function;

public class NewtonMethodTest {

    @Test
    public void correctData_solutionFound() throws InvalidExpressionException,
            MethodInapplicableException {
        Function f = ExpressionParser.parse("x^3-3*x-3");

        double x1 = 2;
        double precision = 0.01;
        double delta = 0.001; // to compare double values

        Calculation res = NewtonMethod.compute(f, x1, precision);
        Assert.assertEquals(2, res.getIterations());
        Assert.assertEquals(3, res.getTabulation().size());
        Assert.assertNotNull(res.getResult());

        Point p0 = res.getTabulation().get(0),
                p1 = res.getTabulation().get(1),
                p2 = res.getTabulation().get(2);

        Assert.assertEquals(2.0, p0.getX(), delta);
        Assert.assertEquals(-1.0, p0.getY(), delta);

        Assert.assertEquals(2.111, p1.getX(), delta);
        Assert.assertEquals(0.075, p1.getY(), delta);

        Assert.assertEquals(2.103, p2.getX(), delta);
        Assert.assertEquals(-0.011, p2.getY(), delta);

        Assert.assertEquals(2.103, res.getResult().getX(), delta);
        Assert.assertEquals(-0.011, res.getResult().getY(), delta);

    }

    @Test
    public void invalidFunction_exceptionIsThrown() throws InvalidExpressionException {
        Function f = ExpressionParser.parse("1/x");
        double x1 = 0;
        double precision = 0.01;

        Assert.assertThrows(MethodInapplicableException.class,
                () -> NewtonMethod.compute(f, x1, precision));
    }
}