package thesolocoder.solo.myvinyls;

import android.content.Context;

import java.util.ArrayList;

public class FileExportHelper {

    public String getRecordData(Context context, String tableName){
        String data;
        MyDBHandler dbHandler = new MyDBHandler(context, null, null, 1);
        ArrayList<Records> recordList = dbHandler.databaseToList("SELECT * FROM " + tableName + " ORDER BY bandname", tableName);
        data = convertRecordListToString(recordList, tableName);
        return data;
    }

    private String convertRecordListToString(ArrayList<Records> records, String tableName)
    {
        String data = "";
        for (int i = 0; i < records.size(); i++)
        {
            data += "<" + tableName + "\\>";
            data += "<ID_URL\\>" + records.get(i).get_imageurl() + "</ID_URL>";
            data += "<BAND_NAME\\>" + records.get(i).get_bandname() + "</BAND_NAME>";
            data += "<ALBUM\\>" + records.get(i).get_albumname() +"</ALBUM>";
            data += "<YEAR\\>" +  records.get(i).get_releaseyear() + "</YEAR>";
            data += "<SIZE\\>" + records.get(i).get_size() + "</SIZE>";
            data += "<NOTE\\>" + records.get(i).get_notes() + "</NOTE>";
            data += "<GENRE\\>" + listToCommaString(records.get(i).get_genre()) + "</GENRE>";
            data += "</" + tableName + ">\n";
        }
        return data;
    }

    public String getLentOutData(Context context){
        String data;
        MyDBHandler dbHandler = new MyDBHandler(context, null, null, 1);
        ArrayList<LentOut> lentOutList = dbHandler.getLentOut("SELECT * FROM lentout ORDER BY album_id");
        data = convertLentOutListToString(lentOutList);
        return data;
    }

    private String convertLentOutListToString(ArrayList<LentOut> lentout){
        String data = "";
        for(int i = 0; i < lentout.size(); i++){
            data += lentout.get(i).id + ",";
            data += lentout.get(i).name + ",";
            data += lentout.get(i).dateOutStr + ",";
            data += lentout.get(i).duebackStr + "\n";
        }
        return data;
    }

    private String listToCommaString(ArrayList<String> list)
    {
        String commaString = "";
        if(list.size() > 0) {
            commaString += list.get(0);
            for (int i = 1; i < list.size(); i++) {
                if(list.get(i) != null) {
                    commaString += "," + list.get(i);
                }
            }
        }
        return commaString;
    }
}
