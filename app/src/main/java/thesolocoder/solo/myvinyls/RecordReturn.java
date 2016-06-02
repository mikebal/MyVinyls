package thesolocoder.solo.myvinyls;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import java.io.File;
import java.util.ArrayList;

/**
 * Created by Michael on 5/27/2016.
 */
public class RecordReturn extends AppCompatActivity{

    ImageView albumArtwork;
    Records album;
    ArrayList<LentOut> lentInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lentoutreview);
        albumArtwork = (ImageView) findViewById(R.id.imageButton);
        loadRecordLendOutData();
    }

    private void loadRecordLendOutData(){
        Bundle extras = getIntent().getExtras();
        String recordID = extras.getString("toEditID");
        MyDBHandler dbHandler = new MyDBHandler(getApplicationContext(), null, null, 1);
        lentInfo = dbHandler.getLentOut("SELECT * FROM lentout WHERE _id=" + recordID);
        populateDisplayLendOutInfo(lentInfo.get(0));
        album = dbHandler.getRecordByID(lentInfo.get(0).id, "records");
        populateDisplayAlbumInfo(album);
    }

    private void populateDisplayLendOutInfo(LentOut lentInfo)
    {
        TextView name = (TextView) findViewById(R.id.lentToName);
        TextView lentOn = (TextView) findViewById(R.id.lentoutLentOn);
        TextView dueBackBy = (TextView) findViewById(R.id.lentoutDueOn);

        name.setText(lentInfo.name);
        lentOn.setText(lentInfo.getDateString(lentInfo.lentout));
        dueBackBy.setText(lentInfo.getDateString(lentInfo.dueBack));
    }

    private void populateDisplayAlbumInfo(Records records)
    {
        TextView albumName = (TextView) findViewById(R.id.textViewAlbumName);
        TextView albumBandName = (TextView) findViewById(R.id.textViewBandName);
        TextView albumYear = (TextView) findViewById(R.id.textViewYear);
        if(records.get_albumname() != null)
            albumName.setText(records.get_albumname());
        albumBandName.setText(records.get_bandname());
        albumYear.setText(records.get_releaseyear());
        loadImage(albumArtwork, records.get_imageurl());
    }

    private void loadImage(ImageView albumCover, String fileName) {
        String imageInSD = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "/MyVinylsAlbumArt/" + fileName + ".jpg";
        Bitmap bitmap = BitmapFactory.decodeFile(imageInSD);
        if(bitmap != null)
            albumCover.setImageBitmap(bitmap);
        else
            albumCover.setImageResource(R.mipmap.default_noartwork);
    }

    public void returnRecord(View v)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Confirm Return");
        builder.setMessage("Are you sure that:\n" + lentInfo.get(0).name + " has returned " + album.get_albumname() + "?");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                MyDBHandler dbHandler = new MyDBHandler(getApplicationContext(), null, null, 1);
                dbHandler.runRawQueryNoResult("DELETE FROM " + "lentout WHERE album_id=\"" + lentInfo.get(0).id + "\";");
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
