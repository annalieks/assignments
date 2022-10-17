package com.example.equationcalculator;

import static com.example.equationcalculator.utils.Constants.APPROX_ROOT;
import static com.example.equationcalculator.utils.Constants.EQUATION;
import static com.example.equationcalculator.utils.Constants.INTERVAL_END;
import static com.example.equationcalculator.utils.Constants.INTERVAL_START;
import static com.example.equationcalculator.utils.Constants.PRECISION;
import static com.example.equationcalculator.utils.Constants.SELECTED_METHOD;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.equationcalculator.databinding.FragmentCalculatorBinding;
import com.example.equationcalculator.exception.InvalidExpressionException;
import com.example.equationcalculator.math.method.MethodType;
import com.example.equationcalculator.math.parser.ExpressionParser;
import com.google.android.material.textfield.TextInputEditText;

public class CalculatorFragment extends Fragment implements View.OnClickListener {

    private FragmentCalculatorBinding binding;
    private RadioGroup methodsRadioGroup;
    private TextInputEditText equationInput;
    private LinearLayout intervalLayout;
    private LinearLayout rootLayout;
    private EditText rootInput;
    private EditText intervalStartInput;
    private EditText intervalEndInput;
    private EditText precisionInput;

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
        Button calculateButton = view.findViewById(R.id.calculateBtn);
        methodsRadioGroup = view.findViewById(R.id.methods);
        equationInput = view.findViewById(R.id.equation);
        intervalLayout = view.findViewById(R.id.interval_layout);
        rootLayout = view.findViewById(R.id.root_layout);
        rootInput = view.findViewById(R.id.root_input);
        intervalStartInput = view.findViewById(R.id.start_interval);
        intervalEndInput = view.findViewById(R.id.end_interval);
        precisionInput = view.findViewById(R.id.precision);

        calculateButton.setOnClickListener(this);
        methodsRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            View radioButton = group.findViewById(checkedId);
            int index = group.indexOfChild(radioButton);
            if (index == MethodType.NEWTON.getNum()) {
                intervalLayout.setVisibility(View.INVISIBLE);
                rootLayout.setVisibility(View.VISIBLE);
            } else {
                intervalLayout.setVisibility(View.VISIBLE);
                rootLayout.setVisibility(View.INVISIBLE);
            }
        });
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
        int index = methodsRadioGroup.indexOfChild(radioButton);
        Editable equationText = equationInput.getText();
        Editable precisionText = precisionInput.getText();
        if (!checkEquation(equationText)) {
            return;
        }
        if (!checkPrecision(precisionText)) {
            return;
        }
        Intent i = new Intent(getActivity(), ShowResultsActivity.class);
        if (index == MethodType.NEWTON.getNum()) {
            Editable rootText = rootInput.getText();
            if (!checkRoot(rootText)) {
                return;
            }
            i.putExtra(APPROX_ROOT, rootText.toString());
        } else {
            Editable startText = intervalStartInput.getText(),
                    endText = intervalEndInput.getText();
            if (!checkInterval(startText, endText)) {
                return;
            }
            i.putExtra(INTERVAL_START, String.valueOf(startText));
            i.putExtra(INTERVAL_END, String.valueOf(endText));
        }
        i.putExtra(SELECTED_METHOD, String.valueOf(index));
        i.putExtra(EQUATION, equationText.toString());
        i.putExtra(PRECISION, precisionText.toString());
        startActivity(i);
    }

    private boolean checkEquation(Editable equationText) {
        if (TextUtils.isEmpty(equationText)) {
            Toast errorToast = Toast.makeText(getActivity(),
                    "Введіть функцію", Toast.LENGTH_SHORT);
            errorToast.show();
            return false;
        }
        String equationStr = equationText.toString();
        try {
            ExpressionParser.parse(equationStr);
        } catch (InvalidExpressionException e) {
            Toast errorToast = Toast.makeText(getActivity(),
                    "Функція невалідна", Toast.LENGTH_SHORT);
            errorToast.show();
            return false;
        }
        return true;
    }

    private boolean checkInterval(Editable start, Editable end) {
        final String errorMsg;
        if (TextUtils.isEmpty(start)) {
            errorMsg = "Введіть початок інтервалу";
        } else if (TextUtils.isEmpty(end)) {
            errorMsg = "Введіть кінець інтервалу";
        } else {
            double startNum = Double.parseDouble(start.toString());
            double endNum = Double.parseDouble(end.toString());
            if (startNum > endNum) {
                errorMsg = "Інтервал невалідний: початок більший за кінець";
            } else {
                return true;
            }
        }
        Toast errorToast = Toast.makeText(getActivity(),
                errorMsg, Toast.LENGTH_SHORT);
        errorToast.show();
        return false;
    }

    private boolean checkRoot(Editable root) {
        if (TextUtils.isEmpty(root)) {
            Toast errorToast = Toast.makeText(getActivity(),
                    "Введіть приблизний корінь", Toast.LENGTH_SHORT);
            errorToast.show();
            return false;
        }
        return true;
    }

    private boolean checkPrecision(Editable root) {
        if (TextUtils.isEmpty(root)) {
            Toast errorToast = Toast.makeText(getActivity(),
                    "Введіть точність розрахунків", Toast.LENGTH_SHORT);
            errorToast.show();
            return false;
        }
        return true;
    }

}

