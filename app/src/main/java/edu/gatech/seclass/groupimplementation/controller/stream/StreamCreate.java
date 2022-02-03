package edu.gatech.seclass.groupimplementation.controller.stream;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import edu.gatech.seclass.groupimplementation.MainActivity;
import edu.gatech.seclass.groupimplementation.R;

public class StreamCreate extends AppCompatActivity {

    private EditText shortName;
    private EditText longName;
    private EditText subscriptionPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_stream);
        shortName = (EditText) findViewById(R.id.shortnameshow);
        longName = (EditText) findViewById(R.id.longnameshow);
        subscriptionPrice = (EditText) findViewById(R.id.subscriptionshow);

        // receive from stream details to show success
        Bundle info = getIntent().getExtras();
        String shortNames = info.getString("shortname_output");
        String longNames = info.getString("longname_output");
        String subscription = info.getString("subscription");
        shortName.setText(shortNames);
        longName.setText(longNames);
        subscriptionPrice.setText(subscription);
    }

    // button's click function
    public void handleClick(View view){
        // back to main interface
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}
