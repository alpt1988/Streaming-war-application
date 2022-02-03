package edu.gatech.seclass.groupimplementation.controller.studio;

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
import edu.gatech.seclass.groupimplementation.model.studio.Studio;

public class ListAdapter extends BaseAdapter {

    private LayoutInflater eventLayoutInflater;
    private List<HashMap<String, Object>> studios;
    private Context context;
    public static HashMap<Integer, Boolean> checkBoxSelected;

    public ListAdapter(List<HashMap<String, Object>> studios, Context context) {

        this.studios = studios;
        this.context = context;
        this.eventLayoutInflater = LayoutInflater.from(context);
        checkBoxSelected = new HashMap<Integer, Boolean>();
        initCheckBoxSelected();
    }
    public static HashMap<Integer, Boolean> getCheckBoxSelected() {
        return checkBoxSelected;
    }

    public static void setCheckBoxSelected(HashMap<Integer, Boolean> checkBoxSelected) {
        edu.gatech.seclass.groupimplementation.controller.event.EventListAdapter.checkBoxSelected = checkBoxSelected;
    }
    private void initCheckBoxSelected() {
        for (int i = 0; i < studios.size(); i++) {
            getCheckBoxSelected().put(i, false);
        }
    }

    @Override
    public int getCount() {
        return studios.size();
    }

    @Override
    public Object getItem(int position) {
        return studios.get(position);
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
        holder.eventFullName.setText((String) studios.get(position).get("Studio Name"));
        holder.eventYear.setText((String) studios.get(position).get("Studio Short Name"));
        holder.checkBox.setChecked(getCheckBoxSelected().get(position));
        return convertView;
    }

    static class eventViewHolder {
        public TextView eventFullName;
        public TextView eventYear;
        public CheckBox checkBox;
    }
}



