package tech.jdp.checky;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import java.util.Map;

import tech.jdp.checky.db.notes;

public class NoteActivity extends AppCompatActivity {

    private EditText txtNote;
    private Integer id = 0;
    private EditText title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        txtNote = (EditText) findViewById(R.id.txtNote);
        assert txtNote != null;
        assert getSupportActionBar() != null;
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        title = new EditText(getApplicationContext());
        title.setSingleLine();
        title.setNextFocusDownId(R.id.txtNote);
        title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
                final EditText title = new EditText(getApplicationContext());
                title.setText(getSupportActionBar().getTitle());
                builder.setView(title);

                builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getSupportActionBar().setTitle(title.getText());
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
            }
        });
        getSupportActionBar().setCustomView(title);

        if (getIntent().getExtras() == null) {
            title.setText("New Note");
            setTitle("");
        } else {
            this.id = (Integer) getIntent().getExtras().get("id");
            notes note = new notes(getApplicationContext());
            Map<String, Object> res = note.read(this.id);
            title.setText((String) res.get("title"));
            setTitle("");
            txtNote.setText((String) res.get("note"));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (this.id == 0) {
            notes note = new notes(getApplicationContext());
            note.create(title.getText().toString(), txtNote.getText().toString());
        } else {
            notes note = new notes(getApplicationContext());
            note.update(this.id, title.getText().toString(), txtNote.getText().toString());
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
