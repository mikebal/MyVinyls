package thesolocoder.solo.myvinyls;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.MediaStore;
import android.view.View;
import android.widget.TextView;

import java.util.StringTokenizer;

public class AddRecord extends RecordBaseActivity {

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
            newRecord.set_genre(genres);

            if(albumCover != null)
                newRecord.set_hasimage("true");
            else
                newRecord.set_hasimage("false");


            MyDBHandler dbHandler = new MyDBHandler(getApplicationContext(), null, null, 1);
            if(editCall.equals("-1")) {
                newAlbumID = dbHandler.addRecord(newRecord, dbTableReferenced , dbTableReferenced + "genres", false);
            }
            else{
                newAlbumID = editCall;
                newRecord.set_id(editCall);
                dbHandler.updateRecord(newRecord);
            }
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
        open_AddGenre.putExtra("toEditID", String.valueOf(editCall));
        startActivityForResult(open_AddGenre, 111);
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
    private void loadAndSetImage(){
        try {
            ImageManager imageManager = new ImageManager();
            albumCover = imageManager.getCorrectlyOrientedImage(getApplicationContext(), mPhotoUri, imageOrientation);
            albumArtwork.setImageBitmap(albumCover);
        }catch (Exception e){
            albumArtwork.setImageResource(R.mipmap.ic_report_black_24dp);
        }
    }
}