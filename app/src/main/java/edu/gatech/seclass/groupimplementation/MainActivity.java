package edu.gatech.seclass.groupimplementation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import edu.gatech.seclass.groupimplementation.controller.demogroup.DemoGroupUIActivity;
import edu.gatech.seclass.groupimplementation.controller.event.EventUIActivity;
import edu.gatech.seclass.groupimplementation.controller.offerevent.OfferEventUIActivity;
import edu.gatech.seclass.groupimplementation.controller.stream.StreamUIActivity;
import edu.gatech.seclass.groupimplementation.controller.studio.StudioUIActivity;
import edu.gatech.seclass.groupimplementation.model.db.DataViewModel;
import edu.gatech.seclass.groupimplementation.model.db.GlobalArchiveRoomDatabase;
import edu.gatech.seclass.groupimplementation.model.db.GlobalRoomDatabase;
import edu.gatech.seclass.groupimplementation.model.demo.DemoGroup;

import android.widget.CompoundButton;
import android.util.Log;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button enterEventButton;
    Button enterOffButton;
    private DataViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);
        if(ConnectionMode.connection){
            Button p1_button = (Button)findViewById(R.id.connectionMode);
            p1_button.setText("Online");
        }else{
            Button p1_button = (Button)findViewById(R.id.connectionMode);
            p1_button.setText("Offline");
        }
    }



    public void onClickOpenEventUI(View view) {
        Intent openEventUIIntent = new Intent(MainActivity.this, EventUIActivity.class);
        MainActivity.this.startActivity(openEventUIIntent);
    }

    public void onClickStreamUI(View view){
        Intent intent = new Intent(this, StreamUIActivity.class);
        startActivity(intent);
    }

    public void onClickTimeUI(View view){
        Intent intent = new Intent(this, TimeDetails.class);
        startActivity(intent);
    }

	public void onClickOpenStudioUI(View view) {
        Intent openStudioUIIntent = new Intent(MainActivity.this, StudioUIActivity.class);
        MainActivity.this.startActivity(openStudioUIIntent);
    }

    public void onClickOpenDemoGroupUI(View view) {
        Intent openDemoGroupUIIntent = new Intent(MainActivity.this, DemoGroupUIActivity.class);
        MainActivity.this.startActivity(openDemoGroupUIIntent);
    }

    public void onClickOpenOfferEventUI(View view) {
        Intent OpenOfferUIIntent = new Intent(MainActivity.this, OfferEventUIActivity.class);
        MainActivity.this.startActivity(OpenOfferUIIntent);
    }

    public void onClickConnectionMode(View view){
        String button_text;
        button_text = ((Button) view).getText().toString();
        if(button_text.equals("Offline")){
            Log.d("Test scenario: ","connected.");
            ConnectionMode.connection = true;
            Button p1_button = (Button)findViewById(R.id.connectionMode);
            p1_button.setText("Online");
        }else{
            Log.d("Test scenario: ","disconnected.");
            ConnectionMode.connection = false;
            Button p1_button = (Button)findViewById(R.id.connectionMode);
            p1_button.setText("Offline");
        }
    }
}