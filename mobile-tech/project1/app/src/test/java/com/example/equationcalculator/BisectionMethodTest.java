package com.example.equationcalculator;

import com.example.equationcalculator.exception.InvalidExpressionException;
import com.example.equationcalculator.exception.MethodInapplicableException;
import com.example.equationcalculator.math.dto.Point;
import com.example.equationcalculator.math.method.BisectionMethod;
import com.example.equationcalculator.math.parser.ExpressionParser;
import com.example.equationcalculator.math.dto.Calculation;
import com.example.equationcalculator.math.dto.Interval;

import org.junit.Assert;
import org.junit.Test;
import org.mariuszgromada.math.mxparser.Function;

import java.util.Arrays;
import java.util.List;

public class BisectionMethodTest {

    /**
     * N1
     * -2 4 -> 1 -> 1
     * -2 1 4 -> -8 1 32
     * N2
     * -2 1 -> -0.5 -> -0.125
     * N3
     * -0.5 1 -> 0.25 -> 0.015625
     * N4
     * -0.5 0.25 -> -0.125 -> -0.001953125
     */
    @Test
    public void correctData_solutionFound() throws InvalidExpressionException,
            MethodInapplicableException {
        Function f = ExpressionParser.parse("x^3");
        Interval i = new Interval(-2, 4);
        double precision = 0.01;
        double delta = 0.001; // to compare double values

        Calculation res = BisectionMethod.compute(f, i, precision);

        Assert.assertNotNull(res.getResult());
        Assert.assertEquals(4, res.getIterations());
        Assert.assertEquals(4, res.getTabulation().size());

        List<Point> expected = Arrays.asList(
                new Point(1, 1),
                new Point(-0.5, -0.125),
                new Point(0.25, 0.015625),
                new Point(-0.125, -0.001953125));

        for (int j = 0; j < res.getTabulation().size(); j++) {
            Point expectedPoint = expected.get(j), actualPoint = res.getTabulation().get(j);
            Assert.assertEquals(expectedPoint.getX(), actualPoint.getX(), delta);
            Assert.assertEquals(expectedPoint.getY(), actualPoint.getY(), delta);
        }
    }

    @Test
    public void conditionsNotMet_exceptionIsThrown() throws InvalidExpressionException {
        Function f = ExpressionParser.parse("x^2 + 1");
        Interval i = new Interval(-2, 4);
        double precision = 0.01;

        Assert.assertThrows(MethodInapplicableException.class,
                () -> BisectionMethod.compute(f, i, precision));

    }
}