
package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.DBHelper;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

/**
 * This is an persistent storage implementation of TransactionDAO interface.  All the
 * transaction logs are stored in the db.
 */
public class PersistentTransactionDAO implements TransactionDAO {
    private DBHelper dbHelper;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

    //have the db
    public PersistentTransactionDAO(DBHelper databaseHelper) {
        dbHelper = databaseHelper;
    }

    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        //add transaction to transactions table in db
        dbHelper.insertToTransaction(date, accountNo, expenseType, amount);
    }

    @Override
    public ArrayList<Transaction> getAllTransactionLogs() {
        Cursor res = dbHelper.getTransactions();
        ArrayList<Transaction> transactions = new ArrayList<Transaction>();
        if (res.getCount() == 0) {
            return null;
        }
        while (res.moveToNext()) {
            Date dateAdjust = new Date();
            try {
                dateAdjust = dateFormat.parse(res.getString(1));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String accountNo = res.getString(2);
            ExpenseType expenseType;
            if (res.getString(3).equals("EXPENSE")) {
                expenseType = ExpenseType.EXPENSE;
            } else {
                expenseType = ExpenseType.INCOME;
            }
            Double amount = Double.parseDouble(res.getString(4));
            Transaction transaction = new Transaction(dateAdjust, accountNo, expenseType, amount);
            transactions.add(transaction);
        }
        return transactions;
    }


    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        ArrayList<Transaction> transactions = getAllTransactionLogs();
        int size = transactions.size();
        if (size <= limit) {
            return transactions;
        }
        return transactions.subList(size - limit, size);
    }
}