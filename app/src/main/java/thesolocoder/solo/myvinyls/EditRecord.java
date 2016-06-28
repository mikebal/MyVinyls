package thesolocoder.solo.myvinyls;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

/**
 * Created by Michael on 6/28/2016.
 */
public class EditRecord extends RecordBaseActivity {

    @Override
    public void onStart(){
        super.onStart();
        Bundle extras = getIntent().getExtras();
        String _id = extras.getString("toEditID");
        if(_id == null || _id.equals("New Entry"))
            return;
        editCall = _id;

        ImageButton deleteButton = (ImageButton) findViewById(R.id.imageButtonDelete);
        deleteButton.setVisibility(View.VISIBLE);
        loadRecordToEdit(_id);
        changeButtonTextToSave();
    }

    private void loadRecordToEdit(String _id){
        MyDBHandler dbHandler = new MyDBHandler(getApplicationContext(), null, null, 1);
        Records recordToEdit = dbHandler.getRecordByID(_id, "records");
        albumName.setText(recordToEdit.get_albumname());
        albumBand.setText(recordToEdit.get_bandname());
        albumYear.setText(recordToEdit.get_releaseyear());
        mPhotoUri = Uri.parse(recordToEdit.get_imageurl());
        loadImage(albumArtwork, _id);
    }
    private void loadImage(ImageView albumCover, String fileName) {
        String imageInSD = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "/MyVinylsAlbumArt/" + fileName + ".jpg";
        Bitmap bitmap = BitmapFactory.decodeFile(imageInSD);
        albumCover.setImageBitmap(bitmap);
    }
    private void changeButtonTextToSave()
    {
        Button saveButton = (Button) findViewById(R.id.button);
        saveButton.setText(R.string.save);
    }

    public void deleteClicked(View v){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Confirm Delete");
        builder.setMessage("Are you sure?\nThis can't be undone.");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                MyDBHandler dbHandler = new MyDBHandler(getApplicationContext(), null, null, 1);
                dbHandler.deleteRecord(editCall);
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
}