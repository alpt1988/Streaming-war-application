package edu.gatech.seclass.groupimplementation.controller.event;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;


import java.util.HashMap;
import java.util.List;

import edu.gatech.seclass.groupimplementation.R;

public class EventListAdapter extends BaseAdapter {
    private LayoutInflater eventLayoutInflater;
    private List<HashMap<String, Object>> events;
    private Context context;
    public static HashMap<Integer, Boolean> checkBoxSelected;

    public EventListAdapter(List<HashMap<String, Object>> events, Context context) {
        this.events = events;
        this.context = context;
        this.eventLayoutInflater = LayoutInflater.from(context);
        checkBoxSelected = new HashMap<Integer, Boolean>();
        initCheckBoxSelected();
    }
    public static HashMap<Integer, Boolean> getCheckBoxSelected() {
        return checkBoxSelected;
    }

    public static void setCheckBoxSelected(HashMap<Integer, Boolean> checkBoxSelected) {
        EventListAdapter.checkBoxSelected = checkBoxSelected;
    }
    private void initCheckBoxSelected() {
        for (int i = 0; i < events.size(); i++) {
            getCheckBoxSelected().put(i, false);
        }
    }

    @Override
    public int getCount() {
        return events.size();
    }

    @Override
    public Object getItem(int position) {
        return events.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        eventViewHolder holder = null;
        if (convertView == null) {
            convertView = eventLayoutInflater.inflate(R.layout.eventlistview, parent, false);
            holder = new eventViewHolder();
            holder.eventFullName = convertView.findViewById(R.id.eventFullName);
            holder.eventYear = convertView.findViewById(R.id.eventYear);
            holder.checkBox = (CheckBox) convertView.findViewById(R.id.checkBox);
            convertView.setTag(holder);
        } else {
            holder = (eventViewHolder) convertView.getTag();
        }
        holder.eventFullName.setText((String) events.get(position).get("Event Name"));
        holder.eventYear.setText((String) events.get(position).get("Year"));
        holder.checkBox.setChecked(getCheckBoxSelected().get(position));
        return convertView;
    }

    static class eventViewHolder {
        public TextView eventFullName;
        public TextView eventYear;
        public CheckBox checkBox;
    }
}

