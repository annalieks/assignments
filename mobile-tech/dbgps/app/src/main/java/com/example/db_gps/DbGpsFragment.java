package com.example.db_gps;

import static android.provider.BaseColumns._ID;
import static androidx.core.content.ContextCompat.checkSelfPermission;
import static com.example.db_gps.db.DatabaseContract.SQL_CREATE_ENTRIES;
import static com.example.db_gps.db.DatabaseContract.SQL_DELETE_ENTRIES;
import static com.example.db_gps.db.DatabaseContract.StudentEntry.COLUMN_FULL_NAME;
import static com.example.db_gps.db.DatabaseContract.StudentEntry.COLUMN_MARK1;
import static com.example.db_gps.db.DatabaseContract.StudentEntry.COLUMN_MARK2;
import static com.example.db_gps.db.DatabaseContract.StudentEntry.TABLE_NAME;

import android.Manifest;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import com.example.db_gps.databinding.FragmentDbGpsBinding;
import com.example.db_gps.db.DatabaseContract;
import com.example.db_gps.db.StudentDbHelper;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

public class DbGpsFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor>, OnMapReadyCallback {

    private FragmentDbGpsBinding binding;
    private StudentDbHelper helper;

    private final ArrayList<String> allStudents = new ArrayList<>();
    private final ArrayList<String> queryStudents = new ArrayList<>();
    private final ArrayList<String> contacts = new ArrayList<>();
    private ArrayAdapter<String> allStudentsAdapter;
    private ArrayAdapter<String> queryStudentsAdapter;
    private ArrayAdapter<String> contactsAdapter;
    private final static String[] FROM_COLUMNS = {
            ContactsContract.Contacts.DISPLAY_NAME_PRIMARY
    };
    private final static int[] TO_IDS = {
            android.R.id.text1
    };
    long contactId;
    String contactKey;
    Uri contactUri;
    private SimpleCursorAdapter cursorAdapter;
    private FusedLocationProviderClient fusedLocationClient;

    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";
    private static final String[] CONTACT_PROJECTION = {
            ContactsContract.Contacts._ID,
            ContactsContract.Contacts.LOOKUP_KEY,
            ContactsContract.Contacts.DISPLAY_NAME_PRIMARY,
    };

    private static final int CONTACT_ID_INDEX = 0;
    private static final int CONTACT_KEY_INDEX = 1;

    private static final String CONTACT_SELECTION =
            ContactsContract.Contacts.DISPLAY_NAME_PRIMARY + " LIKE ?";

    private final String[] selectionArgs = {"Іван%"};

    private ListView allStudentsList;
    private ListView queryStudentsList;
    private ListView contactsList;
    private EditText studentName;
    private EditText mark1;
    private EditText mark2;
    private TextView studentsPercent;
    private TextView contactAddress;
    private MapView mMapView;
    private boolean locationPermissionGranted;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LoaderManager.getInstance(this).initLoader(0, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle args) {
        return new CursorLoader(
                getActivity(),
                ContactsContract.Contacts.CONTENT_URI,
                CONTACT_PROJECTION,
                CONTACT_SELECTION,
                selectionArgs,
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        cursorAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        cursorAdapter.swapCursor(null);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle);
        }

        mMapView.onSaveInstanceState(mapViewBundle);
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        mMapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mMapView.onStop();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        map.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
//        fusedLocationClient.getLastLocation()
//                .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
//                    @Override
//                    public void onSuccess(Location location) {
//                        if (location != null) {
//                            map.setLoca
//                        }
//                    }
//                });
        map.setMyLocationEnabled(true);
        map.getUiSettings().setMyLocationButtonEnabled(true);
    }

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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(getContext(), Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_CONTACTS}, 100);
        }
        if (checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
        }
        helper = new StudentDbHelper(getContext());
        initLists(view);
        studentName = view.findViewById(R.id.studentName);
        mark1 = view.findViewById(R.id.mark1);
        mark2 = view.findViewById(R.id.mark2);
        studentsPercent = view.findViewById(R.id.studentsPercent);
        contactAddress = view.findViewById(R.id.contactAddress);

        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        mMapView = view.findViewById(R.id.map);
        mMapView.onCreate(mapViewBundle);
        mMapView.getMapAsync(this);


        Button addStudentButton = view.findViewById(R.id.addStudentButton);
        addStudentButton.setOnClickListener(new AddStudentListener());
        Button cleanDbButton = view.findViewById(R.id.cleanDbButton);
        cleanDbButton.setOnClickListener(new CleanDbListener());
        Button queryStudentsByMarkBtn = view.findViewById(R.id.queryStudentsByMarkBtn);
        queryStudentsByMarkBtn.setOnClickListener(new QueryStudentsListener());

        fetchStudents();
    }

    private class QueryStudentsListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            queryStudents();
        }
    }

    private class CleanDbListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            helper.getDb().execSQL(SQL_DELETE_ENTRIES);
            helper.getDb().execSQL(SQL_CREATE_ENTRIES);
            allStudentsAdapter.clear();
        }
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
            studentName.setText("");
            mark1.setText("");
            mark2.setText("");
        }
    }

    private class ListViewTouchListener implements ListView.OnTouchListener {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            int action = event.getAction();
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    break;

                case MotionEvent.ACTION_UP:
                    v.getParent().requestDisallowInterceptTouchEvent(false);
                    break;
            }
            v.onTouchEvent(event);
            return true;
        }
    }

    private class ContactItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(
                AdapterView<?> parent, View item, int position, long rowID) {
            Cursor cursor = ((SimpleCursorAdapter) parent.getAdapter()).getCursor();
            cursor.moveToPosition(position);
            contactId = cursor.getLong(CONTACT_ID_INDEX);
            contactKey = cursor.getString(CONTACT_KEY_INDEX);
            Cursor addressCursor = getActivity().getContentResolver().query(
                    ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_URI,
                    null, ContactsContract.CommonDataKinds.StructuredPostal.CONTACT_ID + " = ?",
                    new String[]{String.valueOf(contactId)}, null);
            while (addressCursor.moveToNext()) {
                int columnIndex = addressCursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.FORMATTED_ADDRESS);
                String foundAddress = addressCursor.getString(columnIndex);
                contactAddress.setText(foundAddress);
            }
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

        readToAdapter(allStudentsAdapter, cursor);
    }

    private long readToAdapter(ArrayAdapter<String> adapter, Cursor cursor) {
        List<String> students = new ArrayList<>();
        long i = 0;
        while (cursor.moveToNext()) {
            i++;
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(_ID));
            String fullName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FULL_NAME));
            int mark1 = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_MARK1));
            int mark2 = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_MARK2));
            students.add(id + ", " + fullName + ", " + mark1 + ", " + mark2);
        }
        adapter.clear();
        adapter.addAll(students);
        cursor.close();
        return i;
    }

    private void queryStudents() {
        String[] projection = {
                BaseColumns._ID,
                DatabaseContract.StudentEntry.COLUMN_FULL_NAME,
                COLUMN_MARK1,
                COLUMN_MARK2
        };
        Cursor cursor = helper.getDb().query(TABLE_NAME,
                projection, "(mark1 + mark2) / 2 > 60",
                new String[]{},
                null, null, null
        );

        long count = DatabaseUtils.queryNumEntries(helper.getDb(), TABLE_NAME);
        long numOfRecords = readToAdapter(queryStudentsAdapter, cursor);
        long percent = count == 0 ? 0 : numOfRecords * 100 / count;
        studentsPercent.setText(String.valueOf(percent));
    }

    private void initLists(View view) {
        allStudentsList = view.findViewById(R.id.all_students);
        queryStudentsList = view.findViewById(R.id.query_students);
        contactsList = view.findViewById(R.id.contacts);

        allStudentsAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_1, allStudents);
        queryStudentsAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_1, queryStudents);
        cursorAdapter = new SimpleCursorAdapter(
                getActivity(),
                R.layout.contacts_list_item,
                null,
                FROM_COLUMNS, TO_IDS,
                0);
        contactsList.setAdapter(cursorAdapter);
        contactsList.setOnTouchListener(new ListViewTouchListener());
        contactsList.setOnItemClickListener(new ContactItemClickListener());

        allStudentsList.setAdapter(allStudentsAdapter);
        allStudentsList.setOnTouchListener(new ListViewTouchListener());
        queryStudentsList.setAdapter(queryStudentsAdapter);
        queryStudentsList.setOnTouchListener(new ListViewTouchListener());
        contactsList.setOnTouchListener(new ListViewTouchListener());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mMapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

}