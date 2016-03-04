package thesolocoder.solo.myvinyls;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class AddRecord extends AppCompatActivity {

    EditText albumName, albumYear, albumBand;
    ImageButton albumArtwork;
    Bitmap albumCover;
    Uri mPhotoUri = null;
    int imageOrientation = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addrecord);
        setupVariables();
    }

    private void setupVariables() {
        albumName = (EditText) findViewById(R.id.editText_albumName);
        albumYear = (EditText) findViewById(R.id.editText_year);
        albumBand = (EditText) findViewById(R.id.editText_bandName);
        albumArtwork = (ImageButton) findViewById(R.id.imageButton);
    }

    public void addRecordClicked(View v) {
        String  newAlbumID;
        if (hasRequiredFields()) {
            Records newRecord = new Records();
            newRecord.set_albumname(albumName.getText().toString());

            if (!albumYear.getText().toString().equals(""))
                newRecord.set_releaseyear(albumYear.getText().toString());
            else
                newRecord.set_releaseyear("");
            newRecord.set_bandname(albumBand.getText().toString());

            MyDBHandler dbHandler = new MyDBHandler(getApplicationContext(), null, null, 1);
            newAlbumID = dbHandler.addRecord(newRecord);
            if(albumCover != null) {
                ImageManager imageManager = new ImageManager();
                imageManager.saveImageToFile(albumCover, newAlbumID);
            }
            finish();
        }
    }

    private boolean hasRequiredFields() {
        boolean hasAllRequiredFields = true;
        if (albumName.getText().toString().equals("")) {
            hasAllRequiredFields = false;
            TextView requiredAstrix = (TextView) findViewById(R.id.textView_RequiredAlbumName);
            requiredAstrix.setVisibility(View.VISIBLE);
        }
        return hasAllRequiredFields;
    }

    public void addGenreClicked(View v) {
        Intent open_AddGenre = new Intent(AddRecord.this, AddGenre.class);
        startActivityForResult(open_AddGenre, 1);
    }

    public void albumCoverClicked(View v) {
        selectImage();
    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(AddRecord.this);
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
        if (resultCode == RESULT_OK) {
            imageOrientation = 0;
            if(requestCode == 777)
                mPhotoUri = data.getData();
           loadAndSetImage();
        }
    }
    public void rotateImageRightClicked(View v){
        imageOrientation += 90;
        if(imageOrientation > 270)
            imageOrientation = 0;
        loadAndSetImage();
    }
    private void loadAndSetImage(){
        try {
            ImageManager imageManager = new ImageManager();
            albumCover = imageManager.getCorrectlyOrientedImage(getApplicationContext(), mPhotoUri, imageOrientation);
            albumArtwork.setImageBitmap(albumCover);
        }
        catch (Exception e){
            albumArtwork.setImageResource(R.mipmap.ic_report_black_24dp);
        }
    }
}