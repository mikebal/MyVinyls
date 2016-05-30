package thesolocoder.solo.myvinyls;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import java.io.File;
import java.util.ArrayList;

/**
 * Created by Michael on 5/27/2016.
 */
public class RecordReturn extends AppCompatActivity{

     ImageView albumArtwork;

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
        ArrayList<LentOut> lentInfo = dbHandler.getLentOut("SELECT * FROM lentout WHERE _id=" + recordID);
        populateDisplayLendOutInfo(lentInfo.get(0), dbHandler);
        Records album = dbHandler.getRecordByID(lentInfo.get(0).id, "records");
        populateDisplayAlbumInfo(album);
    }

    private void populateDisplayLendOutInfo(LentOut lentInfo, MyDBHandler dbHandler)
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
        albumCover.setImageBitmap(bitmap);
    }

}
