package edu.gatech.seclass.groupimplementation.controller.event;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import java.lang.ref.WeakReference;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import edu.gatech.seclass.groupimplementation.controller.studio.DisplayStudiosActivity;
import edu.gatech.seclass.groupimplementation.model.db.DataViewModel;
import edu.gatech.seclass.groupimplementation.model.event.Event;

import edu.gatech.seclass.groupimplementation.R;
import edu.gatech.seclass.groupimplementation.model.studio.Studio;

public class DisplayEventsActivity extends AppCompatActivity {
    public List<Event> events;
    List<HashMap<String, Object>> listEvent = new ArrayList<>();
    public static Event eventSelected;
    public List<Event> eventSelectedList = new ArrayList<>();
    List<Integer> eventNumSelected = new ArrayList<>();
    public static int checkedSum = 0;
    private DataViewModel mEventViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_list);
        mEventViewModel = new ViewModelProvider(this).get(DataViewModel.class);

        //get event list from database
        initData();
    }

    public void show(){
        Context context = DisplayEventsActivity.this;
        ListView listViewEvent = (ListView) findViewById(R.id.eventlistview);
        EventListAdapter eventListAdapter = new EventListAdapter(listEvent, context);
        listViewEvent.setAdapter(eventListAdapter);
        listViewEvent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EventListAdapter.eventViewHolder holder = (EventListAdapter.eventViewHolder) view.getTag();
                holder.checkBox.toggle();
                EventListAdapter.getCheckBoxSelected().put(position, holder.checkBox.isChecked());
                if (holder.checkBox.isChecked()) {
                    checkedSum++;
                    eventNumSelected.add(0, position);
                    eventSelectedList.add(0, events.get(position));
                    if (checkedSum > 1) {
                        Toast.makeText(getApplicationContext(), "Please select only one event!",
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    checkedSum--;
                    for (int i = 0, len = eventNumSelected.size(); i < len; i++) {
                        if (position == eventNumSelected.get(i)) {
                            eventNumSelected.remove(i);
                            len--;
                            i--;
                        }
                    }
                    for (int i = 0, len = eventSelectedList.size(); i < len; i++) {
                        if (events.get(position) == eventSelectedList.get(i)) {
                            eventSelectedList.remove(i);
                            len--;
                            i--;
                        }
                    }
                }
                try{ eventSelected = eventSelectedList.get(0);}
                catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Please select one event!",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initData() {
        //get event list from database
        LiveData<List<Event>> selectedAllEvents = mEventViewModel.getAllEvents();
        selectedAllEvents.observe(this, new Observer<List<Event>>() {
            @Override
            public void onChanged(@Nullable List<Event> findEvents) {
                if(findEvents == null || findEvents.size() == 0){
                    Toast.makeText(getApplicationContext(), "No event available",
                            Toast.LENGTH_SHORT).show();
                }else{
                    events = findEvents;
                    for (int i = 0; i < findEvents.size(); i++) {
                        HashMap<String, Object> map = new HashMap<>();
                        String eventFullName = findEvents.get(i).getEventName();
                        int eventYear = findEvents.get(i).getEventYear();
                        map.put("Event Name", "Event Name: " + eventFullName);
                        map.put("Year","Year: " + eventYear);
                        listEvent.add(map);
                    }
                    show();
                }
                selectedAllEvents.removeObserver(this);
            }
        });
    }

    public void onClickEventListView(View view) {
        checkedSum = 0;
        Intent eventDetailsIntent = new Intent(DisplayEventsActivity.this, EventUIActivity.class);
        startActivity(eventDetailsIntent);
    }

    public void onClickViewSelectedEventDetails(View view) {
        checkedSum = 0;
        Intent eventDetailsIntent = new Intent(DisplayEventsActivity.this, ViewSelectedEventDetailsActivity.class);
        startActivity(eventDetailsIntent);
    }

}
