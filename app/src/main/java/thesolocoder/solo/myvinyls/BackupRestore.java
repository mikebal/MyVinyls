package thesolocoder.solo.myvinyls;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Michael on 6/24/2016.
 */
public class BackupRestore extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addrecord);
       // setupVariables();
       // manageIfEdit();
        Bundle extras = getIntent().getExtras();
       // dbTableReferenced = extras.getString("toAddToTable");
    }
}
