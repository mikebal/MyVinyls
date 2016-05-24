package thesolocoder.solo.myvinyls;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by Michael on 5/17/2016.
 */
public class LendoreditPopup extends AppCompatActivity{
    String targetURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lendoreditpopup);
        Bundle extras = getIntent().getExtras();
        targetURL = extras.getString("toEditID");
    }

    public void switchViewToLendOut(View v){
        Intent open_EditRecords = new Intent(LendoreditPopup.this, AddLentOut.class);
        open_EditRecords.putExtra("toEditID", targetURL);
        startActivity(open_EditRecords);
        finish();
    }

    public void switchViewToEdit(View v){
        Intent open_EditRecords = new Intent(LendoreditPopup.this, AddRecord.class);
        open_EditRecords.putExtra("toEditID", targetURL);
        startActivity(open_EditRecords);
        finish();
    }
}
