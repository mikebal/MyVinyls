package thesolocoder.solo.myvinyls;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class BackupRestore extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.backuprestore);
    }

    public void backupClicked(View v){
        Intent startBackup = new Intent(BackupRestore.this, CreateFileActivity.class);
        startActivity(startBackup);
    }

    public void restoreClicked(View v){
        Intent startRestore = new Intent(BackupRestore.this, RetrieveContentsWithProgressDialogActivity.class);
        startActivity(startRestore);
    }
}
