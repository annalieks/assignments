package com.example.equationcalculator;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

public class Project2HelpFragment extends Fragment {

    public Project2HelpFragment() {
        // Required empty public constructor
    }

    public static Project2HelpFragment newInstance(String param1, String param2) {
        Project2HelpFragment fragment = new Project2HelpFragment();
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
        return inflater.inflate(R.layout.fragment_project2_help, container, false);
    }
}