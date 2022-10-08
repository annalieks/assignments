package com.example.equationcalculator.math.method;

import static com.example.equationcalculator.utils.CalculationUtils.calculateWithCheck;

import com.example.equationcalculator.exception.MethodInapplicableException;
import com.example.equationcalculator.utils.Constants;
import com.example.equationcalculator.math.dto.Calculation;
import com.example.equationcalculator.math.dto.Interval;
import com.example.equationcalculator.math.dto.Point;

import org.mariuszgromada.math.mxparser.Function;

import java.util.ArrayList;
import java.util.List;

public class BisectionMethod {

    public static Calculation compute(Function f, Interval i, double precision)
            throws MethodInapplicableException {
        checkApplicable(f, i);

        int iterations = 1;
        double x1 = i.getStart(), x2 = i.getEnd();
        List<Point> tabulation = new ArrayList<>();

        while (iterations <= Constants.MAX_ITERATIONS) {
            double x3 = (x1 + x2) / 2;
            double f3 = calculateWithCheck(f, x3);
            Point p = new Point(x3, f3);
            tabulation.add(p);

            if (Math.abs(f3) <= precision) {
                return new Calculation(iterations, tabulation, p);
            }

            iterations++;
            double f1 = calculateWithCheck(f, x1);
            if (f1 * f3 < 0) {
                x2 = x3;
            } else {
                x1 = x3;
            }
        }
        return new Calculation(Constants.MAX_ITERATIONS, tabulation, null);
    }

    public static void checkApplicable(Function f, Interval i) throws MethodInapplicableException {
        if (i.getStart() > i.getEnd()) {
            throw new MethodInapplicableException("Start of the interval is greater that its end");
        }
        if (f.calculate(i.getStart()) * f.calculate(i.getEnd()) > 0) {
            throw new MethodInapplicableException("Cannot apply bisection method: either " +
                    "f(a) < 0 and f(b) > 0 or f(a) > 0 and f(b) < 0 for the interval " +
                    "[a, b] must be true");
        }

    }

}
