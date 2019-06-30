package tech.huqi.quicknote.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by hzhuqi on 2019/4/14
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "quick_note";
    public static final String ALL_NOTE_TABLE_NAME = "all_notes";
    public static final String WASTED_NOTE_TABLE_NAME = "wasted_notes";
    public static final String CREATE_NOTE_TABLE = "create table " + ALL_NOTE_TABLE_NAME
            + " (_id integer primary key autoincrement, title text, content text,"
            + " date varchar(10), address text, timestamp float,lastmodify float, iswasted integer)";
    public static final String CREATE_WASTED_TABLE = "create table " + WASTED_NOTE_TABLE_NAME
            + " (_id integer primary key autoincrement, title text, content text,"
            + " date varchar(10), address text,timestamp float, lastmodify float, iswasted integer)";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_NOTE_TABLE);
        db.execSQL(CREATE_WASTED_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
