package com.example.equationcalculator;

import static com.example.equationcalculator.utils.Constants.EQUATION;
import static com.example.equationcalculator.utils.Constants.INTERVAL_END;
import static com.example.equationcalculator.utils.Constants.INTERVAL_START;
import static com.example.equationcalculator.utils.Constants.PRECISION;
import static com.example.equationcalculator.utils.Constants.SELECTED_METHOD;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;
import com.example.equationcalculator.exception.InvalidExpressionException;
import com.example.equationcalculator.exception.MethodInapplicableException;
import com.example.equationcalculator.math.dto.Calculation;
import com.example.equationcalculator.math.method.MethodType;
import com.example.equationcalculator.math.parser.ExpressionParser;
import com.example.equationcalculator.utils.CalculationUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.mariuszgromada.math.mxparser.Function;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ShowResultsFragment extends Fragment {

    private ProgressBar progressBar;
    private XYPlot plot;
    private TextView xText;
    private TextView yText;
    private TextView functionText;
    private TextView precisionText;
    private LinearLayout resultsLayout;
    private final static double STEP = 0.1;
    private final static int RANGE = 20;
    private ObjectMapper objectMapper = new ObjectMapper();

    public ShowResultsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new CalculationTask(getContext(), this).execute(getActivity().getIntent());
    }

    public void onTaskFinished(String fileName) {
        Calculation calculation = readFile(fileName);
        if (calculation == null || calculation.getResult() == null) {
            progressBar.setVisibility(View.INVISIBLE);
            handleError("Дані розрахунків недоступні");
            return;
        }
        if (calculation.getInapplableReason() != null) {
            progressBar.setVisibility(View.INVISIBLE);
            handleError(calculation.getInapplableReason());
            return;
        }
        drawCalculationResult(calculation);
    }

    private void drawCalculationResult(Calculation calculation) {
        // draw results
        double x = calculation.getResult().getX(), y = calculation.getResult().getY();
        int methodIdx = Integer.parseInt(getActivity().getIntent().getStringExtra(SELECTED_METHOD));
        double start = Double.parseDouble(getActivity().getIntent().getStringExtra(INTERVAL_START));
        double end = Double.parseDouble(getActivity().getIntent().getStringExtra(INTERVAL_END));
        Function f = extractFunction();
        if (f == null) return;

        double startPlot, endPlot;
        if (methodIdx == MethodType.NEWTON.getNum()) {
            startPlot = x - RANGE;
            endPlot = x + RANGE;
        } else {
            startPlot = start - 1;
            endPlot = end + 1;
        }

        List<Number> xSeries = new ArrayList<>();
        List<Number> ySeries = new ArrayList<>();

        for (double i = startPlot; i < endPlot; i += STEP) {
            xSeries.add(i);
            Double fx = calculate(f, i);
            if (fx == null) return;
            ySeries.add(fx);
        }

        XYSeries series = new SimpleXYSeries(xSeries, ySeries, "Графік функції");

        LineAndPointFormatter series1Format = new LineAndPointFormatter(getContext(),
                R.xml.line_point_formatter_with_labels);
//        series1Format.setInterpolationParams(new CatmullRomInterpolator.Params(
//                10, CatmullRomInterpolator.Type.Centripetal));
        plot.addSeries(series, series1Format);
        plot.setVisibility(View.VISIBLE);
        resultsLayout.setVisibility(View.VISIBLE);
        functionText.setText(f.getFunctionExpressionString());
        precisionText.setText(getActivity().getIntent().getStringExtra(PRECISION));
        xText.setText(String.valueOf(x));
        yText.setText(String.valueOf(y));
        progressBar.setVisibility(View.INVISIBLE);
    }

    private Double calculate(Function f, double x) {
        try {
            return CalculationUtils.calculateWithCheck(f, x);
        } catch (MethodInapplicableException e) {
            handleError(e.getMessage());
            return null;
        }
    }

    private Function extractFunction() {
        try {
            return ExpressionParser.parse(getActivity().getIntent().getStringExtra(EQUATION));
        } catch (InvalidExpressionException e) {
            handleError("Неможливо відобразити функцію");
            return null;
        }
    }

    private Calculation readFile(String path) {
        File file = getContext().getFileStreamPath(path);
        try {
            Calculation calculation = objectMapper.readValue(file, Calculation.class);
            return calculation;
        } catch (IOException e) {
            handleError("Неможливо прочитати файл з результатами обрахунків");
            return null;
        }
    }

    public void handleError(String message) {
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(getContext());
        dlgAlert.setMessage(message);
        dlgAlert.setTitle("Виникла помилка");
        dlgAlert.setPositiveButton("Назад",
                (dialog, which) -> startActivity(new Intent(getActivity(), MainActivity.class)));
        dlgAlert.show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_show_results, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progressBar = view.findViewById(R.id.calculation_progress_bar);
        progressBar.setVisibility(View.VISIBLE);
        plot = (XYPlot) view.findViewById(R.id.plot);
        xText = view.findViewById(R.id.xText);
        yText = view.findViewById(R.id.yText);
        precisionText = view.findViewById(R.id.precisionText);
        functionText = view.findViewById(R.id.functionText);
        resultsLayout = view.findViewById(R.id.resultsLayout);
    }
}