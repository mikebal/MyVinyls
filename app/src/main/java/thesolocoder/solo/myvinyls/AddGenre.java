package thesolocoder.solo.myvinyls;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

// TO-DO
// When expanded category hit: - Cut the current list view to end and click possition
//                             - Show checkbox list for that category
//                             - Create populate secondary listView with the remainder of the first list view
//                             - On click outside of Step 2 (dynamic check box area) recreate single list;

public class AddGenre extends AppCompatActivity {

    ListView genre_list;
    LinearLayout selectedView;
    TextView selectedCategoryTextView;
    ImageButton upArrow;
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
        selectedView = (LinearLayout) findViewById(R.id.LinearLayoutSelectedCategory);
        selectedCategoryTextView = (TextView) findViewById(R.id.selectedCateogryTextView);
        upArrow = (ImageButton) findViewById(R.id.imageButton_genre_pullUp);
    }
    private void populateList(){

        ArrayList<String> primaryGenres;
        GenreFileManager fileManager = new GenreFileManager(getApplicationContext());

        primaryGenres = fileManager.readInGenres(PRIMARY_GENRE_FILE);

        GenreAdapter customAdapter = new GenreAdapter(this, primaryGenres, selectedView, selectedCategoryTextView);
        genre_list.setAdapter(customAdapter);

    }

    public void closeSelectedCategoryView(View view){
        selectedView.setVisibility(View.GONE);
        genre_list.setVisibility(View.VISIBLE);
    }






}
