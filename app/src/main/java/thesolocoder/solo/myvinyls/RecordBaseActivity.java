package thesolocoder.solo.myvinyls;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.StringTokenizer;

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

    public boolean hasRequiredFields() {
        boolean hasAllRequiredFields = true;
        if (albumName.getText().toString().equals("")) {
            hasAllRequiredFields = false;
            TextView requiredAstrix = (TextView) findViewById(R.id.textView_RequiredAlbumName);
            requiredAstrix.setVisibility(View.VISIBLE);
        }
        return hasAllRequiredFields;
    }
    public Records createRecord() {
        Records newRecord = null;
        if (hasRequiredFields())
        {
            newRecord = new Records();
            newRecord.set_albumname(albumName.getText().toString());

            if (!albumYear.getText().toString().equals(""))
                newRecord.set_releaseyear(albumYear.getText().toString());
            else
                newRecord.set_releaseyear("");
            newRecord.set_bandname(albumBand.getText().toString());
            newRecord.set_genre(genres);

            if (albumCover != null)
                newRecord.set_hasimage("true");
            else
                newRecord.set_hasimage("false");

        }
        return newRecord;
    }

    public void albumCoverClicked(View v) {
        selectImage();
    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(RecordBaseActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    mPhotoUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new ContentValues());
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, mPhotoUri);
                    startActivityForResult(intent,666);
                } else if (items[item].equals("Choose from Library")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(Intent.createChooser(intent, "Select File"),777);
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imageOrientation = 0;
        if (resultCode == RESULT_OK) {
            if (requestCode == 111) {
                String returnedData = data.getStringExtra("returnKey");
                newLineStringToArrayList(returnedData);
            }
            else {
                if (requestCode == 777)
                    mPhotoUri = data.getData();
                else {
                    SharedPreferences savedData;
                    savedData = getApplicationContext().getSharedPreferences("savedData", 0);// save data
                    imageOrientation = savedData.getInt("camera_correction", 0);
                }
                loadAndSetImage();
            }
        }
    }
    public void rotateImageRightClicked(View v){
        imageOrientation += 90;
        if(imageOrientation > 270)
            imageOrientation = 0;

        SharedPreferences savedData;
        savedData = getApplicationContext().getSharedPreferences("savedData", 0);// save data
        SharedPreferences.Editor editor = savedData.edit();
        editor.putInt("camera_correction", imageOrientation);
        editor.apply();
        loadAndSetImage();
    }
    private void newLineStringToArrayList(String stringToParse)
    {
        StringTokenizer tokens = new StringTokenizer(stringToParse, "\n");
        while(tokens.hasMoreTokens()) {
            String current = tokens.nextToken();
            if(current.equals("###"))
                genres.add("");
            else
                genres.add(current);
        }
    }
    private void loadAndSetImage() {
        try {
            ImageManager imageManager = new ImageManager();
            albumCover = imageManager.getCorrectlyOrientedImage(getApplicationContext(), mPhotoUri, imageOrientation);
            albumArtwork.setImageBitmap(albumCover);
        } catch (Exception e) {
            albumArtwork.setImageResource(R.mipmap.ic_report_black_24dp);
        }
    }
}
