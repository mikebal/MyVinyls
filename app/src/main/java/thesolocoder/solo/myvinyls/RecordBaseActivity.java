package thesolocoder.solo.myvinyls;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;

import java.util.ArrayList;

/**
 * Created by Michael on 6/28/2016.
 */
public class RecordBaseActivity extends Activity {

    EditText albumName, albumYear, albumBand;
    ImageButton albumArtwork;
    Bitmap albumCover;
    Uri mPhotoUri = null;
    int imageOrientation = 0;
    String editCall = "-1";
    String dbTableReferenced;
    ArrayList<String> genres = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addrecord);
        setupVariables();
        Bundle extras = getIntent().getExtras();
        dbTableReferenced = extras.getString("toAddToTable");
    }

    private void setupVariables() {
        albumName = (EditText) findViewById(R.id.editText_albumName);
        albumYear = (EditText) findViewById(R.id.editText_year);
        albumBand = (EditText) findViewById(R.id.editText_bandName);
        albumArtwork = (ImageButton) findViewById(R.id.imageButton);
    }
}
