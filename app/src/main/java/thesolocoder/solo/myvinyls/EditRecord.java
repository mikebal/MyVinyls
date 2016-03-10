package thesolocoder.solo.myvinyls;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

/**
 * Created by Michael on 3/8/2016.
 */
public class EditRecord extends AppCompatActivity {
    EditText albumName, albumYear, albumBand;
    MyDBHandler dbHandler;
    ImageButton albumArtwork;
    Bitmap albumCover;
    Uri mPhotoUri = null;
    int imageOrientation = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addrecord);
        setupVariables();
        changeButtonTextToSave();
        loadRecordToEdit();
    }

    private void setupVariables() {
        albumName = (EditText) findViewById(R.id.editText_albumName);
        albumYear = (EditText) findViewById(R.id.editText_year);
        albumBand = (EditText) findViewById(R.id.editText_bandName);
        albumArtwork = (ImageButton) findViewById(R.id.imageButton);
        dbHandler = new MyDBHandler(getApplicationContext(), null, null, 1);
    }
    private void changeButtonTextToSave()
    {
        Button saveButton = (Button) findViewById(R.id.button);
        saveButton.setText("Save");
    }
    private void loadRecordToEdit(){
        Bundle extras = getIntent().getExtras();
        String _id = extras.getString("toEditID");
    }
}
