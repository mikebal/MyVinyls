package thesolocoder.solo.myvinyls;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
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
    GenreAdapter customAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.genre);
        setupVariables();
        populateList();


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Button   button = (Button) findViewById(R.id.buttonBackGround);
                button.setVisibility(View.VISIBLE);
                button = (Button) findViewById(R.id.button2);
                button.setVisibility(View.VISIBLE);
                button = (Button) findViewById(R.id.button3);
                button.setVisibility(View.VISIBLE);
            }
        });

 }
    public void menuBackgroundClicked(View v){
        Button   button = (Button) findViewById(R.id.buttonBackGround);
        button.setVisibility(View.GONE);
        button = (Button) findViewById(R.id.button2);
        button.setVisibility(View.GONE);
        button = (Button) findViewById(R.id.button3);
        button.setVisibility(View.GONE);
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

        LinearLayout checkboxHolder = (LinearLayout) findViewById(R.id.LinearLayoutCheckboxAreaHolder);

        primaryGenres = fileManager.readInGenres(PRIMARY_GENRE_FILE);

        customAdapter = new GenreAdapter(this, primaryGenres, selectedView, selectedCategoryTextView, checkboxHolder);
        genre_list.setAdapter(customAdapter);

    }

    public void closeSelectedCategoryView(View view){
        selectedView.setVisibility(View.GONE);
        genre_list.setVisibility(View.VISIBLE);
        ArrayList<String> selected = customAdapter.getCheckedItems();
        //selectedCategoryTextView.setText(selected.get(0));
        //selectedCategoryTextView.setVisibility(View.VISIBLE);
    }
    public void addSubGenreClicked(View v)
    {
        Intent open_AddSubGenrePopup = new Intent(AddGenre.this, AddSubGenrePopup.class);
        startActivityForResult(open_AddSubGenrePopup, 1);
    }
    public void addNewGenre(View v){



            final AlertDialog.Builder alert = new AlertDialog.Builder(this);

            alert.setTitle("Adding Genre!");
            alert.setMessage("Enter new Genre:");

            final EditText input = new EditText(this);

        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            alert.setView(input);

            alert.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    String value = input.getText().toString();
                    if (!value.equals("")) {
                        GenreFileManager genreManager = new GenreFileManager(getApplicationContext());
                        genreManager.addAlphabetically(value, "#genre.txt");

                    } else
                        finish();

                }
            });
            alert.create();
            alert.show();
    }

}
