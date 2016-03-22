package com.chnu.browser_lab2.app.daos;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import com.chnu.browser_lab2.app.MySQLiteHelper;
import com.chnu.browser_lab2.app.dummy.Bookmark;

/**
 * Created by Administrator on 21.03.2016.
 */
public class BookmarksDAO {

    private SQLiteDatabase sqLiteDatabase;

    private MySQLiteHelper mySQLiteHelper;

    private String[] allColumns = {MySQLiteHelper.COLUMN_BOOKMARKS_ID, MySQLiteHelper.COLUMN_BOOKMARKS_CONTENT};

    public BookmarksDAO(Context context){
        mySQLiteHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException{
        sqLiteDatabase = mySQLiteHelper.getWritableDatabase();
    }

    public void close(){
        mySQLiteHelper.close();
    }

    public Bookmark.WebPage createBookmark(String content){
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_BOOKMARKS_CONTENT, content);


    }
}
