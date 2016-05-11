package tech.jdp.checky.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Trcx on 5/10/2016.
 */
public class notes {
    private db mydb;
    private SQLiteDatabase database;
    private final static String TABLE = "notes";
    private final static DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    public notes(Context context) {
        mydb = new db(context);
        database = mydb.getWritableDatabase();
    }

    public long create(String title, String note) {
        ContentValues vals = new ContentValues();
        vals.put("title", title);
        vals.put("note", note);
        vals.put("updated_on", dateFormat.format(new Date()));
        vals.put("created_on", dateFormat.format(new Date()));
        return database.insert(TABLE, null, vals);
    }

    public Map<Integer, Map<String, Object>> read() {
        Cursor mCursor = database.rawQuery("SELECT id,title,note,updated_on,created_on FROM " + TABLE + " ORDER BY id", new String[]{});
        Map<Integer, Map<String, Object>> result = new HashMap<>();
        if (mCursor != null) {
            while (mCursor.moveToNext()) {
                Map<String, Object> temp = new HashMap<>();
                temp.put("id", mCursor.getInt(0));
                temp.put("title", mCursor.getString(1));
                temp.put("note", mCursor.getString(2));
                temp.put("updated_on", mCursor.getString(3));
                temp.put("created_on", mCursor.getString(4));
                result.put((Integer) temp.get("id"), temp);
            }
        }
        return result;
    }


    public Map<String, Object> read(int id) {
        Cursor mCursor = database.rawQuery("SELECT id,title,note,updated_on,created_on FROM " + TABLE + " WHERE id = ?", new String[]{String.valueOf(id)});
        Map<String, Object> result = new HashMap<String, Object>();
        if (mCursor != null) {
            mCursor.moveToFirst();
            result.put("id", id);
            result.put("title", mCursor.getString(1));
            result.put("note", mCursor.getString(2));
            result.put("updated_on", mCursor.getString(3));
            result.put("created_on", mCursor.getString(4));
        }
        return result;
    }

    public int update(int id, String title, String note) {
        ContentValues vals = new ContentValues();
        vals.put("title", title);
        vals.put("note", note);
        vals.put("updated_on", dateFormat.format(new Date()));
        return database.update(TABLE, vals, "id = ?", new String[]{String.valueOf(id)});
    }

    public int delete(int id) {
        return database.delete(TABLE, "id = ?", new String[]{String.valueOf(id)});
    }
}
