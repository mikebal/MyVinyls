package thesolocoder.solo.myvinyls;

import android.content.Intent;
import android.view.View;

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

            if(!notes.getText().toString().equals(""))
                newRecord.set_notes(notes.getText().toString());
            else
                newRecord.set_notes("");

            MyDBHandler dbHandler = new MyDBHandler(getApplicationContext(), null, null, 1);
            if(editCall.equals("-1")) {
                newAlbumID = dbHandler.addRecord(newRecord, dbTableReferenced , dbTableReferenced + "genres", false);
            }
            else{
                newAlbumID = editCall;
                newRecord.set_id(editCall);
                dbHandler.updateRecord(newRecord, dbTableReferenced);
            }
            if(albumCover != null) {
                ImageManager imageManager = new ImageManager();
                imageManager.saveImageToFile(albumCover, dbTableReferenced + newAlbumID);
            }
            finish();
        }
    }

    public void addGenreClicked(View v) {
        Intent open_AddGenre = new Intent(AddRecord.this, AddGenre.class);
        open_AddGenre.putExtra("toEditID", String.valueOf(editCall));
        startActivityForResult(open_AddGenre, 111);
    }
}