package com.example.equationcalculator.utils;

import com.example.equationcalculator.exception.MethodInapplicableException;

import org.mariuszgromada.math.mxparser.Argument;
import org.mariuszgromada.math.mxparser.Expression;
import org.mariuszgromada.math.mxparser.Function;

public class CalculationUtils {

    public static double calculateWithCheck(Function f, double x) throws MethodInapplicableException {
        double fx = f.calculate(x);
        if (Double.isNaN(fx)) {
            Argument arg = f.getArgument(0);
            throw new MethodInapplicableException("Неможливо порахувати значення функції " +
                    "f(" + arg.getArgumentName() + ") = " + f.getFunctionExpressionString() +
                    " в " + arg.getArgumentName() + "= " + x);
        }
        return fx;
    }

    public static double calculateWithCheck(Expression ex) throws MethodInapplicableException {
        double derX1 = ex.calculate();
        if (Double.isNaN(derX1)) {
            Argument arg = ex.getArgument(0);
            throw new MethodInapplicableException("Неможливо порахувати похідну " +
                    ex.getCanonicalExpressionString() + " в " +
                    arg.getArgumentName() + "= " + arg.getArgumentValue());
        }
        return derX1;
    }

}
