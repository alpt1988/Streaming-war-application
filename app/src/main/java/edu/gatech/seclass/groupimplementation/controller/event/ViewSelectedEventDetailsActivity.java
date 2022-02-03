package edu.gatech.seclass.groupimplementation.controller.event;

import android.app.Activity;
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
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModelProvider;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.concurrent.ExecutionException;

import edu.gatech.seclass.groupimplementation.ConnectionMode;
import edu.gatech.seclass.groupimplementation.R;
import edu.gatech.seclass.groupimplementation.controller.stream.StreamUIActivity;
import edu.gatech.seclass.groupimplementation.controller.stream.StreamUpdate;
import edu.gatech.seclass.groupimplementation.model.demo.DemoGroup;
import edu.gatech.seclass.groupimplementation.model.demo.DemoGroupStream;
import edu.gatech.seclass.groupimplementation.model.event.Event;
import edu.gatech.seclass.groupimplementation.model.db.DataViewModel;
import edu.gatech.seclass.groupimplementation.model.studio.Studio;

public class ViewSelectedEventDetailsActivity extends AppCompatActivity {

    Event event;
    private EditText eventTypeText;
    private EditText eventNameText;
    private EditText eventYearText;
    private EditText eventDurationText;
    private EditText eventProduceStudioText;
    private EditText eventLicenseFeeText;
    private DataViewModel mEventViewModel;

    @Override
    protected void onCreate(Bundle savedEventDetailsState) {
        super.onCreate(savedEventDetailsState);
        setContentView(R.layout.detailed_event_view);

        eventTypeText = findViewById(R.id.detailedEventTypeID);
        eventNameText = findViewById(R.id.detailedEventNameID);
        eventYearText = findViewById(R.id.detailedEventYearID);
        eventDurationText = findViewById(R.id.detailedEventDurationID);
        eventProduceStudioText = findViewById(R.id.eventProduceStudioDetailsID);
        eventLicenseFeeText = findViewById(R.id.eventLicenseFeeDetailsID);
        mEventViewModel = new ViewModelProvider(this).get(DataViewModel.class);

        try {
            event = new GetSelectedEvent(ViewSelectedEventDetailsActivity.this).execute().get();
        }
        catch (InterruptedException | ExecutionException e) {
            Toast.makeText(getApplicationContext(), "Error populating event details",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public static class GetSelectedEvent extends AsyncTask<Void, Void, Event> {
        private WeakReference<Context> context;

        public GetSelectedEvent(Context context) {
            this.context = new WeakReference<>(context);
        }

        @Override
        protected Event doInBackground(Void... voids) {
            return DisplayEventsActivity.eventSelected;
        }

        @Override
        protected void onPostExecute(Event event) {
            super.onPostExecute(event);

            if (event == null) {
                return;
            }
            EditText eventTypeEditText = ((Activity) context.get()).findViewById(R.id.detailedEventTypeID);
            EditText eventNameDetailsEditText = ((Activity) context.get()).findViewById(R.id.detailedEventNameID);
            EditText eventYearDetailsEditText = ((Activity) context.get()).findViewById(R.id.detailedEventYearID);
            EditText eventDurationDetailsEditText = ((Activity) context.get()).findViewById(R.id.detailedEventDurationID);
            EditText eventProduceStudioDetailsEditText = ((Activity) context.get()).findViewById(R.id.eventProduceStudioDetailsID);
            EditText eventLicenseFeeDetailsEditText = ((Activity) context.get()).findViewById(R.id.eventLicenseFeeDetailsID);

            eventTypeEditText.setText(event.getEventType());
            eventNameDetailsEditText.setText(event.getEventName());
            eventYearDetailsEditText.setText(String.valueOf(event.getEventYear()));
            eventDurationDetailsEditText.setText(String.valueOf(event.getEventDuration()));
            eventProduceStudioDetailsEditText.setText(event.getEventStudioOwner());
            eventLicenseFeeDetailsEditText.setText(String.valueOf(event.getEventLicenseFee()));

        }
    }

    // save updated event information
    public void save() {
        String eventType = eventTypeText.getText().toString();
        String eventFullName = eventNameText.getText().toString();
        String eventYear = eventYearText.getText().toString();
        String eventDuration = eventDurationText.getText().toString();
        String eventProduceStudio = eventProduceStudioText.getText().toString();
        String eventLicenseFee = eventLicenseFeeText.getText().toString();

        if(ConnectionMode.connection){
            if (eventType.equals("") || eventFullName.equals("") || eventYear.equals("") || eventProduceStudio.equals("") || eventLicenseFee.equals("") || eventDuration.equals("")) {
                Toast.makeText(getApplicationContext(), "Please fill in all the information", Toast.LENGTH_SHORT).show();
            } else {
                try{
                    int temp = Integer.parseInt(eventYear) + Integer.parseInt(eventDuration) + Integer.parseInt(eventLicenseFee);
                }catch(NumberFormatException e) {
                    Toast.makeText(getApplicationContext(),"Event year, duration and license fee should be integers!",Toast.LENGTH_LONG).show();
                }
                LiveData<Event> selectedEvent = mEventViewModel.findEventById(eventFullName + eventYear);
                selectedEvent.observe(this, new Observer<Event>() {
                    @Override
                    public void onChanged(@Nullable Event event) {
                        if(event == null){
                            Toast.makeText(getApplicationContext(), "Cannot find the event in the database!", Toast.LENGTH_SHORT).show();
                        }else {
                            LiveData<List<DemoGroupStream>> selectedAllDemoStreams = mEventViewModel.getAllDemoGroupStreams();
                            LiveData<List<DemoGroupStream>> liveDemoGroupStream = Transformations.switchMap(selectedEvent, (x) -> {
                                return selectedAllDemoStreams;
                            });
                            liveDemoGroupStream.observe(ViewSelectedEventDetailsActivity.this, new Observer<List<DemoGroupStream>>() {
                                @Override
                                public void onChanged(List<DemoGroupStream> demoGroupStreams) {
                                    boolean watched = false;
                                    if (demoGroupStreams != null && demoGroupStreams.size() > 0) {
                                        for (DemoGroupStream temp : demoGroupStreams) {
                                            if (temp.getDemoGroupStreamKey().contains(eventFullName + "+" + eventYear)) {
                                                watched = true;   // already watched, cannot change
                                            }
                                        }
                                    }
                                    if (!watched) {
                                        mEventViewModel.updateEvent(eventFullName+eventYear,eventDuration,eventLicenseFee);
                                        Toast.makeText(getApplicationContext(),"Event updated successfully!",Toast.LENGTH_LONG).show();
                                    }else{
                                        if(event.getEventLicenseFee() != Integer.parseInt(eventLicenseFee)){
                                            Toast.makeText(getApplicationContext(), "License fee cannot change because it has watched events!", Toast.LENGTH_SHORT).show();
                                        }else{
                                            mEventViewModel.updateEvent(eventFullName+eventYear,eventDuration,eventLicenseFee);
                                            Toast.makeText(getApplicationContext(),"Event updated successfully!",Toast.LENGTH_LONG).show();
                                        }
                                    }
                                    liveDemoGroupStream.removeObserver(this);
                                }
                            });
                        }
                        selectedEvent.removeObserver(this);
                    }
                });
            }
        }else {
            Toast.makeText(getApplicationContext(), R.string.Internet, Toast.LENGTH_LONG).show();
        }
    }

    public void onClickEventDetailsUpdateBack(View view) {
        switch (view.getId()) {
            case R.id.update_eventDetails:
                save();
                break;
            case R.id.eventDetails_back:
                Intent eventDetailsIntent = new Intent(ViewSelectedEventDetailsActivity.this, DisplayEventsActivity.class);
                startActivity(eventDetailsIntent);
                break;
        }
    }


}
