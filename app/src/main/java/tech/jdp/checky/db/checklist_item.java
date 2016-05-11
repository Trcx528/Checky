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
public class checklist_item {
    public Integer id = 0;
    public Integer checklist_id;
    public String text;
    public boolean checked;
    public String created_on;
    public String updated_on;

    private static db mydb = null;
    private static SQLiteDatabase database;
    private final static String TABLE = "checklist_items";
    private final static DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm");

    public static void init(Context ctx) {
        if (mydb == null) {
            mydb = new db(ctx);
            database = mydb.getWritableDatabase();
        }
    }

    public static ArrayList<checklist_item> readForChecklistId(int checklist_id) {
        ArrayList<checklist_item> result = new ArrayList<>();
        Cursor mCursor = database.rawQuery("SELECT id,checklist_id,text,checked,updated_on,created_on FROM " + TABLE + " ORDER BY updated_on ASC", new String[]{});
        assert mCursor != null;
        while (mCursor.moveToNext()) {
            checklist_item new_item = new checklist_item();
            new_item.id = mCursor.getInt(0);
            new_item.checklist_id = mCursor.getInt(1);
            new_item.text = mCursor.getString(2);
            new_item.checked = mCursor.getInt(3) == 1;
            new_item.updated_on = dateFormat.format(new Date(mCursor.getLong(4)));
            new_item.created_on = dateFormat.format(new Date(mCursor.getLong(5)));
            result.add(new_item);
        }
        return result;
    }

    public static checklist_item read(Integer id) {
        checklist_item new_item = new checklist_item();
        Cursor mCursor = database.rawQuery("SELECT id, checklist_id, checked, updated_on, created_on FROM " + TABLE + " WHERE id = ?", new String[]{id.toString()});
        assert mCursor != null;
        mCursor.moveToFirst();
        new_item.id = id;
        new_item.checklist_id = mCursor.getInt(1);
        new_item.text = mCursor.getString(2);
        new_item.checked = mCursor.getInt(3) == 1;
        new_item.updated_on = dateFormat.format(new Date(mCursor.getLong(4)));
        new_item.created_on = dateFormat.format(new Date(mCursor.getLong(5)));
        return new_item;
    }

    public int delete() {
        return database.delete(TABLE, "id = ?", new String[]{id.toString()});
    }

    public long create() {
        ContentValues vals = new ContentValues();
        vals.put("checklist_id", checklist_id);
        vals.put("text", text);
        vals.put("checked", checked ? 1 : 0);
        vals.put("created_on", System.currentTimeMillis());
        vals.put("updated_on", System.currentTimeMillis());
        return database.insert(TABLE, null, vals);
    }

    public int update() {
        ContentValues vals = new ContentValues();
        vals.put("text", text);
        vals.put("checked", checked ? 1 : 0);
        vals.put("created_on", System.currentTimeMillis());
        return database.update(TABLE, vals, "id = ?", new String[]{id.toString()});
    }
}
