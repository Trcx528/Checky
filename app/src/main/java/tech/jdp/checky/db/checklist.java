package tech.jdp.checky.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import tech.jdp.checky.MainActivity;

/**
 * Created by JPiquette on 5/11/2016.
 */

public class checklist {
    public Integer id = 0;
    public String title;
    public String updated_on;
    public String created_on;
    public long updated_time;

    private static db mydb = null;
    private static SQLiteDatabase database;
    private final static String TABLE = "checklists";
    private final static DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm");

    public static void init(Context ctx) {
        if (mydb == null) {
            mydb = new db(ctx);
            System.out.println(mydb);
            database = mydb.getWritableDatabase();
        }
    }

    public static ArrayList<checklist> readAll() {
        init(MainActivity.ctx);
        Cursor mCursor = database.rawQuery("SELECT id,title,updated_on,created_on FROM " + TABLE + " ORDER BY updated_on ASC", new String[]{});
        ArrayList<checklist> result = new ArrayList<>();
        if (mCursor != null) {
            while (mCursor.moveToNext()) {
                checklist newChecklist = new checklist();
                newChecklist.id = mCursor.getInt(0);
                newChecklist.title = mCursor.getString(1);
                newChecklist.updated_on = dateFormat.format(new Date(mCursor.getLong(2)));
                newChecklist.created_on = dateFormat.format(new Date(mCursor.getLong(3)));
                newChecklist.updated_time = mCursor.getLong(2);
                result.add(newChecklist);
            }
        }
        return result;
    }

    public static checklist read(Integer id) {
        init(MainActivity.ctx);
        Cursor mCursor = database.rawQuery("SELECT id,title,updated_on,created_on FROM " + TABLE + " WHERE id = ?", new String[]{id.toString()});
        mCursor.moveToFirst();
        checklist newChecklist = new checklist();
        newChecklist.id = id;
        newChecklist.title = mCursor.getString(1);
        newChecklist.updated_on = dateFormat.format(new Date(mCursor.getLong(2)));
        newChecklist.created_on = dateFormat.format(new Date(mCursor.getLong(3)));
        newChecklist.updated_time = mCursor.getLong(2);
        return newChecklist;
    }

    public int delete() {
        init(MainActivity.ctx);
        return database.delete(TABLE, "id = ?", new String[]{id.toString()});
    }

    public long create() {
        init(MainActivity.ctx);
        ContentValues vals = new ContentValues();
        vals.put("title", title);
        vals.put("updated_on", System.currentTimeMillis());
        vals.put("created_on", System.currentTimeMillis());
        return database.insert(TABLE, null, vals);
    }

    public int update() {
        init(MainActivity.ctx);
        ContentValues vals = new ContentValues();
        vals.put("title", title);
        vals.put("updated_on", System.currentTimeMillis());
        return database.update(TABLE, vals, "id = ?", new String[]{id.toString()});

    }
}
