package com.example.equationcalculator.math.method;

import static com.example.equationcalculator.utils.CalculationUtils.calculateWithCheck;

import com.example.equationcalculator.exception.MethodInapplicableException;
import com.example.equationcalculator.utils.Constants;
import com.example.equationcalculator.math.dto.Calculation;
import com.example.equationcalculator.math.dto.Point;

import org.mariuszgromada.math.mxparser.Argument;
import org.mariuszgromada.math.mxparser.Expression;
import org.mariuszgromada.math.mxparser.Function;

import java.util.ArrayList;
import java.util.List;

public class NewtonMethod {

    public static Calculation compute(Function f, double x1, double precision) throws MethodInapplicableException {
        String arg = f.getArgument(0).getArgumentName();
        Argument x = new Argument(String.format(arg + "=%s", x1));
        Expression ex = new Expression("der(" +
                f.getFunctionExpressionString() + "," + arg + ")", x); // derivative

        int iterations = 1;
        List<Point> tabulation = new ArrayList<>();

        while (iterations <= Constants.MAX_ITERATIONS) {
            double fx1 = calculateWithCheck(f, x1);
            double derX1 = calculateWithCheck(ex);
            tabulation.add(new Point(x1, fx1));
            double approxRoot = x1 - (fx1 / derX1);

            //check if current root is equal to the previous root then the result is found
            if (Math.abs(x1 - approxRoot) <= precision) {
                double fRes = calculateWithCheck(f, approxRoot);
                Point result = new Point(approxRoot, fRes);
                tabulation.add(result);
                return new Calculation(iterations, tabulation, result);
            }

            iterations++;
            x1 = approxRoot;
        }
        return new Calculation(Constants.MAX_ITERATIONS, tabulation, null);
    }

}
