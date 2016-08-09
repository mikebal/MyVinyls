package thesolocoder.solo.myvinyls;

import android.content.Context;
import android.widget.Toast;
import com.google.android.gms.drive.DriveContents;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class FileImporter {

    public String acceptDriveVersion(DriveContents contents, Context context){

        BufferedReader reader = new BufferedReader(new InputStreamReader(contents.getInputStream()));
        StringBuilder builder = new StringBuilder();
        String line;
        String tableName = "records";
        ArrayList<String> lineParsed;
        int lentOutID = 1;
        int lineCount = 1;
        ArrayList<String> badLineList = new ArrayList<>();
        String errorMessage = "";

        MyDBHandler dbHandler = new MyDBHandler(context, null, null, 1);
        try {
            dbHandler.dropAndRemake();
            while ((line = reader.readLine()) != null) {
                builder.setLength(0);
                builder.append(line);
                lineParsed = lineToColumb(builder.toString());

                if(lineParsed.size() == 1) {
                    if (lineParsed.get(0).toLowerCase().equals("wishlist") || lineParsed.get(0).toLowerCase().equals("lentout")) {
                        tableName = lineParsed.get(0).toLowerCase();
                    }
                    else{
                        if(!lineParsed.get(0).contains("records")) {    // correction for possible special symbole if edited by google drive
                            Toast toast = Toast.makeText(context, "Record Category is wrong: " + lineParsed.get(0), Toast.LENGTH_LONG);
                            toast.show();
                            break;
                        }
                    }
                }
                else {
                    lineParsed = lineToColumb(builder.toString());
                    if (isValidFormat(lineParsed)) {
                        if (!tableName.equals("lentout"))
                            addRecord(lineParsed, tableName, dbHandler);
                         else {
                            addLentOut(lentOutID, lineParsed, dbHandler);
                            lentOutID++;
                        }
                    }
                    else{
                         badLineList.add(String.valueOf(lineCount));
                    }
                }
                lineCount++;
            }
            if(badLineList.size() >= 1)
            {
                errorMessage = "The Records on the following lines have been ignored:\n";
                for(int i = 0; i < badLineList.size(); i++)
                {
                    if(i != 0)
                        errorMessage += ",";
                    errorMessage += String.valueOf(badLineList.get(i));
                }
            }
        }
        catch (Exception e){
            Toast toast = Toast.makeText(context, e.getClass().getName(), Toast.LENGTH_LONG);
            toast.show();
        }
        return errorMessage;
    }

    private void addRecord(ArrayList<String> lineParsed, String tableName, MyDBHandler dbHandler){
        Records newRecord;
        final int _ID = 0;
        final int BAND_NAME = 1;
        final int ALBUM_NAME = 2;
        final int RELEASE_YEAR = 3;

        newRecord = new Records();
        newRecord.set_imageurl(lineParsed.get(_ID));
        if(!lineParsed.get(BAND_NAME).equals("#RECORDFEILDMISSING#"))
            newRecord.set_bandname(lineParsed.get(BAND_NAME));
        newRecord.set_albumname(lineParsed.get(ALBUM_NAME));
        if(!lineParsed.get(RELEASE_YEAR).equals("#RECORDFEILDMISSING#"))
            newRecord.set_releaseyear(lineParsed.get(RELEASE_YEAR));

        lineParsed.remove(RELEASE_YEAR);
        lineParsed.remove(ALBUM_NAME);
        lineParsed.remove(BAND_NAME);
        lineParsed.remove(_ID);

        newRecord.set_genre(lineParsed);
        dbHandler.addRecord(newRecord, tableName, tableName+"genres", true);
    }

    private void addLentOut(int id, ArrayList<String> lineParsed, MyDBHandler dbHandler)
    {
        String idString = String.valueOf(id);
        while (lineParsed.size() < 4)
            lineParsed.add("");
        dbHandler.runRawQueryNoResult("insert into lentout (_id,album_id,lentout,dateout,dueback) values (" + idString + "," + lineParsed.get(0) + ",'" + lineParsed.get(1)
                + "','" + lineParsed.get(2) + "','" + lineParsed.get(3) + "');");
    }

    private ArrayList lineToColumb(String stringToParse)
    {
        ArrayList<String> lineSegments = new ArrayList<>();
        StringTokenizer tokens = new StringTokenizer(stringToParse, ",");
        while(tokens.hasMoreTokens()) {
            lineSegments.add(tokens.nextToken());
        }
        return lineSegments;
    }

    private boolean isValidFormat(ArrayList<String> data)
    {
        if(data.size() < 3)
            if(data.get(3).equals("#RECORDFEILDMISSING#"))
                return true;

        boolean isValid = true;
       if(data.size() < 3)
            isValid = false;
        if(isValid)
            isValid = isInteger(data.get(0));
        if(isValid)
            isValid = isInteger(data.get(3));
        return isValid;
    }
    public static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch(NumberFormatException e) {
            return false;
        } catch(NullPointerException e) {
            return false;
        }
        // only got here if we didn't return false
        return true;
    }
}
