package edu.gatech.seclass.groupimplementation.controller.stream;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModelProvider;
import androidx.appcompat.app.AppCompatActivity;

import edu.gatech.seclass.groupimplementation.ConnectionMode;
import edu.gatech.seclass.groupimplementation.MainActivity;
import edu.gatech.seclass.groupimplementation.R;
import edu.gatech.seclass.groupimplementation.controller.demogroup.DemoDetailsActivity;
import edu.gatech.seclass.groupimplementation.controller.demogroup.DemoDisplayActivity;
import edu.gatech.seclass.groupimplementation.model.demo.DemoGroup;
import edu.gatech.seclass.groupimplementation.model.demo.DemoGroupStream;
import edu.gatech.seclass.groupimplementation.model.stream.Stream;
import edu.gatech.seclass.groupimplementation.model.db.DataViewModel;

public class StreamUIActivity extends AppCompatActivity {

    private EditText shortName;
    private EditText longName;
    private EditText subscriptionPrice;
    private DataViewModel mStreamViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stream_details);
        // initialize the objects
        shortName = (EditText) findViewById(R.id.shortNameID);
        longName = (EditText) findViewById(R.id.longNameID);
        subscriptionPrice = (EditText) findViewById(R.id.subscription);
        mStreamViewModel = new ViewModelProvider(this).get(DataViewModel.class);
    }

    // button's click function
    public void handleClick(View view){
        String button_text;
        button_text = ((Button) view).getText().toString();
        // create a streaming service
        if(button_text.equals("create")){
            String shortNames = shortName.getText().toString();
            String longNames = longName.getText().toString();
            String subscriptionPrices = subscriptionPrice.getText().toString();
            if(ConnectionMode.connection){
                // pass to streamDisplay to store
                if (shortNames.equals("")){  // need a non-empty shortName
                    Toast.makeText(getApplicationContext(),R.string.miss_shortname,Toast.LENGTH_LONG).show();
                }else if(longNames.equals("")){ // need a non-empty longName
                    Toast.makeText(getApplicationContext(),R.string.miss_longname,Toast.LENGTH_LONG).show();
                }else{
                    if (subscriptionPrices.equals("")) {
                        subscriptionPrices = "0";
                    }
                    try { // subscription number not correct
                        // save in database and trigger stream create
                        save(shortNames, longNames, Integer.parseInt(subscriptionPrices));
                    }catch(NumberFormatException e) {
                        Toast.makeText(getApplicationContext(),R.string.number_format,Toast.LENGTH_LONG).show();
                    }
                }
            }else{
                Toast.makeText(getApplicationContext(), R.string.Internet, Toast.LENGTH_LONG).show();
            }
        }
        // display the info of a given streaming service
        else if(button_text.equals("display stream")){
            String shortNames = shortName.getText().toString();
            if (shortNames.equals("")){  // need a non-empty shortName
                Toast.makeText(getApplicationContext(),R.string.miss_shortname,Toast.LENGTH_LONG).show();
            }else {
                LiveData<Stream> selectedStream = mStreamViewModel.findStreamByShortName(shortNames);
                selectedStream.observe(this, new Observer<Stream>() {
                    @Override
                    public void onChanged(@Nullable Stream stream) {
                        if(stream == null){
                            Toast.makeText(getApplicationContext(),R.string.no_instance,Toast.LENGTH_LONG).show();
                        }else {
                            Intent intent = new Intent(StreamUIActivity.this, StreamDisplay.class);
                            intent.putExtra("shortname_output", shortNames);
                            intent.putExtra("longname_output", stream.getStreamLongName());
                            intent.putExtra("subscription", String.valueOf(stream.getStreamSubscription()));
                            intent.putExtra("stream_licensing", String.valueOf(stream.getStreamLicensing()));
                            intent.putExtra("currentperiod", String.valueOf(stream.getStreamCurrentRevenue()));
                            intent.putExtra("previousperiod", String.valueOf(stream.getStreamPreviousRevenue()));
                            intent.putExtra("totalperiod", String.valueOf(stream.getStreamTotalRevenue()));
                            startActivity(intent);
                        }
                        selectedStream.removeObserver(this);
                    }
                });
            }
        }
        // update the information of streaming service
        else if(button_text.equals("update stream")){
            String shortNames = shortName.getText().toString();
            String longNames = longName.getText().toString();
            String subscriptionPrices = subscriptionPrice.getText().toString();
            if(ConnectionMode.connection){
                if (shortNames.equals("")){
                    Toast.makeText(getApplicationContext(),R.string.miss_shortname,Toast.LENGTH_LONG).show();
                }else{
                    try { // subscription number not correct
                        if(!subscriptionPrices.equals("")){  // if not empty, try convert to integer
                            Integer.parseInt(subscriptionPrices);
                        }
                    }catch(NumberFormatException e) {
                        Toast.makeText(getApplicationContext(),R.string.number_format,Toast.LENGTH_LONG).show();
                    }
                    LiveData<Stream> selectedStream = mStreamViewModel.findStreamByShortName(shortNames);
                    selectedStream.observe(this, new Observer<Stream>() {
                        @Override
                        public void onChanged(@Nullable Stream stream) {
                            if(stream == null){
                                Toast.makeText(getApplicationContext(),R.string.no_instance,Toast.LENGTH_LONG).show();
                            }else{
                                LiveData<List<DemoGroupStream>> selectedAllDemoStreams = mStreamViewModel.getAllDemoGroupStreams();
                                LiveData<List<DemoGroupStream>> liveDemoGroupStream = Transformations.switchMap(selectedStream, (x) -> {
                                    return selectedAllDemoStreams;
                                });
                                liveDemoGroupStream.observe(StreamUIActivity.this, new Observer<List<DemoGroupStream>>() {
                                    @Override
                                    public void onChanged(List<DemoGroupStream> demoGroupStreams) {
                                        boolean watched = false;
                                        if (demoGroupStreams != null && demoGroupStreams.size() > 0) {
                                            for (DemoGroupStream temp : demoGroupStreams) {
                                                if (temp.getDemoGroupStreamKey().contains(shortNames)) {
                                                    watched = true;   // already watched, cannot change
                                                }
                                            }
                                        }
                                        String shortNames = shortName.getText().toString();
                                        String longNames = longName.getText().toString();
                                        String subscriptionPrices = subscriptionPrice.getText().toString();
                                        if (!watched) {
                                            Intent intent = new Intent(StreamUIActivity.this, StreamUpdate.class);
                                            intent.putExtra("shortname_output", shortNames);
                                            if (longNames.equals("")) {
                                                intent.putExtra("longname_output", stream.getStreamLongName());
                                                longNames = stream.getStreamLongName();
                                            } else {
                                                intent.putExtra("longname_output", longNames);
                                            }
                                            if (subscriptionPrices.equals("")) {
                                                intent.putExtra("subscription", String.valueOf(stream.getStreamSubscription()));
                                                subscriptionPrices = String.valueOf(stream.getStreamSubscription());
                                            } else {
                                                intent.putExtra("subscription", subscriptionPrices);
                                            }
                                            mStreamViewModel.updateStream(shortNames, longNames, subscriptionPrices);
                                            startActivity(intent);
                                        }else{
                                            if (subscriptionPrices.equals("")) {  // does not change subscription price
                                                Intent intent = new Intent(StreamUIActivity.this, StreamUpdate.class);
                                                intent.putExtra("shortname_output", shortNames);
                                                if (longNames.equals("")) {
                                                    intent.putExtra("longname_output", stream.getStreamLongName());
                                                    longNames = stream.getStreamLongName();
                                                } else {
                                                    intent.putExtra("longname_output", longNames);
                                                }
                                                intent.putExtra("subscription", String.valueOf(stream.getStreamSubscription()));
                                                subscriptionPrices = String.valueOf(stream.getStreamSubscription());
                                                mStreamViewModel.updateStream(shortNames, longNames, subscriptionPrices);
                                                startActivity(intent);
                                            }else{
                                                Toast.makeText(getApplicationContext(), "Subscription price cannot change because it has watched events!", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                        liveDemoGroupStream.removeObserver(this);
                                    }
                                });
                            }
                            selectedStream.removeObserver(this);
                        }
                    });
                }
            }else{
                Toast.makeText(getApplicationContext(), R.string.Internet, Toast.LENGTH_LONG).show();
            }
        }
        // back to main
        else if(button_text.equals("back to main")){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }

    public void save(String shortName,String longName,int subscriptionPrice) {
        LiveData<Stream> selectedStream = mStreamViewModel.findStreamByShortName(shortName);
        selectedStream.observe(this, new Observer<Stream>() {
            @Override
            public void onChanged(@Nullable Stream stream) {
                if(stream == null){
                    Stream newStream = new Stream(shortName,longName,subscriptionPrice);
                    mStreamViewModel.insert(newStream);
                    Intent intent = new Intent(StreamUIActivity.this, StreamCreate.class);
                    intent.putExtra("shortname_output", shortName);
                    intent.putExtra("longname_output",longName);
                    intent.putExtra("subscription",String.valueOf(subscriptionPrice));
                    startActivity(intent);
                }else {
                    Toast.makeText(getApplicationContext(), "Streaming service of this short name already exists!", Toast.LENGTH_SHORT).show();
                }
                selectedStream.removeObserver(this);
            }
        });
    }
}

