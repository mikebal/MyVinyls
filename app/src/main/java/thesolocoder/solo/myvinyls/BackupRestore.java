package thesolocoder.solo.myvinyls;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.util.ArrayList;

public class BackupRestore extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.backuprestore);
    }

    public void backupClicked(View v){
        Intent startBackup = new Intent(BackupRestore.this, CreateFileActivity.class);
        startActivity(startBackup);
        finish();
    }

    public void restoreClicked(View v){
        Intent startRestore = new Intent(BackupRestore.this, RetrieveContentsWithProgressDialogActivity.class);
        startActivity(startRestore);
    /*    MyDBHandler dbHandler = new MyDBHandler(getApplicationContext(), null, null, 1);
        FileImporter importer = new FileImporter();
        ArrayList<String> parsed = importer.lineToColumb("10,#EMPTY#,lies,#EMPTY#,,");
        if(importer.isValidFormat(parsed))
            importer.addRecord(parsed, "records", dbHandler);*/
        finish();
    }
}
