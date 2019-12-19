package lk.ac.mrt.cse.dbs.simpleexpensemanager.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

public class DBHelper extends SQLiteOpenHelper{
//    Date date, String accountNo, ExpenseType expenseType, double amount
    private SimpleDateFormat dateFormat =new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

    private static final String DATABASE_NAME = "170074T";
    private static final String TRANSACTIONS_TABLE = "transactions";
    private static final String TRANSACTION_ID = "id";
    private static final String TRANSACTION_DATE = "date";
    private static final String TRANSACTION_ACCOUNTNO = "accountNo";
    private static final String TRANSACTION_EXPENSETYPE = "expenseType";
    private static final String TRANSACTION_AMOUNT = "amount";

    private static final String ACCOUNT_TABLE = "account";
    private static final String ACCOUNT_ACCOUNT_NO = "accountNo" ;
    private static final String ACCOUNT_BANK_NAME = "bankName" ;
    private static final String ACCOUNT_HOLDER_NAME= "accountHolderName" ;
    private static final String ACCOUNT_BALANCE = "balance" ;

    public DBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 4);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    db.execSQL(
            "create table "+ TRANSACTIONS_TABLE + "("
                    +TRANSACTION_DATE +" text,"
                    +TRANSACTION_ACCOUNTNO +" text,"
                    + TRANSACTION_EXPENSETYPE + " text,"
                    + TRANSACTION_AMOUNT +" real" + ")"
                    );
    db.execSQL(
                "create table "+ ACCOUNT_TABLE + "("
                        +ACCOUNT_ACCOUNT_NO+ " text primary key,"
                        +ACCOUNT_BANK_NAME +" text,"
                        +ACCOUNT_HOLDER_NAME +" text,"
                        + ACCOUNT_BALANCE + " real"
                        + ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("drop table if exists " + TRANSACTIONS_TABLE);
        db.execSQL("drop table if exists " + ACCOUNT_TABLE);
        onCreate(db);
    }

    public boolean insertToTransaction(Date date, String accNo, ExpenseType expenseType,double amount){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(TRANSACTION_DATE, dateFormat.format(date));
        cv.put(TRANSACTION_ACCOUNTNO, accNo);
        cv.put(TRANSACTION_EXPENSETYPE, expenseType.toString());
        cv.put(TRANSACTION_AMOUNT, amount);
        long result = db.insert(TRANSACTIONS_TABLE,null,cv);
        return result != -1;
    }

    public Cursor getTransactions(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res  =db.rawQuery("select * from "+ TRANSACTIONS_TABLE ,null);
      return res;
    }


    public Cursor getAccNumbres(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res  =db.rawQuery("select "+ ACCOUNT_ACCOUNT_NO+" from "+ ACCOUNT_TABLE ,null);
        return res;
    }

    public Cursor getAccounts(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res  =db.rawQuery("select * from "+ ACCOUNT_TABLE ,null);
        return res;
    }
    public Cursor getAccount(String accNo){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res  =db.rawQuery("select * from "+ ACCOUNT_TABLE +"where " + ACCOUNT_ACCOUNT_NO + "=\'" +accNo+ "\'",null);
        return res;
    }

    public boolean addAccount(Account account){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(ACCOUNT_ACCOUNT_NO, account.getAccountNo());
        cv.put(ACCOUNT_BANK_NAME, account.getBankName());
        cv.put(ACCOUNT_HOLDER_NAME,account.getAccountHolderName());
        cv.put(ACCOUNT_BALANCE, account.getBalance());
        long result = db.insert(ACCOUNT_TABLE,null,cv);
        return result != -1;
    }
    public void removeAccount(String accNo){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("delete from " + ACCOUNT_TABLE + " where " +ACCOUNT_ACCOUNT_NO + " = \'" +accNo + "\'",null);

    }
}


