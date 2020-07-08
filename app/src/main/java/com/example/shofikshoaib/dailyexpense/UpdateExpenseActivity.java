package com.example.shofikshoaib.dailyexpense;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.provider.MediaStore;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class UpdateExpenseActivity extends AppCompatActivity {

    private EditText datePicker, amount;
    private EditText timePicker;
    private ImageView imageView;
    private String encodedImage;
    private ExpenseDatabaseHelper expenseDatabaseHelper;
    private Button clearBtn, saveBtn;
    private Spinner spinner;
    private long miliSecond, dateInMiliSecond;
    private String expenseType, expenseDate, expenseTime, encodeImage, status;
    private String expenseAmount;
    private ExpensesFragment expensesFragment;
    private String atype,aAmount,aDate,aTime;
private Integer updateId;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm aa");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_expense);



        datePicker = findViewById(R.id.dateET);
        timePicker = findViewById(R.id.timeET);
        imageView = findViewById(R.id.imageview);
        saveBtn = findViewById(R.id.saveBtn);
        clearBtn = findViewById(R.id.clearBtn);
        expenseDatabaseHelper = new ExpenseDatabaseHelper(this);
        spinner = findViewById(R.id.expenseTypeSpinner);
        amount = findViewById(R.id.amountET);
        clearBtn = findViewById(R.id.clearBtn);

        Bundle bundle = getIntent().getExtras();
        updateId = bundle.getInt("expense_id");
         atype = (String) bundle.get("expense_name");
         aAmount = (String) bundle.get("expense_amount");
         aDate = (String) bundle.get("expense_date");
         aTime = (String) bundle.get("expense_time");

        amount.setText(aAmount);
        datePicker.setText(aDate);
        timePicker.setText(aTime);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.expense_arrays, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        if (atype != null) {
            int spinnerPosition = adapter.getPosition(atype);
            spinner.setSelection(spinnerPosition);
        }

//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
//            return;
//        }

        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCalender();
            }
        });

        timePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openClock();
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Spinner mySpinner = (Spinner) findViewById(R.id.expenseTypeSpinner);
                expenseType = mySpinner.getSelectedItem().toString();
                expenseAmount = amount.getText().toString();
                expenseDate = datePicker.getText().toString();
                expenseTime = timePicker.getText().toString();
                encodeImage = encodedImage;
                status = "Activated";
                Date date = null;

                try {
                    date = dateFormat.parse(expenseDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                dateInMiliSecond = date.getTime();
              //  dateInMiliSecond = miliSecond;
                boolean errorFound = false;
                if (TextUtils.isEmpty(expenseAmount)) {
                    Toast toast = Toast.makeText(UpdateExpenseActivity.this, "Enter Amount", Toast.LENGTH_LONG);
                    View view1 = toast.getView();
                    TextView text = (TextView) view1.findViewById(android.R.id.message);
                    view1.getBackground().setColorFilter(Color.parseColor("#FF0000"), PorterDuff.Mode.SRC_IN);
                    text.setTextColor(Color.parseColor("#FFFFFF"));
                    toast.show();
                    errorFound = true;
                }
                if (TextUtils.isEmpty(expenseDate)) {
                    Toast toast = Toast.makeText(UpdateExpenseActivity.this, "Select Date", Toast.LENGTH_LONG);
                    View view1 = toast.getView();
                    TextView text = (TextView) view1.findViewById(android.R.id.message);
                    view1.getBackground().setColorFilter(Color.parseColor("#FF0000"), PorterDuff.Mode.SRC_IN);
                    text.setTextColor(Color.parseColor("#FFFFFF"));
                    toast.show();
                    errorFound = true;
                }
                if (errorFound == false) {
                    long id = expenseDatabaseHelper.updateData(expenseType, expenseAmount, expenseDate, expenseTime, status, encodeImage, dateInMiliSecond, updateId);
                    if (id > 0) {
                        amount.setText("");
                        datePicker.setText("");
                        timePicker.setText("");
                        imageView.setImageResource(R.drawable.noimageavailable);
                        onBackPressed();
                        Toast toast = Toast.makeText(UpdateExpenseActivity.this, "Expense Updated Successfully", Toast.LENGTH_SHORT);
                        View view1 = toast.getView();
                        TextView text = (TextView) view1.findViewById(android.R.id.message);
                        view1.getBackground().setColorFilter(Color.parseColor("#009900"), PorterDuff.Mode.SRC_IN);
                        text.setTextColor(Color.parseColor("#FFFFFF"));
                        toast.show();
                    } else {
                        Toast toast = Toast.makeText(UpdateExpenseActivity.this, "Failed! Try Again", Toast.LENGTH_LONG);
                        View view1 = toast.getView();
                        TextView text = (TextView) view1.findViewById(android.R.id.message);
                        view1.getBackground().setColorFilter(Color.parseColor("#FF0000"), PorterDuff.Mode.SRC_IN);
                        text.setTextColor(Color.parseColor("#FFFFFF"));
                        toast.show();
                    }
                }
            }
        });

        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               onBackPressed();
            }
        });
    }


    public void onBackPressed()
    {
        // code here to show dialog
        super.onBackPressed();  // optional depending on your needs
    }
    /* File Selection from Camera or Gallery*/
    public void OpenCamera(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 0);
    }

    public void OpenGallery(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 0) {
                Bundle bundle = data.getExtras();
                final Bitmap bitmap = (Bitmap) bundle.get("data");
                encodedImage = encodeToBase64(bitmap, Bitmap.CompressFormat.JPEG, 100);
                imageView.setImageBitmap(bitmap);
            } else if (requestCode == 1) {
                Uri uri = data.getData();
                Bitmap bitmap2 = null;
                try {
                    bitmap2 = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                encodedImage = encodeToBase64(bitmap2, Bitmap.CompressFormat.JPEG, 100);
                imageView.setImageBitmap(bitmap2);
            }
        }
    }

    public static String encodeToBase64(Bitmap image, Bitmap.CompressFormat compressFormat, int quality) {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        image.compress(compressFormat, quality, byteArrayOS);
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
    }

    public static Bitmap decodeBase64(String input) {
        byte[] decodedBytes = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }


    /* Time Picker Starts*/
    private void openClock() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.time_picker, null);

        Button done = view.findViewById(R.id.timePickerBtn);
        final TimePicker timePicker = view.findViewById(R.id.timePickerTP);

        builder.setView(view);
        final Dialog dialog = builder.create();
        dialog.show();

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  @SuppressLint({"NewApi", "LocalSuppress"}) int hour = timePicker.getHour();
                //  @SuppressLint({"NewApi", "LocalSuppress"}) int minute = timePicker.getMinute();
                int hour = 0;
                int minute = 0;
                int currentApiVersion = android.os.Build.VERSION.SDK_INT;
                if (currentApiVersion > android.os.Build.VERSION_CODES.LOLLIPOP_MR1){
                    hour = timePicker.getHour();
                    minute = timePicker.getMinute();
                } else {
                    hour = timePicker.getCurrentHour();
                    minute = timePicker.getCurrentMinute();
                }


                Time time = new Time(hour, minute, 0);
                ((EditText) findViewById(R.id.timeET)).setText(timeFormat.format(time));
                dialog.dismiss();
            }
        });
    }


    /*Date Picker Starts*/
    private void openCalender() {
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
                miliSecond = date.getTime();
                ((EditText) findViewById(R.id.dateET)).setText(currentDate);
            }
        };

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, onDateSetListener, year, month, day);
        datePickerDialog.show();
    }
}
