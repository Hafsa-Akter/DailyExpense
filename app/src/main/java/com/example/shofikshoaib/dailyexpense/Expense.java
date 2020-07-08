package com.example.shofikshoaib.dailyexpense;

public class Expense {
    private Integer id;
    private  String expenseType, expenseAmount, expenseDate, expenseTime, expenseDocument;

    public Expense(Integer id, String expenseType, String expenseAmount, String expenseDate, String expenseTime, String expenseDocument) {
        this.id = id;
        this.expenseType = expenseType;
        this.expenseAmount = expenseAmount;
        this.expenseDate = expenseDate;
        this.expenseTime = expenseTime;
        this.expenseDocument = expenseDocument;
    }

    public Expense(String expenseType) {
        this.expenseType = expenseType;
    }

    public Integer getId() {
        return id;
    }

    public String getExpenseType() {
        return expenseType;
    }

    public String getExpenseAmount() {
        return expenseAmount;
    }

    public String getExpenseDate() {
        return expenseDate;
    }

    public String getExpenseTime() {
        return expenseTime;
    }

    public String getExpenseDocument() {
        return expenseDocument;
    }
}
