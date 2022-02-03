package edu.gatech.seclass.groupimplementation.controller.stream;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import edu.gatech.seclass.groupimplementation.MainActivity;
import edu.gatech.seclass.groupimplementation.R;

public class StreamDisplay extends AppCompatActivity {

    private EditText shortName;
    private EditText longName;
    private EditText subscriptionPrice;
    private EditText licensing;
    private EditText currentperiod;
    private EditText previousperiod;
    private EditText total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_stream);
        shortName = (EditText) findViewById(R.id.shortnameshow);
        longName = (EditText) findViewById(R.id.longnameshow);
        subscriptionPrice = (EditText) findViewById(R.id.subscriptionshow);
        licensing = (EditText) findViewById(R.id.licensingshow);
        currentperiod = (EditText) findViewById(R.id.currentperiodshow);
        previousperiod = (EditText) findViewById(R.id.previousperiodshow);
        total = (EditText) findViewById(R.id.totalshow);

        // show in the xml
        Intent info = getIntent();
        String shortname = info.getExtras().getString("shortname_output");
        String longname = info.getExtras().getString("longname_output");
        String subscription = info.getExtras().getString("subscription");
        String licensefee = info.getExtras().getString("stream_licensing");
        String current = info.getExtras().getString("currentperiod");
        String previous = info.getExtras().getString("previousperiod");
        String totalperiod = info.getExtras().getString("totalperiod");

        shortName.setText(shortname);
        longName.setText(longname);
        subscriptionPrice.setText(subscription);
        licensing.setText(licensefee);
        currentperiod.setText(current);
        previousperiod.setText(previous);
        total.setText(totalperiod);
    }

    // button's click function
    public void handleClick(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
