package thesolocoder.solo.myvinyls;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import java.io.File;
import java.io.IOException;

public class DeleteAllImages extends Activity {

    private static final String PREFS_NAME = "prefs";
    private static final String PREF_DARK_THEME = "dark_theme";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean useDarkTheme = preferences.getBoolean(PREF_DARK_THEME, false);
        if(useDarkTheme)
            setTheme(R.style.AppTheme_Dark_NoActionBar);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.deleteallimages);
    }

    public void warningUnderstood(View view){
        CheckBox userUnderstandsConfirmation = (CheckBox) findViewById(R.id.deleteImagesCheckBox);
        Button confirmationButton = (Button) findViewById(R.id.deleteAllArtworkNowButton);
        if(userUnderstandsConfirmation.isChecked()) {
            confirmationButton.setVisibility(View.VISIBLE);
        }
        else
        {
            confirmationButton.setVisibility(View.GONE);
        }
    }

    public void deleteAllImagesNow(View v){
       String path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "/MyVinylsAlbumArt/";
        File file = new File(path);

        if (file.exists()) {
            String deleteCmd = "rm -r " + path;
            Runtime runtime = Runtime.getRuntime();
            try {
                runtime.exec(deleteCmd);
            } catch (IOException e) { }
        }
        finish();
    }

    public void closeActivity(View v){
        finish();
    }
}
