package tech.jdp.checky.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Trcx on 5/10/2016.
 */
public class db extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "main";
    private static final int DATABASE_VERSION = 1;

    public db(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE notes (id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, note TEXT, created_on INTEGER, updated_on INTEGER)");
        db.execSQL("CREATE TABLE checklists (id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, created_on INTEGER, updated_on INTEGER)");
        db.execSQL("CREATE TABLE checklist_items (id INTEGER PRIMARY KEY AUTOINCREMENT, checklist_id INTEGER, text TEXT, checked INTEGER, created_on INTEGER, updated_on INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
