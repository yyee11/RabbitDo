package com.example.todo.cart;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CartDatabase extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Cart.db";
    private static final String TABLE_NAME = "cart_table";
    private static final String COL_1 = "ID";
    private static final String COL_2 = "TITLE";
    private static final String COL_3 = "PRICE";
    private static final String COL_4 = "IMAGE";
    private static final String COL_5 = "USERID";
    private static final int DATABASED_VERSION = 1;
    public CartDatabase(Context context) {
        // super(context, name, factor, version)
                super(context, DATABASE_NAME, null, DATABASED_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table if NOT EXISTS " + TABLE_NAME +
                "(ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "TITLE TEXT, PRICE TEXT, IMAGE INTEGER, USERID TEXT)");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
    // Method to insert a record to the database
    public boolean insertData(String title, String price, int image, String userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        onCreate(db);
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2,title);
        contentValues.put(COL_3,price);
        contentValues.put(COL_4,image);
        contentValues.put(COL_5,userId);
        long result = db.insert(TABLE_NAME, null, contentValues);
        if (result == -1)
            return false;
        else
            return true;
    }
    // Method to show all the records
    public Cursor getAllData(String username) {
        SQLiteDatabase db = this.getWritableDatabase();
        onCreate(db);
        String sql = "select * from " + TABLE_NAME + " where " + COL_5 + " =?";
        Cursor res = db.rawQuery(sql, new String[]{username});
        return res;
    }
    public String getName(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        onCreate(db);
        String sql = "select * from " + TABLE_NAME + " where " + COL_1 + " =?";
        Cursor res = db.rawQuery(sql, new String[]{id});
        res.moveToNext();
        StringBuffer buffer = new StringBuffer();
        buffer.append(res.getString(1));
        String name = buffer.toString();
        return name;
    }
    // Method to update a record
//    public boolean updateData(String id, String title, String price, int image)
//    {
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues contentValues = new ContentValues();
//        contentValues.put(COL_1,id);
//        contentValues.put(COL_2,title);
//        contentValues.put(COL_3,price);
//        contentValues.put(COL_4,image);
//        db.update(TABLE_NAME, contentValues, "ID = ?", new String[] {id});
//        return true;
//    }
    // Method to delete a record
    public Integer deleteData (String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "ID = ?", new String[] {id});
    }

    public void deleteAll (){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE  FROM " + TABLE_NAME);
    }
}
