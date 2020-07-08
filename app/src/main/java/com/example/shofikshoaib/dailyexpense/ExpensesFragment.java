package com.example.shofikshoaib.dailyexpense;


import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ExpensesFragment extends Fragment {

    private String selectFromDate, selecteToDate;
    private long fromDateMilis, toDateMIlis;
    private String expenseType;
    private TextView fromDate, toDate;
    private Spinner spinner;
    private RecyclerView recyclerView;
    private List<Expense> expenseList = new ArrayList<>();
    private ExpenseAdapter expenseAdapter;
    private ExpenseDatabaseHelper expenseDatabaseHelper;

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat monthFormat = new SimpleDateFormat("yyyy-MM");

    public ExpensesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_expenses, container, false);

        init(view);
        setDefaultDate();
        fromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCalender(fromDate);
            }
        });
        toDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCalender(toDate);
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                expenseList.clear();
                expenseAdapter.notifyDataSetChanged();
                getAllExpenseInfo();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
       // expenseAdapter.notifyDataSetChanged();
        getAllExpenseInfo();


        FloatingActionButton floatingActionButton = view.findViewById(R.id.idAddExpense);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), AddExpensesActivity.class);
                startActivity(intent);

            }
        });
        return view;
    }

    private void getAllExpenseInfo() {

        selecteToDate = toDate.getText().toString();
        selectFromDate = fromDate.getText().toString();
        expenseType = spinner.getSelectedItem().toString();
        fromDateMilis = convertDatetoMilis(selectFromDate);
        toDateMIlis = convertDatetoMilis(selecteToDate);
        Cursor cursor = expenseDatabaseHelper.showdata(fromDateMilis, toDateMIlis, expenseType);

        while (cursor.moveToNext()) {

            Integer id = cursor.getInt(cursor.getColumnIndex(expenseDatabaseHelper.COL_ID));
            String expenseType = cursor.getString(cursor.getColumnIndex(expenseDatabaseHelper.COL_TYPE));
            String expenseAmount = cursor.getString(cursor.getColumnIndex(expenseDatabaseHelper.COL_AMOUNT));
            String expenseDate = cursor.getString(cursor.getColumnIndex(expenseDatabaseHelper.COL_DATE));
            String expenseTime = cursor.getString(cursor.getColumnIndex(expenseDatabaseHelper.COL_TIME));
            String expenseDocument = cursor.getString(cursor.getColumnIndex(expenseDatabaseHelper.COL_DOCUMENT));

            expenseList.add(new Expense(id, expenseType, expenseAmount, expenseDate, expenseTime, expenseDocument));
            expenseAdapter.notifyDataSetChanged();

        }
    }

    private long convertDatetoMilis(String selectedDate) {
        Date date = null;

        try {
            date = dateFormat.parse(selectedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime();
    }

    private void setDefaultDate() {
        Calendar calendar = Calendar.getInstance();
        toDate.setText(dateFormat.format(calendar.getTime()));
        String monthYear = monthFormat.format(calendar.getTime());
        fromDate.setText(monthYear + "-01");
    }

    private void init(View view) {

        recyclerView = view.findViewById(R.id.idRecyclearView);
        expenseDatabaseHelper = new ExpenseDatabaseHelper(getContext());
        expenseAdapter = new ExpenseAdapter(getContext(), expenseList, expenseDatabaseHelper);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(expenseAdapter);

        fromDate = view.findViewById(R.id.fromDateTV);
        toDate = view.findViewById(R.id.toDateTV);
        spinner = view.findViewById(R.id.expenseTypeSpinner);
    }

    private void openCalender(final TextView textView) {
        DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String currentDate = year + "-" + month + "-" + day;

                Date date = null;

                try {
                    date = dateFormat.parse(currentDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                textView.setText(currentDate);
                expenseList.clear();
                expenseAdapter.notifyDataSetChanged();
                getAllExpenseInfo();

            }
        };

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), onDateSetListener, year, month, day);
        datePickerDialog.show();
    }

}
