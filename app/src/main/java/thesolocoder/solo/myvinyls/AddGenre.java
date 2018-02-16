package thesolocoder.solo.myvinyls;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class AddGenre extends AppCompatActivity {

    ListView genre_list;
    LinearLayout selectedView;
    TextView selectedCategoryTextView;
    ImageButton upArrow;
    GenreAdapter customAdapter;
    ArrayList<String> selectedGenres = new ArrayList<>();
    String toEditID = "-1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.genre);
        setupVariables();
        manageIfEdit();
        populateList();

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if(fab != null)
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            //    showButton(findViewById(R.id.buttonBackGround));
             //   showButton(findViewById(R.id.button2));
             //   showButton(findViewById(R.id.button3));
               // fab.setImageResource(R.mipmap.ic_clear_white_36dp);
                addNewGenre();
            }
        });
    }
    public void menuBackgroundClicked(View v){
        closeOpenMenu();
    }
    private void closeOpenMenu(){
       // hideButton(findViewById(R.id.buttonBackGround));
        //hideButton(findViewById(R.id.button2));
        //hideButton(findViewById(R.id.button3));
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if(fab!= null)
            fab.setImageResource(R.mipmap.ic_add_white_36dp);
    }
 /*   private void hideButton(View button)
    {
        if(button != null)
            button.setVisibility(View.GONE);
    }
    private void showButton(View button)
    {
        if(button != null)
            button.setVisibility(View.VISIBLE);
    }*/

    private void setupVariables(){
        genre_list = (ListView) findViewById(R.id.listView_genre);
        selectedView = (LinearLayout) findViewById(R.id.LinearLayoutSelectedCategory);
        selectedCategoryTextView = (TextView) findViewById(R.id.selectedCateogryTextView);
        upArrow = (ImageButton) findViewById(R.id.imageButton_genre_pullUp);

    }
    private void populateList(){
        MyDBHandler dbHandler = new MyDBHandler(getApplicationContext(), null, null, 1);
        LinearLayout checkboxHolder = (LinearLayout) findViewById(R.id.LinearLayoutCheckboxAreaHolder);
        ArrayList<String> primaryGenres = dbHandler.dbReturnListStrings("SELECT DISTINCT genre FROM recordsgenres where genre != '' ORDER BY genre;","genre");
        customAdapter = new GenreAdapter(this, primaryGenres, selectedView, selectedCategoryTextView, checkboxHolder, toEditID);
        genre_list.setAdapter(customAdapter);
    }

    public void closeSelectedCategoryView(View view){
        selectedView.setVisibility(View.GONE);
        genre_list.setVisibility(View.VISIBLE);
        ArrayList<String> selected = customAdapter.getCheckedItems();
        selectedGenres.addAll(selected);
        LinearLayout checkboxHolder = (LinearLayout) findViewById(R.id.LinearLayoutCheckboxAreaHolder);
        if(checkboxHolder != null){
            LinearLayout checkboxArea1 = (LinearLayout) checkboxHolder.findViewById(R.id.LinearLayoutCheckboxArea1);
            LinearLayout checkboxArea2 = (LinearLayout) checkboxHolder.findViewById(R.id.LinearLayoutCheckboxArea2);
            checkboxArea1.removeAllViews();
            checkboxArea2.removeAllViews();
        }
    }

    public void addNewGenre(){
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
                        MyDBHandler dbHandler = new MyDBHandler(getApplicationContext(), null, null, 1);
                        dbHandler.runRawQueryNoResult("insert into recordsgenres (genre,subgenre) values ('"+ value +"','');");
                        populateList();
                        closeOpenMenu();
                    } else
                        finish();
                }
            });
            alert.create();
            alert.show();
    }

    public void doneClicked(View v){
        Intent intent = new Intent();
        String returnValue = "";
        selectedGenres = customAdapter.getCheckedItems();
        for(int i = 0; i < selectedGenres.size(); i++)
        {
            returnValue += selectedGenres.get(i) + "\n";
        }
        intent.putExtra("returnKey", returnValue);
        setResult(RESULT_OK,intent);
        finish();
    }
    private void manageIfEdit(){
        Bundle extras = getIntent().getExtras();
        String _id = extras.getString("toEditID");
        if(_id == null || _id.equals("New Entry"))
            return;
        toEditID = _id;
        if(!toEditID.equals("-1")) {
            Button button = (Button) findViewById(R.id.button4);
            if(button != null)
                button.setVisibility(View.GONE);
        }
    }
}
