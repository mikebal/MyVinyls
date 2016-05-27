package thesolocoder.solo.myvinyls;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Michael on 5/27/2016.
 */
public class RecordReturn extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lentoutreview);

        Bundle extras = getIntent().getExtras();
        String recordID = extras.getString("toAddToTable");
    }

}
