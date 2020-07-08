package com.example.shofikshoaib.dailyexpense;


import android.app.DatePickerDialog;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardFragment extends Fragment {

    private TextView fromDate, toDate, totalBillAmount,type;
    private String selectFromDate, selecteToDate;
    private long fromDateMilis, toDateMIlis;
    private Spinner spinner;
    private String expenseType;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat monthFormat = new SimpleDateFormat("yyyy-MM");

    private ExpenseDatabaseHelper expenseDatabaseHelper;


    public DashboardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        init(view);
        setDefaultDate();
        totalAmountCalculation();

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
                totalAmountCalculation();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        return view;
    }

    private void totalAmountCalculation() {
        selecteToDate = toDate.getText().toString();
        selectFromDate = fromDate.getText().toString();
        expenseType = spinner.getSelectedItem().toString();
        fromDateMilis = convertDatetoMilis(selectFromDate);
        toDateMIlis = convertDatetoMilis(selecteToDate);
        double totalAmount = expenseDatabaseHelper.getTotalAMount(fromDateMilis, toDateMIlis, expenseType);
        String total2 = String.valueOf(totalAmount);
        type.setText(expenseType);
        totalBillAmount.setText(total2);
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

        fromDate = view.findViewById(R.id.fromDateTV);
        toDate = view.findViewById(R.id.toDateTV);
        spinner = view.findViewById(R.id.expenseTypeSpinner);
        expenseDatabaseHelper = new ExpenseDatabaseHelper(getContext());
        totalBillAmount = view.findViewById(R.id.totalAmountTV);
        type = view.findViewById(R.id.typeTV);
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
                totalAmountCalculation();
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
