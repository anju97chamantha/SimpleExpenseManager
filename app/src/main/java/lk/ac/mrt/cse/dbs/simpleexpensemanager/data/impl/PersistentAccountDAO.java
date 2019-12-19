
package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.Context;
import android.database.Cursor;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.DBHelper;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

/**
 * This is an persistent storage implementation of AccountDAO interface. All
 * account details are saved in the memory
 */

public class PersistentAccountDAO implements AccountDAO {
    private DBHelper dbHelper;
    private Context context;
    public PersistentAccountDAO(DBHelper databaseHelper,Context context) {
        this.dbHelper = databaseHelper;
        this.context = context;
    }

    @Override
    public List<String> getAccountNumbersList() {
        Cursor res = dbHelper.getAccNumbres();
        ArrayList<String> accNumbers = new ArrayList<String>();
        if (res.getCount() == 0) {
            return null;
        }
        while (res.moveToNext()) {
            String accountNo = res.getString(0);
        }
        return accNumbers;
    }

    @Override
    public List<Account> getAccountsList() {
        //select * from accounts table
        Cursor res = dbHelper.getAccounts();
        ArrayList<Account> accounts = new ArrayList<Account>();
        while (res.moveToNext()){
            Account account = new Account(res.getString(0),res.getString(1),res.getString(2),Double.parseDouble(res.getString(3)));
            accounts.add(account);
        }
        return accounts;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        //select & from acounts table where accnumber = accountNo
        Cursor res = dbHelper.getAccount(accountNo);
        Account account=null;
        while(res.moveToNext()){
            account = new Account(res.getString(0), res.getString(1),res.getString(2),Double.parseDouble(res.getString(3)));
        }
        return account;
    }

    @Override
    public void addAccount(Account account) {
        //insert into accounts values(...)
        dbHelper.addAccount(account);
    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        //remove entry from the account table where accountNo =accountNo
    dbHelper.removeAccount(accountNo);
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        Account account =getAccount(accountNo);

        // specific implementation based on the transaction type
        switch (expenseType) {
        case EXPENSE:
            account.setBalance(account.getBalance() - amount);
            break;
        case INCOME:
            account.setBalance(account.getBalance() + amount);
            break;
        }
        //dbHelper.updateAccount(account.getAccountNo(),account.getBankName(),account.getAccountHolderName(),account.getBalance());
    }
}
