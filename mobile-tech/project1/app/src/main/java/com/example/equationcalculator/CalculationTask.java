package com.example.equationcalculator;

import static com.example.equationcalculator.utils.Constants.APPROX_ROOT;
import static com.example.equationcalculator.utils.Constants.EQUATION;
import static com.example.equationcalculator.utils.Constants.INTERVAL_END;
import static com.example.equationcalculator.utils.Constants.INTERVAL_START;
import static com.example.equationcalculator.utils.Constants.PRECISION;
import static com.example.equationcalculator.utils.Constants.SELECTED_METHOD;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;

import com.example.equationcalculator.exception.InvalidExpressionException;
import com.example.equationcalculator.exception.MethodInapplicableException;
import com.example.equationcalculator.math.dto.Calculation;
import com.example.equationcalculator.math.dto.Interval;
import com.example.equationcalculator.math.method.BisectionMethod;
import com.example.equationcalculator.math.method.MethodType;
import com.example.equationcalculator.math.method.NewtonMethod;
import com.example.equationcalculator.math.method.SecantMethod;
import com.example.equationcalculator.math.parser.ExpressionParser;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.mariuszgromada.math.mxparser.Function;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;

public class CalculationTask extends AsyncTask<Intent, Integer, String> {

    private final Context context;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ShowResultsFragment parent;

    public CalculationTask(Context context, ShowResultsFragment parent) {
        this.context = context;
        this.parent = parent;
    }

    @Override
    protected String doInBackground(Intent... intents) {
        if (intents.length != 1) {
            throw new IllegalStateException("Invalid number of params");
        }
        Intent i = intents[0];
        Calculation calculation;
        try {
            Function f = ExpressionParser.parse(i.getStringExtra(EQUATION));
            double precision = Double.parseDouble(i.getStringExtra(PRECISION));
            int methodIdx = Integer.parseInt(i.getStringExtra(SELECTED_METHOD));

            if (methodIdx == MethodType.NEWTON.getNum()) {
                double approxRoot = Double.parseDouble(i.getStringExtra(APPROX_ROOT));
                calculation = NewtonMethod.compute(f, approxRoot, precision);
            } else {
                double start = Double.parseDouble(i.getStringExtra(INTERVAL_START));
                double end = Double.parseDouble(i.getStringExtra(INTERVAL_END));
                Interval interval = new Interval(start, end);
                if (methodIdx == MethodType.BISECTION.getNum()) {
                    calculation = BisectionMethod.compute(f, interval, precision);
                } else if (methodIdx == MethodType.SECANT.getNum()) {
                    calculation = SecantMethod.compute(f, interval, precision);
                } else {
                    throw new IllegalStateException("Unknown method index");
                }
            }
        } catch (MethodInapplicableException e) {
            calculation = new Calculation(e.getMessage());
        } catch (InvalidExpressionException e) {
            throw new IllegalStateException("Cannot parse provided function");
        }

        return writeToFile(calculation);
    }

    @Override
    protected void onPostExecute(String fileName) {
        parent.onTaskFinished(fileName);
    }

    private String writeToFile(Calculation calculation) {
        try {
            String json = objectMapper.writeValueAsString(calculation);
            String fileName = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date()) + ".json";
            OutputStreamWriter outputStreamWriter =
                    new OutputStreamWriter(context.openFileOutput(fileName, Context.MODE_PRIVATE));
            outputStreamWriter.write(json);
            outputStreamWriter.close();
            return fileName;
        } catch (IOException e) {
            throw new IllegalStateException("Could not write to file");
        }
    }

}
