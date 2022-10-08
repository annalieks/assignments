package com.example.equationcalculator;

import com.example.equationcalculator.exception.InvalidExpressionException;
import com.example.equationcalculator.exception.MethodInapplicableException;
import com.example.equationcalculator.math.dto.Calculation;
import com.example.equationcalculator.math.dto.Interval;
import com.example.equationcalculator.math.dto.Point;
import com.example.equationcalculator.math.method.BisectionMethod;
import com.example.equationcalculator.math.method.NewtonMethod;
import com.example.equationcalculator.math.method.SecantMethod;
import com.example.equationcalculator.math.parser.ExpressionParser;

import org.junit.Assert;
import org.junit.Test;
import org.mariuszgromada.math.mxparser.Function;

import java.util.Arrays;
import java.util.List;

public class SecantMethodTest {

    @Test
    public void correctData_solutionFound() throws InvalidExpressionException,
            MethodInapplicableException {
        Function f = ExpressionParser.parse("x^3");
        Interval i = new Interval(-0.5, 0.2);
        double precision = 0.01;
        double delta = 0.0001; // to compare double values

        Calculation res = SecantMethod.compute(f, i, precision);

        Assert.assertNotNull(res.getResult());
        Assert.assertEquals(7, res.getIterations());
        Assert.assertEquals(7, res.getTabulation().size());

        List<Point> expected = Arrays.asList(
                new Point(0.15789, 0.0039),
                new Point(0.1171, 0.0016),
                new Point(0.088998, 0.0007),
                new Point(0.067, 0.0003),
                new Point(0.0506, 0.00013),
                new Point(0.0382, 0.00005577),
                new Point(0.028846, 0.000024));

        for (int j = 0; j < res.getTabulation().size(); j++) {
            Point expectedPoint = expected.get(j), actualPoint = res.getTabulation().get(j);
            Assert.assertEquals(expectedPoint.getX(), actualPoint.getX(), delta);
            Assert.assertEquals(expectedPoint.getY(), actualPoint.getY(), delta);
        }
    }

    @Test
    public void invalidFunction_exceptionIsThrown() throws InvalidExpressionException {
        Function f = ExpressionParser.parse("1/x");
        Interval i = new Interval(0, 1);
        double precision = 0.01;

        Assert.assertThrows(MethodInapplicableException.class,
                () -> SecantMethod.compute(f, i, precision));
    }

}