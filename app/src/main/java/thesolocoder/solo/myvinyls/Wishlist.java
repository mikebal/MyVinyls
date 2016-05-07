package thesolocoder.solo.myvinyls;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ListView;

/**
 * Created by Michael on 4/28/2016.
 */
public class Wishlist extends AppCompatActivity {
    MyDBHandler dbHandler;
    ListView recordDisplayList;
    ListViewAdapterMain customAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wishlist);

        final Animation animRotate = AnimationUtils.loadAnimation(this, R.anim.anim_rotate);
        //  dbHandler = new MyDBHandler(getApplicationContext(), null, null, 1);
        //  populateArrayList("SELECT * FROM records ORDER BY albumname;");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(animRotate);
                //   Intent open_AddRecords = new Intent(MainActivity.this, AddRecord.class);
                //  open_AddRecords.putExtra("toEditID", String.valueOf("New Entry"));
                // startActivity(open_AddRecords);
            }
        });


        //inputSearch = (EditText) findViewById(R.id.editTextSearchBox);
        //setListClickListener();
    }


}
