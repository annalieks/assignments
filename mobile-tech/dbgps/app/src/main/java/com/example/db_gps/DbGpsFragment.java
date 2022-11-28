package com.example.db_gps;

import static android.provider.BaseColumns._ID;
import static com.example.db_gps.db.DatabaseContract.StudentEntry.COLUMN_FULL_NAME;
import static com.example.db_gps.db.DatabaseContract.StudentEntry.COLUMN_MARK1;
import static com.example.db_gps.db.DatabaseContract.StudentEntry.COLUMN_MARK2;
import static com.example.db_gps.db.DatabaseContract.StudentEntry.TABLE_NAME;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.text.Editable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.db_gps.databinding.FragmentDbGpsBinding;
import com.example.db_gps.db.DatabaseContract;
import com.example.db_gps.db.StudentDbHelper;

import java.util.ArrayList;
import java.util.List;

public class DbGpsFragment extends Fragment {

    private FragmentDbGpsBinding binding;
    private StudentDbHelper helper;

    private final ArrayList<String> allStudents = new ArrayList<>();
    private final ArrayList<String> queryStudents = new ArrayList<>();
    private ArrayAdapter<String> allStudentsAdapter;
    private ArrayAdapter<String> queryStudentsAdapter;

    private ListView allStudentsList;
    private ListView queryStudentsList;
    private EditText studentName;
    private EditText mark1;
    private EditText mark2;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentDbGpsBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        helper = new StudentDbHelper(getContext());
        initLists(view);
        studentName = view.findViewById(R.id.studentName);
        mark1 = view.findViewById(R.id.mark1);
        mark2 = view.findViewById(R.id.mark2);
        Button addStudentButton = view.findViewById(R.id.addStudentButton);
        addStudentButton.setOnClickListener(new AddStudentListener());

        fetchStudents();
    }

    private class AddStudentListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            Editable fullName = studentName.getText();
            Editable mark1Text = mark1.getText();
            Editable mark2Text = mark2.getText();

            if (TextUtils.isEmpty(fullName) || TextUtils.isEmpty(mark1Text)
                    || TextUtils.isEmpty(mark2Text)) {
                return;
            }

            ContentValues values = new ContentValues();
            values.put(DatabaseContract.StudentEntry.COLUMN_FULL_NAME,
                    studentName.getText().toString());
            values.put(COLUMN_MARK1,
                    mark1.getText().toString());
            values.put(COLUMN_MARK2,
                    mark2.getText().toString());

            helper.getDb().insert(TABLE_NAME, null, values);
            fetchStudents();
        }

    }

    private void fetchStudents() {
        String[] projection = {
                BaseColumns._ID,
                DatabaseContract.StudentEntry.COLUMN_FULL_NAME,
                COLUMN_MARK1,
                COLUMN_MARK2
        };
        Cursor cursor = helper.getDb().query(TABLE_NAME, projection, null, new String[]{},
                null, null, null
        );

        List<String> students = new ArrayList<>();
        while(cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(_ID));
            String fullName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FULL_NAME));
            int mark1 = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_MARK1));
            int mark2 = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_MARK2));
            students.add(id + ", " + fullName + ", " + mark1 + ", " + mark2);
        }
        allStudentsAdapter.clear();
        allStudentsAdapter.addAll(students);
        cursor.close();
    }

    private void initLists(View view) {
        allStudentsList = view.findViewById(R.id.all_students);
        queryStudentsList = view.findViewById(R.id.query_students);

        allStudentsAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_1, allStudents);
        queryStudentsAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_1, queryStudents);

        allStudentsList.setAdapter(allStudentsAdapter);
        queryStudentsList.setAdapter(queryStudentsAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}