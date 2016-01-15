package thesolocoder.solo.myvinyls;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.ArrayList;


public class AddGenre extends AppCompatActivity {

    ListView genre_list;
    private static final String PRIMARY_GENRE_FILE = "#genre.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.genre);
        setupVariables();
        populateList();
 }

    private void setupVariables(){
        genre_list = (ListView) findViewById(R.id.listView_genre);
    }
    private void populateList(){

        ArrayList<String> primaryGenres; 
        GenreFileManager fileManager = new GenreFileManager(getApplicationContext());

        primaryGenres = fileManager.readInGenres(PRIMARY_GENRE_FILE);

        GenreAdapter customAdapter = new GenreAdapter(this, primaryGenres);
        genre_list.setAdapter(customAdapter);
    }
}
