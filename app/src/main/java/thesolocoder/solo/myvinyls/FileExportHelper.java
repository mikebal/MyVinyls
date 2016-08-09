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
        String temp = "";
        for (int i = 0; i < records.size(); i++)
        {
            data += records.get(i).get_imageurl() + ",";
            temp =  records.get(i).get_bandname();
            if(temp.isEmpty() || temp.equals(""))
                temp = "#RECORDFEILDMISSING#,";
            else
                temp += ",";
            data += temp;
            data += records.get(i).get_albumname() + ",";

            temp += records.get(i).get_releaseyear();
            if(temp.isEmpty() || temp.equals(""))
                temp = "#RECORDFEILDMISSING#,";
            else
                temp += ",";
            data += temp;
            data += listToCommaString(records.get(i).get_genre()) + "\n";
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
        for(int i = 0; i < list.size(); i++)
            commaString += "," + list.get(i);

        return commaString;
    }
}
