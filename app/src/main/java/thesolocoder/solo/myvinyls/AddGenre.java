package thesolocoder.solo.myvinyls;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by Michael on 1/14/2016.
 */
public class AddGenre extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.genre);
        ArrayList<String> myList = new ArrayList<>();
        myList.add("Test 1");
        myList.add("Test 2");

        ListView genre_list = (ListView) findViewById(R.id.listView_genre);
        GenreAdapter customAdapter = new GenreAdapter(this, myList);
        genre_list.setAdapter(customAdapter);

    }
}
