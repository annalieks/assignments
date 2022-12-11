package com.example.equationcalculator;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

public class Project1HelpFragment extends Fragment {
    public Project1HelpFragment() {
        // Required empty public constructor
    }

    public static Project1HelpFragment newInstance(String param1, String param2) {
        Project1HelpFragment fragment = new Project1HelpFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_project1_help, container, false);
    }
}