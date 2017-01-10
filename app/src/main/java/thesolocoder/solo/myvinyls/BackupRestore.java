package thesolocoder.solo.myvinyls;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

public class BackupRestore extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences preferences = getSharedPreferences("prefs", MODE_PRIVATE);
        boolean useDarkTheme = preferences.getBoolean("dark_theme", false);
        if(useDarkTheme)
            setTheme(R.style.AppTheme_Dark);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.backuprestore);

        if(useDarkTheme){
            LinearLayout layout = (LinearLayout) findViewById(R.id.backupRestoreLayout);
            layout.setBackgroundColor(Color.DKGRAY);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem menuItem;
        menuItem=menu.add("Title"); //your desired title here
        menuItem.setIcon(R.mipmap.ic_info_outline_white_48dp); //your desired icon here
        menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent showImportExplanation = new Intent(BackupRestore.this, ImportExplanation.class);
        startActivity(showImportExplanation);
        return super.onOptionsItemSelected(item);
    }

    public void backupClicked(View v){
        Intent startBackup = new Intent(BackupRestore.this, CreateFileActivity.class);
        startActivity(startBackup);
        finish();
    }

    public void restoreClicked(View v){
        Intent startRestore = new Intent(BackupRestore.this, RetrieveContentsWithProgressDialogActivity.class);
        startActivity(startRestore);
        finish();
    }
}
