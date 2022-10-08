package com.example.equationcalculator.math.method;

import static com.example.equationcalculator.utils.CalculationUtils.calculateWithCheck;

import com.example.equationcalculator.exception.MethodInapplicableException;
import com.example.equationcalculator.math.dto.Calculation;
import com.example.equationcalculator.math.dto.Interval;
import com.example.equationcalculator.math.dto.Point;
import com.example.equationcalculator.utils.Constants;

import java.util.ArrayList;
import java.util.List;

import org.mariuszgromada.math.mxparser.Function;

public class SecantMethod {

    public static Calculation compute(Function f, Interval i, double precision)
            throws MethodInapplicableException {
        int iterations = 1;
        double x0 = i.getStart(), x1 = i.getEnd();
        List<Point> tabulation = new ArrayList<>();

        while (iterations <= Constants.MAX_ITERATIONS) {
            double fx0 = calculateWithCheck(f, x0), fx1 = calculateWithCheck(f, x1);
            double x2 = x1 - fx1 * ((x1 - x0) / (fx1 - fx0));
            double fx2 = calculateWithCheck(f, x2);
            double difference = Math.abs(x2 - x1);
            Point p = new Point(x2, fx2);
            tabulation.add(p);
            x0 = x1;
            x1 = x2;
            if (difference < precision) {
                return new Calculation(iterations, tabulation, p);
            }
            iterations++;
        }
        return new Calculation(Constants.MAX_ITERATIONS, tabulation, null);
    }

}

