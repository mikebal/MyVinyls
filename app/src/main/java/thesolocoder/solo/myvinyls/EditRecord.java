package thesolocoder.solo.myvinyls;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.File;

public class EditRecord extends RecordBaseActivity {

    String callingTable;

    @Override
    public void onStart(){
        super.onStart();
        Bundle extras = getIntent().getExtras();
        String _id = extras.getString("toEditID");
        if(_id == null || _id.equals("New Entry"))
            return;
        callingTable = extras.getString("callingTable");
        if(callingTable.equals("lentout"))
            callingTable = "records";
        editCall = _id;

        ImageButton deleteButton = (ImageButton) findViewById(R.id.imageButtonDelete);
        deleteButton.setVisibility(View.VISIBLE);
        loadRecordToEdit(_id);
        changeButtonTextToSave();
    }

    private void loadRecordToEdit(String _id){
        MyDBHandler dbHandler = new MyDBHandler(getApplicationContext(), null, null, 1);
        Records recordToEdit = dbHandler.getRecordByID(_id, callingTable);
        albumName.setText(recordToEdit.get_albumname());
        albumBand.setText(recordToEdit.get_bandname());
        albumYear.setText(recordToEdit.get_releaseyear());
        mPhotoUri = Uri.parse(recordToEdit.get_imageurl());
        loadImage(albumArtwork, _id);
    }
    private void loadImage(ImageView albumCover, String fileName) {
        String imageInSD = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "/MyVinylsAlbumArt/" + fileName + ".jpg";
        Bitmap bitmap = BitmapFactory.decodeFile(imageInSD);
        if(bitmap != null)
            albumCover.setImageBitmap(bitmap);
    }
    private void changeButtonTextToSave()
    {
        Button saveButton = (Button) findViewById(R.id.button);
        saveButton.setText(R.string.save);
    }

    public void addRecordClicked(View v) {
        String  newAlbumID;
        newAlbumID = editCall;
        if (hasRequiredFields()) {
            Records newRecord = createRecord();
            if(newRecord != null){
                MyDBHandler dbHandler = new MyDBHandler(getApplicationContext(), null, null, 1);
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

    public void deleteClicked(View v){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Confirm Delete");
        builder.setMessage("Are you sure?\nThis can't be undone.");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                MyDBHandler dbHandler = new MyDBHandler(getApplicationContext(), null, null, 1);
                dbHandler.deleteRecord(editCall, callingTable);
                ImageManager imageManager = new ImageManager();
                imageManager.deleteImage(editCall);
                dialog.dismiss();
                finish();
            }

        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do nothing
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    public void addGenreClicked(View v) {
        Intent open_AddGenre = new Intent(EditRecord.this, AddGenre.class);
        open_AddGenre.putExtra("toEditID", String.valueOf(editCall));
        startActivityForResult(open_AddGenre, 111);
    }
}
