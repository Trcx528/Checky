package tech.jdp.checky;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import tech.jdp.checky.db.checklist;
import tech.jdp.checky.db.notes;

public class MainActivity extends AppCompatActivity {

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    public static Context ctx;

    private void setupList() {
        notes note = new notes(getApplicationContext());
        Map<Integer, Map<String, Object>> allNotes = note.read();
        Map<Long, RowItem> data = new HashMap<>();
        for (Integer id : allNotes.keySet()) {
            RowItem newRow = new RowItem();
            newRow.isNote = true;
            newRow.title = (String) allNotes.get(id).get("title");
            newRow.updated = (String) allNotes.get(id).get("updated_on");
            newRow.id = id;
            newRow.updated_time = (long) allNotes.get(id).get("updated_time");
            data.put(newRow.updated_time, newRow);
        }
        ArrayList<checklist> checklistData = checklist.readAll();
        for (checklist list : checklistData) {
            RowItem newRow = new RowItem();
            newRow.isNote = false;
            newRow.title = list.title;
            newRow.updated = list.updated_on;
            newRow.id = list.id;
            newRow.updated_time = list.updated_time;
            data.put(newRow.updated_time, newRow);
        }
        final ListView lv = (ListView) findViewById(R.id.mainListView);
        assert lv != null;

        Object[] sorted = data.keySet().toArray();
        Arrays.sort(sorted);
        int i = 0;
        for (i = 0; i < sorted.length / 2; i++) {
            Object tmp = sorted[i];
            sorted[i] = sorted[sorted.length - 1 - i];
            sorted[sorted.length - 1 - i] = tmp;
        }

        ArrayList<RowItem> finalData = new ArrayList<>();
        for (Object o : sorted) {
            finalData.add(data.get(o));
        }

        MainListAdapter adapter = new MainListAdapter(getApplicationContext(), finalData);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RowItem row = (RowItem) lv.getItemAtPosition(position);
                if (row.isNote) {
                    Intent i = new Intent(getApplicationContext(), NoteActivity.class);
                    Bundle extras = new Bundle();
                    extras.putInt("id", row.id);
                    i.putExtras(extras);
                    startActivity(i);
                } else {
                    Intent i = new Intent(getApplicationContext(), ChecklistActivity.class);
                    Bundle extras = new Bundle();
                    extras.putInt("id", row.id);
                    i.putExtras(extras);
                    startActivity(i);
                }
            }
        });

        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final RowItem row = (RowItem) lv.getItemAtPosition(position);
                if (row.isNote) {
                    AlertDialog.Builder dia = new AlertDialog.Builder(MainActivity.this);

                    dia.setMessage("Delete note " + row.title + "?");
                    dia.setCancelable(true);
                    dia.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            notes note = new notes(getApplicationContext());
                            note.delete(row.id);
                        }
                    });

                    dia.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                        }
                    });

                    dia.create().show();
                } else {
                    AlertDialog.Builder dia = new AlertDialog.Builder(MainActivity.this);

                    dia.setMessage("Delete checklist " + row.title + "?");
                    dia.setCancelable(true);
                    dia.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            checklist.read(row.id).delete();
                        }
                    });

                    dia.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                        }
                    });

                    dia.create().show();
                }
                return true;
            }
        });
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            setupList();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
        ctx = getApplicationContext();
        setupList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.new_id) {
            AlertDialog.Builder dia = new AlertDialog.Builder(MainActivity.this);
            dia.setMessage("Create a new checklist or note?");
            dia.setCancelable(true);
            dia.setPositiveButton("Checklist", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent i = new Intent(getApplicationContext(), ChecklistActivity.class);
                    checklist new_checklist = new checklist();
                    new_checklist.title = "New Checklist";
                    new_checklist.id = (int) new_checklist.create();
                    Bundle extras = new Bundle();
                    System.out.println(new_checklist.id);
                    extras.putInt("id", new_checklist.id);
                    i.putExtras(extras);
                    startActivity(i);
                }
            });

            dia.setNegativeButton("Note", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent i = new Intent(getApplicationContext(), NoteActivity.class);
                    startActivity(i);
                }
            });
            dia.create().show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://tech.jdp.checky/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://tech.jdp.checky/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}
