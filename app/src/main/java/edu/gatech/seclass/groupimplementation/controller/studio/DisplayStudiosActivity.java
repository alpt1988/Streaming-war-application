package edu.gatech.seclass.groupimplementation.controller.studio;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import edu.gatech.seclass.groupimplementation.R;
import edu.gatech.seclass.groupimplementation.controller.stream.StreamUIActivity;
import edu.gatech.seclass.groupimplementation.controller.stream.StreamUpdate;
import edu.gatech.seclass.groupimplementation.model.db.DataViewModel;
import edu.gatech.seclass.groupimplementation.model.stream.Stream;
import edu.gatech.seclass.groupimplementation.model.studio.Studio;

public class DisplayStudiosActivity extends AppCompatActivity {
    public List<Studio> studios;
    List<HashMap<String, Object>> listStudio = new ArrayList<>();
    public static Studio studioSelected;
    public List<Studio> studioSelectedList = new ArrayList<>();
    List<Integer> studioNumSelected = new ArrayList<>();
    public static int checkedSum = 0;
    private DataViewModel mStudioViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.studio_list);
        mStudioViewModel = new ViewModelProvider(this).get(DataViewModel.class);

        //get studio list from database
        initData();
    }
    private void show(){
        Context context = DisplayStudiosActivity.this;
        ListView listViewStudio = (ListView) findViewById(R.id.eventlistview);
        ListAdapter studioListAdapter = new ListAdapter(listStudio, context);
        listViewStudio.setAdapter(studioListAdapter);
        listViewStudio.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListAdapter.eventViewHolder holder = (ListAdapter.eventViewHolder) view.getTag();
                holder.checkBox.toggle();
                ListAdapter.getCheckBoxSelected().put(position, holder.checkBox.isChecked());
                if (holder.checkBox.isChecked()) {
                    checkedSum++;
                    studioNumSelected.add(0, position);
                    studioSelectedList.add(0, studios.get(position));
                    if (checkedSum > 1) {
                        Toast.makeText(getApplicationContext(), "Please select only one studio!",
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    checkedSum--;
                    for (int i = 0, len = studioNumSelected.size(); i < len; i++) {
                        if (position == studioNumSelected.get(i)) {
                            studioNumSelected.remove(i);
                            len--;
                            i--;
                        }
                    }
                    for (int i = 0, len = studioSelectedList.size(); i < len; i++) {
                        if (studios.get(position) == studioSelectedList.get(i)) {
                            studioSelectedList.remove(i);
                            len--;
                            i--;
                        }
                    }
                }
                try{ studioSelected = studioSelectedList.get(0);}
                catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Please select one studio!",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initData() {
        //get studio list from database
        LiveData<List<Studio>> selectedAllStudios = mStudioViewModel.getAllStudios();
        selectedAllStudios.observe(this, new Observer<List<Studio>>() {
            @Override
            public void onChanged(@Nullable List<Studio> findStudios) {
                if(findStudios == null || findStudios.size() == 0){
                    Toast.makeText(getApplicationContext(), "No studio available",
                            Toast.LENGTH_SHORT).show();
                }else{
                    studios = findStudios;
                    for (int i = 0; i < findStudios.size(); i++) {
                        HashMap<String, Object> map = new HashMap<>();
                        String studioFullName = findStudios.get(i).getStudioLongName();
                        map.put("Studio Name", "Studio Name: " + studioFullName);
                        map.put("Studio Short Name", "Studio short name: " + findStudios.get(i).getStudioShortName());
                        listStudio.add(map);
                    }
                    show();
                }
                selectedAllStudios.removeObserver(this);
            }
        });
    }

    public void onClickStudioListView(View view) {
        checkedSum = 0;
        Intent studioDetailsIntent = new Intent(DisplayStudiosActivity.this, StudioUIActivity.class);
        startActivity(studioDetailsIntent);
    }

    public void onClickViewSelectedStudioDetails(View view) {
        checkedSum = 0;
        Intent studioDetailsIntent = new Intent(DisplayStudiosActivity.this, ViewSelectedStudioDetailsActivity.class);
        startActivity(studioDetailsIntent);
    }
}
