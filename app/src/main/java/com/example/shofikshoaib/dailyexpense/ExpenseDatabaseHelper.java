package com.example.shofikshoaib.dailyexpense;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;

public class ExpenseDatabaseHelper extends SQLiteOpenHelper {

    public static String DATABASE_NAME = "DailyExpense.db";
    public static String TABLE_NAME = "Expense";
    public static String COL_ID = "Id";
    public static String COL_TYPE = "Type";
    public static String COL_AMOUNT = "Amount";
    public static String COL_DATE = "Date";
    public static String COL_TIME = "Time";
    public static String COL_STATUS = "Status";
    public static String COL_DOCUMENT = "Document";
    public static String COL_MILISECOND = "Milisecond";


    private String CREATE_TABLE = "create table " + TABLE_NAME + " (Id INTEGER PRIMARY KEY AUTOINCREMENT,Type TEXT, Amount TEXT, Date TEXT, Time TEXT,Status TEXT ,Document TEXT, Milisecond TEXT )";
    private static int VERSION = 1;

    public ExpenseDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public long insertData(String expenseType, String amount, String expenseDate, String expenseTime, String status, String encodedImage, long dateInMilisecond) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_AMOUNT, amount);
        contentValues.put(COL_TYPE, expenseType);
        contentValues.put(COL_DATE, expenseDate);
        contentValues.put(COL_TIME, expenseTime);
        contentValues.put(COL_STATUS, status);
        contentValues.put(COL_DOCUMENT, encodedImage);
        contentValues.put(COL_MILISECOND, dateInMilisecond);

        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        long id = sqLiteDatabase.insert(TABLE_NAME, null, contentValues);
        sqLiteDatabase.close();
        return id;
    }

    public Cursor showdata(long fromDate, long toDate, String expenseType) {

        String show_all = null;
        if(expenseType.equals("All Expenses")){
            show_all = "select * from Expense where Milisecond between " + fromDate + " and " + toDate;
        }
        else{
            show_all = "select * from Expense where Type = '" + expenseType + "' AND Milisecond between " + fromDate + " and " + toDate;
        }
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(show_all, null);
        return cursor;
    }

    public double getTotalAMount(long fromDate, long toDate, String expenseType) {
        double total = 0;
        String show_all = null;
        if (expenseType.equals("All Expenses")) {
            show_all = "select SUM(Amount) from Expense where Milisecond between " + fromDate + " and " + toDate;
        }
        else{
            show_all = "select SUM(Amount) from Expense where Type = '" + expenseType + "' AND Milisecond between " + fromDate + " and " + toDate;

        }
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(show_all, null);
        if (cursor.moveToFirst()) {
            total = cursor.getDouble(0);
        }

        return total;
    }


    public void deleteData(Integer id) {
        getWritableDatabase().delete(TABLE_NAME,"ID=?", new String[]{String.valueOf(id)});
    }

    public long updateData(String expenseType, String expenseAmount, String expenseDate, String expenseTime, String status, String encodeImage, long dateInMiliSecond, Integer updateId) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_AMOUNT, expenseAmount);
        contentValues.put(COL_TYPE, expenseType);
        contentValues.put(COL_DATE, expenseDate);
        contentValues.put(COL_TIME, expenseTime);
        contentValues.put(COL_STATUS, status);
        contentValues.put(COL_DOCUMENT, encodeImage);
        contentValues.put(COL_MILISECOND, dateInMiliSecond);
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        long id = sqLiteDatabase.update(TABLE_NAME,contentValues,"Id="+updateId,null);
        //long id = sqLiteDatabase.insert(TABLE_NAME, null, contentValues);
        sqLiteDatabase.close();
        return id;
    }
}
