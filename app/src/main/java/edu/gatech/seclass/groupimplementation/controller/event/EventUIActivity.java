package edu.gatech.seclass.groupimplementation.controller.event;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.concurrent.ExecutionException;

import edu.gatech.seclass.groupimplementation.ConnectionMode;
import edu.gatech.seclass.groupimplementation.MainActivity;
import edu.gatech.seclass.groupimplementation.R;
import edu.gatech.seclass.groupimplementation.model.event.Event;
import edu.gatech.seclass.groupimplementation.model.db.DataViewModel;
import edu.gatech.seclass.groupimplementation.model.studio.Studio;

public class EventUIActivity extends AppCompatActivity {

    private EditText eventTypeText;
    private EditText eventNameText;
    private EditText eventYearText;
    private EditText eventDurationText;
    private EditText eventProduceStudioText;
    private EditText eventLicenseFeeText;
    private DataViewModel mEventViewModel;

    // jump to event tab from main menu
    @Override
    protected void onCreate(Bundle enterCurrentEvent) {
        super.onCreate(enterCurrentEvent);
        setContentView(R.layout.event_details);
        eventTypeText = findViewById(R.id.detailedEventTypeID);
        eventNameText = findViewById(R.id.detailedEventNameID);
        eventYearText = findViewById(R.id.detailedEventYearID);
        eventDurationText = findViewById(R.id.detailedEventDurationID);
        eventProduceStudioText = findViewById(R.id.eventProduceStudioDetailsID);
        eventLicenseFeeText = findViewById(R.id.eventLicenseFeeDetailsID);
        mEventViewModel = new ViewModelProvider(this).get(DataViewModel.class);
    }

    // execute display event in another UI page
    public void onClickDisplayEvents(View view) {
        Intent displayEventIntent = new Intent(EventUIActivity.this, DisplayEventsActivity.class);
        startActivity(displayEventIntent);
    }

    // save action: check if complete and save to database --------------------------------
    public void save() {
        String eventType = eventTypeText.getText().toString();
        String eventFullName = eventNameText.getText().toString();
        String eventYear = eventYearText.getText().toString();
        String eventDuration = eventDurationText.getText().toString();
        String eventProduceStudio = eventProduceStudioText.getText().toString();
        String eventLicenseFee = eventLicenseFeeText.getText().toString();
        String eventID = eventFullName + eventYear;

        if(ConnectionMode.connection){
            if (eventType.equals("") || eventFullName.equals("") || eventYear.equals("") || eventProduceStudio.equals("") || eventLicenseFee.equals("") || eventDuration.equals("")) {
                Toast.makeText(getApplicationContext(), "Please fill in all the information", Toast.LENGTH_SHORT).show();
            } else {
                try{
                    LiveData<Event> selectedEvent = mEventViewModel.findEventById(eventFullName + eventYear);
                    selectedEvent.observe(this, new Observer<Event>() {
                        @Override
                        public void onChanged(@Nullable Event event) {
                            if(event == null){
                                Event newEvent = new Event(eventType, eventFullName, Integer.parseInt(eventYear), Integer.parseInt(eventDuration), eventProduceStudio, Integer.parseInt(eventLicenseFee));
                                mEventViewModel.insert(newEvent);
                                Toast.makeText(getApplicationContext(), "Event successfully created!", Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(getApplicationContext(), "Event of this short name already exists!", Toast.LENGTH_SHORT).show();
                            }
                            selectedEvent.removeObserver(this);
                        }
                    });
                }catch(NumberFormatException e) {
                    Toast.makeText(getApplicationContext(),"Event year, duration and license fee should be integers!",Toast.LENGTH_LONG).show();
                }
            }
        }else{
            Toast.makeText(getApplicationContext(), R.string.Internet, Toast.LENGTH_LONG).show();
        }
    }

    public void onClickCreateBack(View view) {
        switch (view.getId()) {
            case R.id.create_event:
                save();
                break;
            case R.id.eventDetails_back:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
        }
    }

}
