package thesolocoder.solo.myvinyls;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;

/**
 * Created by Michael on 1/21/2016.
 */
public class AddSubGenrePopup extends AppCompatActivity {

    Spinner primaryCategorySpinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addsubgenrepopup);
        primaryCategorySpinner = (Spinner) findViewById(R.id.spinner);
        populateSpinner();
    }
    private void populateSpinner(){
        GenreFileManager fileManager = new GenreFileManager(getApplicationContext());
        ArrayList<String> genres = fileManager.readInGenres("#genre.txt");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, genres);
        primaryCategorySpinner.setAdapter(adapter);
    }
    public void addSubGenre(View v){
        EditText subGenreInput = (EditText) findViewById(R.id.editTextNewSubGenre);
        String input = subGenreInput.getText().toString();
        if(input.equals("") || input.charAt(0) == '#')
            finish();

        String filename = "#" + primaryCategorySpinner.getSelectedItem().toString() + ".txt";

        GenreFileManager fileManager = new GenreFileManager(getApplicationContext());
        fileManager.addAlphabetically(input, filename);
        finish();
    }
}
