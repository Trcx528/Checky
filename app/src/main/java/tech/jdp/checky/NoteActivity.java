package tech.jdp.checky;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
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
        getSupportActionBar().setCustomView(title);
        setTitle("");
        if (getIntent().getExtras() == null) {
            title.setText("New Note");
        } else {
            this.id = (Integer) getIntent().getExtras().get("id");
            notes note = new notes(getApplicationContext());
            Map<String, Object> res = note.read(this.id);
            title.setText((String) res.get("title"));
            txtNote.setText((String) res.get("note"));
        }
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
