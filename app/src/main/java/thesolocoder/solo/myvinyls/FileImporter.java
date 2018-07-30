package thesolocoder.solo.myvinyls;

import android.content.Context;
import android.widget.Toast;
import com.google.android.gms.drive.DriveContents;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class FileImporter {

    final String[] _backupRecordTypes = {"records", "wishlist", "lentout"};
    final String[] _requiredTags = {"ID_URL", "ALBUM"};
    final String[] _recordTags = {"BAND_NAME", "YEAR", "SIZE", "NOTE", "GENRE"};


    public String acceptDriveVersionNew(DriveContents contents, Context context) {

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
                line = builder.toString();

                processBackup(line, context);
            }
        }
                  catch (Exception e){
                Toast toast = Toast.makeText(context, e.getClass().getName(), Toast.LENGTH_LONG);
                toast.show();
            }
            return errorMessage;
        }
                /*if(lineParsed.size() == 1) {
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

    }*/

    public String acceptDriveVersionOld(DriveContents contents, Context context){

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

    public void processBackup(String line, Context context){
        if(verifyRecordIsOfKnownType(line) && recordHasRequiredFields(line)){
            Records newRecord = createRecordFromBackupEntry(line);
            MyDBHandler myDBHandler = new MyDBHandler(context, null, null, 1);
            myDBHandler.dropAndRemake();
            myDBHandler.addRecord(newRecord, "records", "recordsgenres", true);
        }
    }

    /**
     *   final String[] _backupRecordTypes = {"records", "wishlist", "lentout"};
     final String[] _requiredTags = {"ID_URL", "ALBUM"};
     final String[] _recordTags = {"BAND_NAME", "YEAR", "SIZE", "NOTE", "GENRE"};
     * @param line
     * @return
     */
    private Records createRecordFromBackupEntry(String line){
        Records newRecordFromBackup = new Records();
        newRecordFromBackup.set_id(getStringBetweenTags(_requiredTags[0], line));
        newRecordFromBackup.set_albumname(getStringBetweenTags(_requiredTags[1], line));

        newRecordFromBackup.set_bandname(getStringBetweenTags(_recordTags[0], line));
        newRecordFromBackup.set_releaseyear(getStringBetweenTags(_recordTags[1], line));
        newRecordFromBackup.set_size(getStringBetweenTags(_recordTags[2], line));
        newRecordFromBackup.set_notes(getStringBetweenTags(_recordTags[3], line));
        newRecordFromBackup.set_imageurl(getStringBetweenTags(_requiredTags[0], line));
        newRecordFromBackup.set_genre(lineToColumb(getStringBetweenTags(_recordTags[4], line)));
        return newRecordFromBackup;
    }

    boolean verifyRecordIsOfKnownType(String record){
        boolean validated = false;

        for (String type: _backupRecordTypes){
            BackupHeaderTrailer tagFormat = new BackupHeaderTrailer(type);
            if(record.startsWith(tagFormat.getHeaderTag()) && record.endsWith(tagFormat.getTrailerTag() + "\n")){
                validated = true;
            }
        }
        return validated;
    }

    private boolean recordHasRequiredFields(String line){

        boolean validated = true;

        for (String tag: _requiredTags){
            BackupHeaderTrailer requiredTag = new BackupHeaderTrailer(tag);

            if(!line.contains(requiredTag.getHeaderTag()) && !line.contains(requiredTag.getTrailerTag())
                    || (line.indexOf(requiredTag.getHeaderTag()) > line.indexOf(requiredTag.getTrailerTag()))){
                validated = false;
            }
            if(validated){
                String requiredValue = getStringBetweenTags(tag, line);
                if(requiredValue == null || requiredValue.isEmpty()){
                    validated = false;
                }
            }
        }
        return validated;
    }

    private String getStringBetweenTags(String searchTag, String line){
        String value = null;
        BackupHeaderTrailer tag = new BackupHeaderTrailer(searchTag);

        if(line.indexOf(tag.getHeaderTag()) < line.indexOf(tag.getTrailerTag())){
            value = line.substring(line.indexOf(tag.getHeaderTag()) + tag.getHeaderTag().length(), line.indexOf(tag.getTrailerTag()));
        }
        return value;
    }


    public void addRecord(ArrayList<String> lineParsed, String tableName, MyDBHandler dbHandler){
        Records newRecord;
        final int _ID = 0;
        final int BAND_NAME = 1;
        final int ALBUM_NAME = 2;
        final int RELEASE_YEAR = 3;
        final int RECORD_SIZE = 4;

        newRecord = new Records();
        newRecord.set_imageurl(lineParsed.get(_ID));
        if(lineParsed.get(BAND_NAME).equals("#EMPTY#"))
            newRecord.set_bandname("");
        else
            newRecord.set_bandname(lineParsed.get(BAND_NAME));
        newRecord.set_albumname(lineParsed.get(ALBUM_NAME));
        if(lineParsed.get(RELEASE_YEAR).equals("#EMPTY#"))
            newRecord.set_releaseyear("");
        else
            newRecord.set_releaseyear(lineParsed.get(RELEASE_YEAR));

        if(isValidRecordSize(lineParsed.get(RECORD_SIZE))){
            newRecord.set_size(lineParsed.get(RECORD_SIZE) + "\"");
        } else{
            newRecord.set_size(" ");
        }


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

    public ArrayList lineToColumb(String stringToParse)
    {
        ArrayList<String> lineSegments = new ArrayList<>();
        StringTokenizer tokens = new StringTokenizer(stringToParse, ",");
        while(tokens.hasMoreTokens()) {
            lineSegments.add(tokens.nextToken());
        }
        return lineSegments;
    }

    public boolean isValidFormat(ArrayList<String> data)
    {
        if(data.size() < 3)
            if(data.get(3).equals("#EMPTY#"))
                return true;

        boolean isValid = true;
       if(data.size() < 3)
            isValid = false;
        if(isValid)
            isValid = isInteger(data.get(0));
        if(isValid && !data.get(3).equals("#EMPTY#"))
            isValid = isInteger(data.get(3));
        return isValid;
    }
    private static boolean isInteger(String s) {
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

    private boolean isValidRecordSize(String size){
        boolean isValid = true;
        if(size == null || size.isEmpty()){
            isValid = false;
        } else if( !size.equals("7") || !size.equals("10") || !size.equals("12")){
            isValid = false;
        }

        return isValid;
    }
}
