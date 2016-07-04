package thesolocoder.solo.myvinyls;

import android.content.Context;

import java.util.ArrayList;

public class FileExportHelper {

    public String getRecordData(Context context){
        String data = "";
        MyDBHandler dbHandler = new MyDBHandler(context, null, null, 1);
        ArrayList<Records> recordList = dbHandler.databaseToList("SELECT * FROM records ORDER BY bandname");
        data = convertListToString(recordList);
        return data;
    }

    private String convertListToString(ArrayList<Records> records)
    {
        String data = "";
        for (int i = 0; i < records.size(); i++)
        {
            data += records.get(i).get_bandname() + ",";
            data += records.get(i).get_albumname() + ",";
            data += records.get(i).get_releaseyear();
            data += listToCommaString(records.get(i).get_genre()) + "\n";
        }
        return data;
    }

    private String listToCommaString(ArrayList<String> genres)
    {
        String commaString = "";
        for(int i = 0; i < genres.size(); i++)
            commaString += "," + genres.get(i);

        return commaString;
    }
}
