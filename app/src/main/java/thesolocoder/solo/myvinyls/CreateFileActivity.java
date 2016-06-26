/**
 * Copyright 2013 Google Inc. All Rights Reserved.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */

package thesolocoder.solo.myvinyls;

import android.os.Bundle;
import android.util.Log;
import android.webkit.MimeTypeMap;

import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi.DriveContentsResult;
import com.google.android.gms.drive.DriveContents;
import com.google.android.gms.drive.DriveFolder.DriveFileResult;
import com.google.android.gms.drive.MetadataChangeSet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

/**
 * An activity to illustrate how to create a file.
 */
public class CreateFileActivity extends BaseDemoActivity {

    private static final String TAG = "CreateFileActivity";

    @Override
    public void onConnected(Bundle connectionHint) {
        super.onConnected(connectionHint);
        // create new contents resource
        Drive.DriveApi.newDriveContents(getGoogleApiClient())
                .setResultCallback(driveContentsCallback);
    }

    final private ResultCallback<DriveContentsResult> driveContentsCallback = new
            ResultCallback<DriveContentsResult>() {
        @Override
        public void onResult(DriveContentsResult result) {
            if (!result.getStatus().isSuccess()) {
                showMessage("Error while trying to create new file contents");
                return;
            }
            final DriveContents driveContents = result.getDriveContents();

            // Perform I/O off the UI thread.
            new Thread() {
                @Override
                public void run() {
                    // write content to DriveContents
                    OutputStream outputStream = driveContents.getOutputStream();
                    Writer writer = new OutputStreamWriter(outputStream);
                    MyDBHandler dbHandler = new MyDBHandler(getApplicationContext(), null, null, 1);
                    String databaseLocaton = dbHandler.getDBpath();
                    String input;
                    try {
                        try {
                            File file = new File(databaseLocaton);
                            BufferedReader inputReader = new BufferedReader(new FileReader(file));
                            //BufferedReader inputReader = new BufferedReader(new InputStreamReader(getApplicationContext().openFileInput("records.db")));

                            while ((input = inputReader.readLine()) != null){
                                writer.write(input);
                            }
                            inputReader.close();
                        }
                        catch (IOException e) {}
                        writer.close();
                    } catch (IOException e) {
                        Log.e(TAG, e.getMessage());
                    }

                    //change the metadata of the file. by setting title, setMimeType.
                    String mimeType = MimeTypeMap.getSingleton().getExtensionFromMimeType("db");
                    MetadataChangeSet changeSet = new MetadataChangeSet.Builder()
                            .setTitle("recodrs")
                            .setMimeType(mimeType)
                            .build();
                  /*  MetadataChangeSet changeSet = new MetadataChangeSet.Builder()
                            .setTitle("New file")
                            .setMimeType("text/plain")
                            .setStarred(true).build();*/

                    // create a file on root folder
                    Drive.DriveApi.getRootFolder(getGoogleApiClient())
                            .createFile(getGoogleApiClient(), changeSet, driveContents)
                            .setResultCallback(fileCallback);
                }
            }.start();
        }
    };

    final private ResultCallback<DriveFileResult> fileCallback = new
            ResultCallback<DriveFileResult>() {
        @Override
        public void onResult(DriveFileResult result) {
            if (!result.getStatus().isSuccess()) {
                showMessage("Error while trying to create the file");
                return;
            }
            showMessage("Created a file with content: " + result.getDriveFile().getDriveId());
        }
    };


}
