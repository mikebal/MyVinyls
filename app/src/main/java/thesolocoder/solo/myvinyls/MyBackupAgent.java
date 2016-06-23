package thesolocoder.solo.myvinyls;

import android.app.backup.BackupAgent;
import android.app.backup.BackupAgentHelper;
import android.app.backup.BackupDataInput;
import android.app.backup.BackupDataOutput;
import android.app.backup.FileBackupHelper;
import android.app.backup.SharedPreferencesBackupHelper;
import android.os.ParcelFileDescriptor;
import android.util.Log;

import java.io.IOException;

/**
 * Created by Michael on 6/21/2016.
 */
public class MyBackupAgent extends BackupAgentHelper {
        // The name of the file
        static final String TOP_SCORES = "scores";
        static final String PLAYER_STATS = "stats";

        // A key to uniquely identify the set of backup data
        static final String FILES_BACKUP_KEY = "myfiles";

        // Allocate a helper and add it to the backup agent
        @Override
        public void onCreate() {

            MyDBHandler dbHandler = new MyDBHandler(getApplicationContext(), null, null, 1);
            String dataBaseLocation = dbHandler.getDBpath();
            dbHandler.close();
            FileBackupHelper helper = new FileBackupHelper(this,dataBaseLocation);
            addHelper(FILES_BACKUP_KEY, helper);
        }


    @Override
    public void onBackup(ParcelFileDescriptor oldState, BackupDataOutput data,
                         ParcelFileDescriptor newState) throws IOException {
        // Hold the lock while the FileBackupHelper performs backup
       // synchronized (MyDBHandler.sDataLock) {
            super.onBackup(oldState, data, newState);
        //}
    }

    @Override
    public void onRestore(BackupDataInput data, int appVersionCode,
                          ParcelFileDescriptor newState) throws IOException {
        // Hold the lock while the FileBackupHelper restores the file
      //  synchronized (MyDBHandler.sDataLock) {
            super.onRestore(data, appVersionCode, newState);
       // }
    }
}
