package thesolocoder.solo.myvinyls;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class AddRecord extends AppCompatActivity {

    EditText albumName, albumYear, albumBand;
    ImageButton albumArtwork;
    Bitmap albumCover;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addrecord);
        setupVariables();
    }

    private void setupVariables() {
        albumName = (EditText) findViewById(R.id.editText_albumName);
        albumYear = (EditText) findViewById(R.id.editText_year);
        albumBand = (EditText) findViewById(R.id.editText_bandName);
        albumArtwork = (ImageButton) findViewById(R.id.imageButton);
    }

    public void addRecordClicked(View v) {
        String  newAlbumID;
        if (hasRequiredFields()) {
            Records newRecord = new Records();
            newRecord.set_albumname(albumName.getText().toString());

            if (!albumYear.getText().toString().equals(""))
                newRecord.set_releaseyear(Integer.valueOf(albumYear.getText().toString()));  // Make record contain year as a string?
            else
                newRecord.set_releaseyear(-1);
            newRecord.set_bandname(albumBand.getText().toString());

            MyDBHandler dbHandler = new MyDBHandler(getApplicationContext(), null, null, 1);
            newAlbumID = dbHandler.addRecord(newRecord);

            saveImageToFile(albumCover, newAlbumID);
            finish();
        }
    }

    private boolean hasRequiredFields() {
        boolean hasAllRequiredFields = true;
        if (albumName.getText().toString().equals("")) {
            hasAllRequiredFields = false;
            TextView requiredAstrix = (TextView) findViewById(R.id.textView_RequiredAlbumName);
            requiredAstrix.setVisibility(View.VISIBLE);
        }
        return hasAllRequiredFields;
    }

    public void addGenreClicked(View v) {
        Intent open_AddGenre = new Intent(AddRecord.this, AddGenre.class);
        startActivityForResult(open_AddGenre, 1);
    }

    public void albumCoverClicked(View v) {
        selectImage();
    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(AddRecord.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, 666);
                } else if (items[item].equals("Choose from Library")) {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(
                            Intent.createChooser(intent, "Select File"),
                            777);
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 666) {
                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
                File destination = new File(Environment.getExternalStorageDirectory(),
                        System.currentTimeMillis() + ".jpg");
                FileOutputStream fo;
                try {
                    destination.createNewFile();
                    fo = new FileOutputStream(destination);
                    fo.write(bytes.toByteArray());
                    fo.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                albumArtwork.setImageBitmap(thumbnail);
            } else if (requestCode == 777) {

                Uri selectedImageUri = data.getData();
                try {
                    albumCover = getCorrectlyOrientedImage(getApplicationContext(), selectedImageUri);
                    albumArtwork.setImageBitmap(albumCover);
                   // saveImageToFile(albumCover);
                }
               catch (Exception e){

               }
            }
        }
    }

    public static int getOrientation(Context context, Uri photoUri) {
    /* it's on the external media. */
        Cursor cursor = context.getContentResolver().query(photoUri,
                new String[] { MediaStore.Images.ImageColumns.ORIENTATION }, null, null, null);

        if (cursor.getCount() != 1) {
            return -1;
        }

        cursor.moveToFirst();
        return cursor.getInt(0);
    }

    public static Bitmap getCorrectlyOrientedImage(Context context, Uri photoUri) throws IOException {
        int MAX_IMAGE_DIMENSION = 150;
        InputStream is = context.getContentResolver().openInputStream(photoUri);
        BitmapFactory.Options dbo = new BitmapFactory.Options();
        dbo.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(is, null, dbo);
        is.close();

        int rotatedWidth, rotatedHeight;
        int orientation = getOrientation(context, photoUri);

        if (orientation == 90 || orientation == 270) {
            rotatedWidth = dbo.outHeight;
            rotatedHeight = dbo.outWidth;
        } else {
            rotatedWidth = dbo.outWidth;
            rotatedHeight = dbo.outHeight;
        }

        Bitmap srcBitmap;
        is = context.getContentResolver().openInputStream(photoUri);
        if (rotatedWidth > MAX_IMAGE_DIMENSION || rotatedHeight > MAX_IMAGE_DIMENSION) {
            float widthRatio = ((float) rotatedWidth) / ((float) MAX_IMAGE_DIMENSION);
            float heightRatio = ((float) rotatedHeight) / ((float) MAX_IMAGE_DIMENSION);
            float maxRatio = Math.max(widthRatio, heightRatio);

            // Create the bitmap from file
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = (int) maxRatio;
            srcBitmap = BitmapFactory.decodeStream(is, null, options);
        } else {
            srcBitmap = BitmapFactory.decodeStream(is);
        }
        is.close();

    /*
     * if the orientation is not 0 (or -1, which means we don't know), we
     * have to do a rotation.
     */
        if (orientation > 0) {
            Matrix matrix = new Matrix();
            matrix.postRotate(orientation);

            srcBitmap = Bitmap.createBitmap(srcBitmap, 0, 0, srcBitmap.getWidth(),
                    srcBitmap.getHeight(), matrix, true);
        }

        return srcBitmap;
    }

    private void saveImageToFile(Bitmap image, String fileName){
        createDirIfNotExists("MyVinylsAlbumArt");
        File f = new File(Environment.getExternalStorageDirectory()
                + File.separator + "/MyVinylsAlbumArt/" + fileName + ".jpg");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        try {
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            fo.close();
        }
        catch (Exception e){}
    }
    public static boolean createDirIfNotExists(String path) {
        boolean ret = true;

        File file = new File(Environment.getExternalStorageDirectory(), path);
        if (!file.exists()) {
            if (!file.mkdirs()) {
                Log.e("TravellerLog :: ", "Problem creating Image folder");
                ret = false;
            }
        }
        return ret;
    }
}
