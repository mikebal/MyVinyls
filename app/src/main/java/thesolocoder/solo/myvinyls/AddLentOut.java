package thesolocoder.solo.myvinyls;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;

public class AddLentOut extends AppCompatActivity {
    String recordID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lendout);
        Bundle extras = getIntent().getExtras();
        String _id = extras.getString("toEditID");
        if(_id == null || _id.equals("New Entry"))
            return;
        recordID = _id;
    }

    public void lentOutDone(View v){
        EditText name = (EditText) findViewById(R.id.editTextLendoutName);
        if(name != null) {
            String nameStr = name.getText().toString();
            if (nameStr.equals(""))
                return;
        }
        MyDBHandler dbHandler = new MyDBHandler(getApplicationContext(), null, null, 1);
        manageDuplicate(dbHandler);
    }

    private int findLargestString(ArrayList<String> list)
    {
        int highest = 1;
        int current;
        for(int i = 0; i < list.size(); i++)
        {
            current = Integer.valueOf(list.get(i));
            if(current > highest)
                highest = current;
        }
        return highest;
    }
    private void manageDuplicate(final MyDBHandler dbHandler){
        String result = dbHandler.runRawQueryIfExists("SELECT * FROM lentout WHERE album_id='" + recordID +"'", "album_id");
        if(!result.equals("-1")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle("Duplicate Lent Out");
            builder.setMessage("This record is show as already out, would you like override?");

            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dbHandler.runRawQueryNoResult("DELETE FROM " + "lentout WHERE album_id=\"" + recordID + "\";");
                    dialog.dismiss();
                    lendOutRecord(dbHandler);
                }
            });

            builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Do nothing
                    dialog.dismiss();
                }
            });

            AlertDialog alert = builder.create();
            alert.show();
        }
        else
            lendOutRecord(dbHandler);
    }
    private void lendOutRecord(MyDBHandler dbHandler){

        int currentID = 1;
        EditText name = (EditText) findViewById(R.id.editTextLendoutName);
        EditText dateOut = (EditText) findViewById(R.id.editTextLentoutDateOut);
        EditText dateDue = (EditText) findViewById(R.id.editTextLentoutDue);

        if(name == null || dateDue == null || dateOut == null)
            return;

        String nameStr = name.getText().toString();
        ArrayList<String> listOfIDs = dbHandler.dbReturnListStrings("SELECT * FROM lentout", "_id");
        if (listOfIDs != null && listOfIDs.size() != 0)
            currentID += findLargestString(listOfIDs);
        String currentIDStr = String.valueOf(currentID);
        String dateOutStr = dateOut.getText().toString();
        String dateDueStr = dateDue.getText().toString();
        dbHandler.runRawQueryNoResult("insert into lentout (_id,album_id,lentout,dateout,dueback) values (" + currentIDStr + "," + recordID + ",'" + nameStr + "','" + dateOutStr + "','" + dateDueStr + "');");
        finish();
    }
}
