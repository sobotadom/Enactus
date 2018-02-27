package enactus.budget.enactus;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;



/**
 * Created by Daniel on 2/23/2018.
 */

public class About extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_page);

        TextView t1 = (TextView) findViewById(R.id.aboutDescription);
        t1.setMovementMethod(LinkMovementMethod.getInstance());

        ImageButton enactusButton =  findViewById(R.id.enactusButton);
        enactusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent webLink = new Intent(Intent.ACTION_VIEW);
                webLink.setData(Uri.parse("http://www.enactusyorku.ca/"));
                startActivity(webLink);
            }
        });
    }


}
