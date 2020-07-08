package com.example.shofikshoaib.dailyexpense;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ViewHolder> {

    private Context context;
    private List<Expense> expenseList;

    private ExpenseDatabaseHelper  expenseDatabaseHelper;

    public ExpenseAdapter(Context context, List<Expense> expenses, ExpenseDatabaseHelper expenseDatabaseHelper) {
        this.context = context;
        this.expenseList = expenses;
        this.expenseDatabaseHelper = expenseDatabaseHelper;
    }

    @Override
    public ExpenseAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclear_expense_list,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ExpenseAdapter.ViewHolder holder, final int position) {

        final Expense expense= expenseList.get(position);


        holder.expenseName.setText(expense.getExpenseType());
        holder.expenseDate.setText(expense.getExpenseDate());
        holder.expenseAmount.setText(expense.getExpenseAmount());

        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expenseDatabaseHelper = new ExpenseDatabaseHelper(context);
                expenseDatabaseHelper.deleteData(expense.getId());
                expenseList.remove(position);
                notifyDataSetChanged();

                Toast toast = Toast.makeText(context, "Expense Deleted Successfully", Toast.LENGTH_SHORT);
                View view1 = toast.getView();
                TextView text = (TextView) view1.findViewById(android.R.id.message);
                view1.getBackground().setColorFilter(Color.parseColor("#009900"), PorterDuff.Mode.SRC_IN);
                text.setTextColor(Color.parseColor("#FFFFFF"));
                toast.show();
            }
        });

        holder.editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, UpdateExpenseActivity.class);
                intent.putExtra("expense_id", expense.getId());
                intent.putExtra("expense_name", expense.getExpenseType());
                intent.putExtra("expense_amount", expense.getExpenseAmount());
                intent.putExtra("expense_date", expense.getExpenseDate());
                intent.putExtra("expense_time", expense.getExpenseTime());
                intent.putExtra("expense_document", expense.getExpenseDocument());

                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return expenseList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView expenseName, expenseDate, expenseAmount;
        private ImageView editBtn, deleteBtn;
        public ViewHolder(View itemView) {
            super(itemView);

            expenseAmount = itemView.findViewById(R.id.amountTV);
            expenseName = itemView.findViewById(R.id.expenseNameTV);
            expenseDate = itemView.findViewById(R.id.dateTV);
            editBtn = itemView.findViewById(R.id.editBtnIV);
            deleteBtn = itemView.findViewById(R.id.removeBtnIV);
        }
    }
}
