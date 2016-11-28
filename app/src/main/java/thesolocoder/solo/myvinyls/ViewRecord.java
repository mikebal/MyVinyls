package thesolocoder.solo.myvinyls;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.File;

public class ViewRecord extends EditRecord{

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

        ImageButton editButton = (ImageButton) findViewById(R.id.imageButtonEdit);
        editButton.setVisibility(View.VISIBLE);

        loadRecordToEdit(_id);
        hideUnusedElements();
        disableFocus();
    }

    private void hideUnusedElements(){
        findViewById(R.id.button).setVisibility(View.GONE);
        findViewById(R.id.imageButtonRotation).setVisibility(View.GONE);
        findViewById(R.id.imageButton2).setVisibility(View.GONE);
        findViewById(R.id.textView4).setVisibility(View.GONE);
        findViewById(R.id.textView5).setVisibility(View.GONE);
        ImageButton deleteButton = (ImageButton) findViewById(R.id.imageButtonDelete);
        deleteButton.setVisibility(View.GONE);
    }

    private void disableFocus(){
        EditText editText = (EditText) findViewById(R.id.editText_albumName);
        editText.setFocusable(false);
        editText = (EditText) findViewById(R.id.editText_year);
        editText.setFocusable(false);
        editText = (EditText) findViewById(R.id.editText_bandName);
        editText.setFocusable(false);
        editText = (EditText) findViewById(R.id.editTextNote);
        editText.setFocusable(false);
        ImageButton albumCover = (ImageButton) findViewById(R.id.imageButton);
        albumCover.setClickable(false);
        loadImage(albumCover, editCall);
    }

    public void editClicked(View v){
        Intent startActivity = new Intent();
        startActivity.setClass(getApplicationContext(), EditRecord.class);
        startActivity.setAction(ViewRecord.class.getName());
        startActivity.setFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        startActivity.putExtra("toEditID", editCall);
        startActivity.putExtra("callingTable", callingTable);
        getApplicationContext().startActivity(startActivity);
    }

    private void loadImage(ImageButton albumCover, String fileName) {
        String imageInSD = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "/MyVinylsAlbumArt/" + fileName + ".jpg";
        Bitmap bitmap = BitmapFactory.decodeFile(imageInSD);
        if(bitmap != null)
            albumCover.setImageBitmap(bitmap);
        else
            albumCover.setImageResource(R.mipmap.default_noartwork);
    }
}
