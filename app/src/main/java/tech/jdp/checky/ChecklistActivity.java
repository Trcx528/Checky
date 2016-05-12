package tech.jdp.checky;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import tech.jdp.checky.db.checklist;
import tech.jdp.checky.db.checklist_item;

public class ChecklistActivity extends AppCompatActivity {

    Integer checklist_id = 0;
    EditText txtTitle;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        checklist list = checklist.read(checklist_id);
        list.title = txtTitle.getText().toString();
        list.update();
    }

    private void loadData() {
        final ListView lv = (ListView) findViewById(R.id.checkListView);
        final Context ctx = this;
        assert lv != null;
        checklist list = checklist.read(checklist_id);
        txtTitle.setText(list.title);
        ChecklistAdapter adapter = new ChecklistAdapter(getApplicationContext(), checklist_item.readForChecklistId(checklist_id));
        lv.setAdapter(adapter);
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final checklist_item item = (checklist_item) lv.getItemAtPosition(position);
                AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
                final EditText text = new EditText(getApplicationContext());
                checklist cl = checklist.read(checklist_id);
                cl.title = txtTitle.getText().toString();
                cl.update();
                text.setSingleLine();
                builder.setTitle("Edit Item");
                text.setTextColor(Color.BLACK);
                text.setText(item.text);
                builder.setView(text);
                builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        item.text = text.getText().toString();
                        item.update();
                    }
                });

                builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builder.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        item.delete();
                        loadData();
                    }
                });
                builder.setCancelable(true);
                builder.create().show();
                return true;
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checklist);
        txtTitle = new EditText(getApplicationContext());

        assert getSupportActionBar() != null;
        getSupportActionBar().setTitle("");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        txtTitle.setSingleLine();
        txtTitle.setHint("New Checklist");
        txtTitle.setText("New Checklist");
        getSupportActionBar().setCustomView(txtTitle);
        if (getIntent().getExtras() != null) {
            this.checklist_id = (Integer) getIntent().getExtras().get("id");
        }
        loadData();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        } else if (id == R.id.new_checklist_item_id) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            final EditText text = new EditText(this);
            text.setSingleLine();
            builder.setView(text);
            builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    checklist_item new_item = new checklist_item();
                    new_item.checklist_id = checklist_id;
                    new_item.text = text.getText().toString();
                    new_item.checked = false;
                    new_item.create();
                    loadData();
                }
            });

            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            builder.setTitle("New Item");
            builder.setCancelable(true);
            builder.create().show();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_checklist, menu);
        return true;
    }
}
