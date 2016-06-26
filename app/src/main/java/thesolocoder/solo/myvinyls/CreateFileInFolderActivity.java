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

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi.DriveContentsResult;
import com.google.android.gms.drive.DriveApi.DriveIdResult;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.DriveFolder.DriveFileResult;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.MetadataChangeSet;


/**
 * An activity to create a file inside a folder.
 */
public class CreateFileInFolderActivity extends BaseDemoActivity {

    private DriveId mFolderDriveId;

    @Override
    public void onConnected(Bundle connectionHint) {
        super.onConnected(connectionHint);
        SharedPreferences savedData;
        savedData = getSharedPreferences("savedData", 0);// save data
        String folder_id = savedData.getString("FOLDER_ID", "ERROR");

        if(folder_id.equals("ERROR"))
        {
            Intent open_AddRecords = new Intent(CreateFileInFolderActivity.this, CreateFileInFolderActivity.class);
            startActivity(open_AddRecords);
            folder_id = savedData.getString("FOLDER_ID", "ERROR");
        }

        if(!folder_id.equals("ERROR"))
        Drive.DriveApi.fetchDriveId(getGoogleApiClient(), EXISTING_FOLDER_ID)
                .setResultCallback(idCallback);
    }

    final private ResultCallback<DriveIdResult> idCallback = new ResultCallback<DriveIdResult>() {
        @Override
        public void onResult(DriveIdResult result) {
            if (!result.getStatus().isSuccess()) {
                showMessage("Cannot find DriveId. Are you authorized to view this file?");
                return;
            }
            mFolderDriveId = result.getDriveId();
            Drive.DriveApi.newDriveContents(getGoogleApiClient())
                    .setResultCallback(driveContentsCallback);
        }
    };

    final private ResultCallback<DriveContentsResult> driveContentsCallback =
            new ResultCallback<DriveContentsResult>() {
        @Override
        public void onResult(DriveContentsResult result) {
            if (!result.getStatus().isSuccess()) {
                showMessage("Error while trying to create new file contents");
                return;
            }
            DriveFolder folder = mFolderDriveId.asDriveFolder();
            MetadataChangeSet changeSet = new MetadataChangeSet.Builder()
                    .setTitle("New file in folder")
                    .setMimeType("text/plain")
                    .setStarred(true).build();
            folder.createFile(getGoogleApiClient(), changeSet, result.getDriveContents())
                    .setResultCallback(fileCallback);
            finish();
        }
    };

    final private ResultCallback<DriveFileResult> fileCallback =
            new ResultCallback<DriveFileResult>() {
        @Override
        public void onResult(DriveFileResult result) {
            if (!result.getStatus().isSuccess()) {
                showMessage("Error while trying to create the file");
                return;
            }
            SharedPreferences savedData;
            savedData = getSharedPreferences("savedData", 0);// save data
            SharedPreferences.Editor editor = savedData.edit();
            editor.putString("FOLDER_ID", result.getDriveFile().getDriveId().toString());
            finish();
            return;
           // showMessage("Created a file: " + result.getDriveFile().getDriveId());
        }
    };
}
