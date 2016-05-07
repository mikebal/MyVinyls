package thesolocoder.solo.myvinyls;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;

/**
 * Created by Michael on 4/28/2016.
 */
public class AddLentOut extends AppCompatActivity {

    ArrayList<String> recordIDs = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lendout);
    }
    public void lentoutDone(View v){
        EditText name = (EditText) findViewById(R.id.editTextLendoutName);
        EditText dateOut = (EditText) findViewById(R.id.editTextLentoutDateOut);
        EditText dateDue = (EditText) findViewById(R.id.editTextLentoutDue);
        String nameStr = name.getText().toString();
        String dateOutStr = dateOut.getText().toString();
        String dateDueSter = dateDue.getText().toString();

        finish();
    }
}
