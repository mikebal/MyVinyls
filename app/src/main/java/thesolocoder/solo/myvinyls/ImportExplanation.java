package thesolocoder.solo.myvinyls;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

/**
 * Created by Michael on 8/19/2016.
 */
public class ImportExplanation extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.importexplanation);
        // 1) How to replace link by text like "Click Here to visit Google" and
        // the text is linked with the website url ?
        TextView link = (TextView) findViewById(R.id.textViewSampleDoc);
        String linkText = "View the <a href='https://drive.google.com/file/d/0B73GrIAmmg0nNGFUclZfSGswZm8/view?usp=sharing'>Sample file</a> on Drive.";
        link.setText(Html.fromHtml(linkText));
        link.setMovementMethod(LinkMovementMethod.getInstance());


    }
}
