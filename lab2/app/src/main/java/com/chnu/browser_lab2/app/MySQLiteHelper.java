package com.chnu.browser_lab2.app;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 21.03.2016.
 */
public class MySQLiteHelper extends SQLiteOpenHelper {
    public static final String TABLE_BOOKMARKS = "bookmarks";
    public static final String COLUMN_BOOKMARKS_ID = "id";
    public static final String COLUMN_BOOKMARKS_CONTENT = "content";

    private static final String DATABASE_NAME = "browser-chnu.db";
    private static final int DATABASE_VERSION = 1;

    // Database creation
    private static final String DATABASE_CREATE = "CREATE TABLE " + TABLE_BOOKMARKS
            + "("
            + COLUMN_BOOKMARKS_ID + " integer primary key autoincrement, "
            + COLUMN_BOOKMARKS_CONTENT + " text not null"
            + ");" ;

    public MySQLiteHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOKMARKS);
        onCreate(db);
    }
}
