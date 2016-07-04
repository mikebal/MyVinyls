package thesolocoder.solo.myvinyls;

import android.content.Context;
import android.widget.Toast;
import com.google.android.gms.drive.DriveContents;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class FileImporter {

    public void acceptDriveVersion(DriveContents contents, Context context){

        final int BAND_NAME = 0;
        final int ALBUM_NAME = 1;
        final int RELEASE_YEAR = 2;
        BufferedReader reader = new BufferedReader(new InputStreamReader(contents.getInputStream()));
        StringBuilder builder = new StringBuilder();
        String line;
        String tableName = "records";
        ArrayList<String> lineParsed;
        Records newRecord;

        MyDBHandler dbHandler = new MyDBHandler(context, null, null, 1);
        try {
            while ((line = reader.readLine()) != null) {
                builder.setLength(0);
                builder.append(line);
                lineParsed = lineToColumb(builder.toString());

                if(lineParsed.size() == 1) {
                    if (lineParsed.get(0).toLowerCase().equals("records") || lineParsed.get(0).toLowerCase().equals("wishlist")) {
                        tableName = lineParsed.get(0).toLowerCase();
                        dbHandler.runRawQueryNoResult("DELETE * FROM " + tableName + ";");
                    }
                }
                else{
                    lineParsed = lineToColumb(builder.toString());
                    newRecord = new Records();
                    newRecord.set_bandname(lineParsed.get(BAND_NAME));
                    newRecord.set_albumname(lineParsed.get(ALBUM_NAME));
                    newRecord.set_releaseyear(lineParsed.get(RELEASE_YEAR));

                    lineParsed.remove(RELEASE_YEAR);
                    lineParsed.remove(ALBUM_NAME);
                    lineParsed.remove(BAND_NAME);

                    newRecord.set_genre(insertUserParentGenre(lineParsed));

                    dbHandler.addRecord(newRecord, tableName, tableName+"genres");
                }
            }
        }
        catch (Exception e){
            Toast toast = Toast.makeText(context, e.getClass().getName(), Toast.LENGTH_LONG);
            toast.show();
        }
    }

    private ArrayList lineToColumb(String stringToParse)
    {
        ArrayList<String> lineSegments = new ArrayList();
        StringTokenizer tokens = new StringTokenizer(stringToParse, ",");
        while(tokens.hasMoreTokens()) {
            lineSegments.add(tokens.nextToken());
        }
        return lineSegments;
    }
    private ArrayList insertUserParentGenre(ArrayList<String> genres)
    {
        ArrayList<String> formatedList = new ArrayList<>();
        final String parentGenre = "User";

        for(int i = 0; i < genres.size(); i++)
        {
            formatedList.add(parentGenre);
            formatedList.add(genres.get(i));
        }
        return formatedList;
    }
}
