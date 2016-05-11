package tech.jdp.checky.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by JPiquette on 5/11/2016.
 */

public class checklist {
    public Integer id = 0;
    public String title;
    public String updated_on;
    public String created_on;

    private static db mydb = null;
    private static SQLiteDatabase database;
    private final static String TABLE = "checklists";
    private final static DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm");

    public static void init(Context ctx) {
        if (mydb == null) {
            mydb = new db(ctx);
            database = mydb.getWritableDatabase();
        }
    }

    public static ArrayList<checklist> readAll() {
        Cursor mCursor = database.rawQuery("SELECT id,title,updated_on,created_on FROM " + TABLE + " ORDER BY updated_on ASC", new String[]{});
        ArrayList<checklist> result = new ArrayList<>();
        if (mCursor != null) {
            while (mCursor.moveToNext()) {
                checklist newChecklist = new checklist();
                newChecklist.id = mCursor.getInt(0);
                newChecklist.title = mCursor.getString(1);
                newChecklist.updated_on = dateFormat.format(new Date(mCursor.getLong(2)));
                newChecklist.created_on = dateFormat.format(new Date(mCursor.getLong(3)));
                result.add(newChecklist);
            }
        }
        return result;
    }

    public static checklist read(Integer id) {
        Cursor mCursor = database.rawQuery("SELECT id,title,updated_on,created_on FROM " + TABLE + " WHERE id = ?", new String[]{id.toString()});
        mCursor.moveToFirst();
        checklist newChecklist = new checklist();
        newChecklist.id = id;
        newChecklist.title = mCursor.getString(1);
        newChecklist.updated_on = dateFormat.format(new Date(mCursor.getLong(2)));
        newChecklist.created_on = dateFormat.format(new Date(mCursor.getLong(3)));
        return newChecklist;
    }

    public int delete() {
        return database.delete(TABLE, "id = ?", new String[]{id.toString()});
    }

    public long create() {
        ContentValues vals = new ContentValues();
        vals.put("title", title);
        vals.put("updated_on", System.currentTimeMillis());
        vals.put("created_on", System.currentTimeMillis());
        return database.insert(TABLE, null, vals);
    }

    public int update() {
        ContentValues vals = new ContentValues();
        vals.put("title", title);
        vals.put("updated_on", System.currentTimeMillis());
        return database.update(TABLE, vals, "id = ?", new String[]{id.toString()});

    }
}
