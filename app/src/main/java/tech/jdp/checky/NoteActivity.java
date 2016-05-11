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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        txtNote = (EditText) findViewById(R.id.txtNote);
        assert getSupportActionBar() != null;
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (savedInstanceState == null) {
            setTitle("New Note");
        } else {
            id = (Integer) savedInstanceState.get("id");
            notes note = new notes(getApplicationContext());
            Map<String, Object> res = note.read(id);
            setTitle((String) res.get("title"));
            txtNote.setText((String) res.get("note"));
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (this.id == 0) {
            notes note = new notes(getApplicationContext());
            note.create(getSupportActionBar().getTitle().toString(), txtNote.getText().toString());
        } else {
            notes note = new notes(getApplicationContext());
            note.update(this.id, getSupportActionBar().getTitle().toString(), txtNote.getText().toString());
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
