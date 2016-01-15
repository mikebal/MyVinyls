package thesolocoder.solo.myvinyls;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

public class AddRecord extends AppCompatActivity{

    EditText albumName, albumYear, albumBand;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addrecord);
        setupVariables();
    }

    private void setupVariables(){
        albumName = (EditText) findViewById(R.id.editText_albumName);
        albumYear = (EditText) findViewById(R.id.editText_year);
        albumBand = (EditText) findViewById(R.id.editText_bandName);
    }

    public void addRecordClicked(View v){
        Records newRecord = new Records();
        newRecord.set_albumname(albumName.getText().toString());
        newRecord.set_releaseyear(Integer.valueOf(albumYear.getText().toString()));  // Make record contain year as a string?
        newRecord.set_bandname(albumBand.getText().toString());

        MyDBHandler dbHandler = new MyDBHandler(getApplicationContext(), null, null, 1);
        dbHandler.addRecord(newRecord);
        finish();
    }
}
