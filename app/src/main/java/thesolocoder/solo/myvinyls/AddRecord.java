package thesolocoder.solo.myvinyls;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class AddRecord extends AppCompatActivity{

    EditText albumName, albumYear, albumBand;
    ArrayList<String> genres = new ArrayList<String>();

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
        if(hasRequiredFields())
            {
                Records newRecord = new Records();
                newRecord.set_albumname(albumName.getText().toString());

                if(!albumYear.getText().toString().equals(""))
                    newRecord.set_releaseyear(Integer.valueOf(albumYear.getText().toString()));  // Make record contain year as a string?
                else
                    newRecord.set_releaseyear(-1);

                newRecord.set_bandname(albumBand.getText().toString());
                newRecord.set_genre(genres);
                MyDBHandler dbHandler = new MyDBHandler(getApplicationContext(), null, null, 1);
                dbHandler.addRecord(newRecord);
                finish();
            }
    }

    private boolean hasRequiredFields()
    {
        boolean hasAllRequiredFields = true;
        if(albumName.getText().toString().equals("")) {
            hasAllRequiredFields = false;
            TextView requiredAstrix = (TextView) findViewById(R.id.textView_RequiredAlbumName);
            requiredAstrix.setVisibility(View.VISIBLE);
        }
        return hasAllRequiredFields;
    }

    public void addGenreClicked(View v){
        Intent open_AddGenre = new Intent(AddRecord.this, AddGenre.class);
        startActivityForResult(open_AddGenre, 1);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                String returnedData = data.getStringExtra("returnKey");
                newLineStringToArrayList(returnedData);
            }
        }
    }
    private void newLineStringToArrayList(String stringToParse)
    {
        StringTokenizer tokens = new StringTokenizer(stringToParse, "\n");
        while(tokens.hasMoreTokens())
            genres.add(tokens.nextToken());
    }
}
