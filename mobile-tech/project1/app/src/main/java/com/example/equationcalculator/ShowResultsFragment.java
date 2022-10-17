package com.example.equationcalculator;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.equationcalculator.math.dto.Calculation;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class ShowResultsFragment extends Fragment {

    private ProgressBar progressBar;
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

        // disable loader

        // draw results

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
        progressBar.setVisibility(View.VISIBLE
        );
    }
}