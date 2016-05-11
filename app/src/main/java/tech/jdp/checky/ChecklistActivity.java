package tech.jdp.checky;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import tech.jdp.checky.db.checklist_item;

public class ChecklistActivity extends AppCompatActivity {

    Integer checklist_id = 0;
    EditText txtTitle;

    private void loadData() {
        if (checklist_id != 0) {
            final ListView lv = (ListView) findViewById(R.id.checkListView);
            assert lv != null;
            ChecklistAdapter adapter = new ChecklistAdapter(getApplicationContext(), checklist_item.readForChecklistId(checklist_id));
            lv.setAdapter(adapter);
            lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    final checklist_item item = (checklist_item) lv.getItemAtPosition(position);
                    AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
                    final EditText text = new EditText(getApplicationContext());
                    text.setSingleLine();
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
                        }
                    });
                    builder.setCancelable(true);
                    builder.create().show();
                    return true;
                }
            });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checklist);
        txtTitle = new EditText(getApplicationContext());

        assert txtTitle != null;
        assert getSupportActionBar() != null;
        getSupportActionBar().setTitle("");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        txtTitle.setSingleLine();
        getSupportActionBar().setCustomView(txtTitle);
        if (getIntent().getExtras() == null) {
            txtTitle.setText("New Checklist");
        } else {
            this.checklist_id = (Integer) getIntent().getExtras().get("id");
        }
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
