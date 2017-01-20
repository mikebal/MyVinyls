package thesolocoder.solo.myvinyls;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import java.io.File;
import java.util.ArrayList;

public class UpdateAlbumCoverFix extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences preferences = getSharedPreferences("prefs", MODE_PRIVATE);
        boolean useDarkTheme = preferences.getBoolean("dark_theme", false);
        if (useDarkTheme)
            setTheme(R.style.AppTheme_Dark);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.updatealbumcoverfix);

        if (useDarkTheme) {
            LinearLayout layout = (LinearLayout) findViewById(R.id.backupRestoreLayout);
            layout.setBackgroundColor(Color.DKGRAY);
        }

        final MyDBHandler dbHandler = new MyDBHandler(getApplicationContext(), null, null, 1);
        ArrayList<Records> records = dbHandler.databaseToList("SELECT * from wishlist", "wishlist");
        if(records.isEmpty()){
            renameAlbumArtwork("records");
            setErrorAsCorrected();
        }
        else{
            records = dbHandler.databaseToList("SELECT * from records", "records");
            if(records.isEmpty()){
                renameAlbumArtwork("wishlist");
                setErrorAsCorrected();
            }
        }

    }


    public void albumFixMyCollection(View v) {
        showAlertDaialog("My Collection");
    }

    public void albumFixWishlist(View v) {
        showAlertDaialog("Wishlist");
    }

    public void albumFixManual(View v) {
        displayDoManuallyPopup();
    }


    public void showAlertDaialog(final String previousSelection) {

        AlertDialog.Builder builder = new AlertDialog.Builder(UpdateAlbumCoverFix.this);
        builder.setTitle("Set album art to: " + previousSelection);
        builder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        switch (previousSelection) {
                            case "My Collection":
                                renameAlbumArtwork("records");
                                break;
                            case "Wishlist":
                                renameAlbumArtwork("wishlist");
                                break;
                            default:
                                throw new IllegalArgumentException("Invalid selection in updateAlbumCoverFix of:" + previousSelection);
                        }

                    }
                }
        )
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.dismiss();
                            }
                        }
                ).create();
        builder.show();
    }

    private void renameAlbumArtwork(String tableName) {
        final MyDBHandler dbHandler = new MyDBHandler(getApplicationContext(), null, null, 1);
        String query = "SELECT * FROM " + tableName + " ORDER BY _id DESC LIMIT 0, 1";
        int maxID = Integer.valueOf(dbHandler.runRawQueryIfExists(query, "_id"));
        int fileName = 0;

        while (fileName <= maxID){
            File currentFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "/MyVinylsAlbumArt/" + String.valueOf(fileName) + ".jpg");
            File newFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "/MyVinylsAlbumArt/" + tableName + String.valueOf(fileName) + ".jpg");
            rename(currentFile, newFile);
            fileName++;
        }
     setErrorAsCorrected();
    }

    private boolean rename(File from, File to) {
        return from.getParentFile().exists() && from.exists() && from.renameTo(to);
    }

    private void displayDoManuallyPopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(UpdateAlbumCoverFix.this);
        builder.setTitle("Selected: Manual");
        builder.setMessage("Any pre-existing album art will not be visible until changed.\n" +
                "To rename manually on PC connect device to USB and navigate to /MyVinylsAlbumArt/\n" +
                "All album art for 'My Collection' edit as 'records#' \n e.g. ( 1 -> records1 )\n" +
                "All album art for 'Wishlist' edit as 'wishlist#' \n e.g (2 -> wishlist2) ");
        builder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    setErrorAsCorrected();
                    }
                }
        )
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.dismiss();
                            }
                        }
                ).create();
        builder.show();
    }

    private void setErrorAsCorrected(){
        SharedPreferences.Editor editor = getSharedPreferences("prefs", MODE_PRIVATE).edit();
        editor.putBoolean("albumCoverNameVersion2", true);
        editor.apply();
        finish();
    }
}


