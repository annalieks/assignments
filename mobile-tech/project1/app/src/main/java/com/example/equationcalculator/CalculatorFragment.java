package com.example.equationcalculator;

import static com.example.equationcalculator.utils.Constants.EQUATION;
import static com.example.equationcalculator.utils.Constants.SELECTED_METHOD;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.equationcalculator.databinding.FragmentCalculatorBinding;
import com.example.equationcalculator.exception.InvalidExpressionException;
import com.example.equationcalculator.math.parser.ExpressionParser;
import com.google.android.material.textfield.TextInputEditText;

import org.mariuszgromada.math.mxparser.Function;

public class CalculatorFragment extends Fragment implements View.OnClickListener {

    private FragmentCalculatorBinding binding;
    private Button calculateButton;
    private RadioGroup methodsRadioGroup;
    private TextInputEditText equationInput;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentCalculatorBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        calculateButton = view.findViewById(R.id.calculateBtn);
        methodsRadioGroup = view.findViewById(R.id.methods);
        equationInput = view.findViewById(R.id.equation);

        calculateButton.setOnClickListener(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onClick(View view) {
        int method = methodsRadioGroup.getCheckedRadioButtonId();
        RadioButton radioButton = methodsRadioGroup.findViewById(method);
        Editable equationText = equationInput.getText();
        if (TextUtils.isEmpty(equationText)) {
            Toast errorToast = Toast.makeText(getActivity(),
                    "Please, enter the equation", Toast.LENGTH_SHORT);
            errorToast.show();
            return;
        }
        String equationStr = equationText.toString();
        try {
            ExpressionParser.parse(equationStr);
        } catch (InvalidExpressionException e) {
            Toast errorToast = Toast.makeText(getActivity(),
                    "Please, enter a valid expression", Toast.LENGTH_SHORT);
            errorToast.show();
            return;
        }
        Intent i = new Intent(getActivity(), ShowResultsActivity.class);
        i.putExtra(SELECTED_METHOD, radioButton.getId());
        i.putExtra(EQUATION, equationStr);
        startActivity(i);
    }
}