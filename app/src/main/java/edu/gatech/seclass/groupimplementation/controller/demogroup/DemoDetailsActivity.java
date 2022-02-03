package edu.gatech.seclass.groupimplementation.controller.demogroup;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import edu.gatech.seclass.groupimplementation.ConnectionMode;
import edu.gatech.seclass.groupimplementation.MainActivity;
import edu.gatech.seclass.groupimplementation.R;
import edu.gatech.seclass.groupimplementation.model.db.DataViewModel;
import edu.gatech.seclass.groupimplementation.model.demo.DemoGroup;
import edu.gatech.seclass.groupimplementation.model.demo.DemoGroupStream;

public class DemoDetailsActivity extends AppCompatActivity {
    private EditText m_demoShortNameET;
    private EditText m_demoLongNameET;
    private EditText m_demoNumAccountsET;
    private DataViewModel mDemoGroupViewModel;
    private List<DemoGroup> allDemoGroups = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo_details);

        m_demoShortNameET = findViewById(R.id.demoDetailsShortNameID);
        m_demoLongNameET = findViewById(R.id.demoDetailsLongNameID);
        m_demoNumAccountsET = findViewById(R.id.demoDetailsNumAccountsID);
        mDemoGroupViewModel = new ViewModelProvider(this).get(DataViewModel.class);
    }


    // execute display demo in another UI page
    public void onClickCreateDisplayUpdateButton(View view) {
        switch (view.getId()) {
            case R.id.createdemo:
                createDemo();
                break;
            case R.id.displaydemo:
                displayDemo();
                break;
            case R.id.updatedemo:
                updateDemo();
                break;
            case R.id.backtomain:
                startActivity(new Intent(DemoDetailsActivity.this, MainActivity.class));
                break;
        }
    }

    // save action: check if complete and save to database --------------------------------
    public void createDemo() {
        String demoShortName = m_demoShortNameET.getText().toString();
        String demoLongName = m_demoLongNameET.getText().toString();
        String demoNumAccounts = m_demoNumAccountsET.getText().toString();
        if(ConnectionMode.connection){
            if (demoShortName.equals("") || demoLongName.equals("") || demoNumAccounts.equals("") ) {
                // if not fill all information
                Toast.makeText(getApplicationContext(), "Please fill in all the information", Toast.LENGTH_SHORT).show();
            } else {
                LiveData<DemoGroup> selectedDemoGroup = mDemoGroupViewModel.findDemoGroupByShortName(demoShortName);
                selectedDemoGroup.observe(this, new Observer<DemoGroup>() {
                    @Override
                    public void onChanged(@Nullable DemoGroup demoGroup) {
                        if(demoGroup == null){
                            DemoGroup newDemoGroup = new DemoGroup(demoShortName, demoLongName, Integer.parseInt(demoNumAccounts),
                                    0,0,0);
                            mDemoGroupViewModel.insert(newDemoGroup);
                            Intent intent = new Intent(DemoDetailsActivity.this, DemoDisplayActivity.class);
                            intent.putExtra("demo_shortname", demoShortName);
                            startActivity(intent);
                        }else {
                            Toast.makeText(getApplicationContext(), "Demographic group of this short name already exists!", Toast.LENGTH_SHORT).show();
                        }
                        selectedDemoGroup.removeObserver(this);
                    }
                });
            }
        }else{
            Toast.makeText(getApplicationContext(), R.string.Internet, Toast.LENGTH_LONG).show();
        }
    }

    // display action: find the demo in db and display --------------------------------
    public void displayDemo(){
        String demoShortNameText = m_demoShortNameET.getText().toString();
        if (demoShortNameText.equals("")){
            Toast.makeText(getApplicationContext(), "Please type a demographic group to display!", Toast.LENGTH_SHORT).show();
        }
        else {
            LiveData<DemoGroup> selectedDemoGroup = mDemoGroupViewModel.findDemoGroupByShortName(demoShortNameText);
            selectedDemoGroup.observe(this, new Observer<DemoGroup>() {
                @Override
                public void onChanged(@Nullable DemoGroup demoGroup) {
                    if(demoGroup == null){
                        Toast.makeText(getApplicationContext(), "Demographic group is not found!", Toast.LENGTH_SHORT).show();
                    }else {
                        Intent intent = new Intent(DemoDetailsActivity.this, DemoDisplayActivity.class);
                        intent.putExtra("demo_shortname", demoShortNameText);
                        startActivity(intent);
                    }
                    selectedDemoGroup.removeObserver(this);
                }
            });
        }
    }

    public void updateDemo() {
        String displayDemoShortNameET = m_demoShortNameET.getText().toString();
        String displayDemoLongNameET = m_demoLongNameET.getText().toString();
        String displayDemoNumAccountsET = m_demoNumAccountsET.getText().toString();

        if(ConnectionMode.connection){
            if (displayDemoShortNameET.equals("")) {
                Toast.makeText(getApplicationContext(), R.string.miss_shortname, Toast.LENGTH_SHORT).show();
            } else {
                try { // subscription number not correct
                    if(!displayDemoNumAccountsET.equals("")){  // if not empty, try convert to integer
                        Integer.parseInt(displayDemoNumAccountsET);
                    }
                }catch(NumberFormatException e) {
                    Toast.makeText(getApplicationContext(),"The account number should be an integer!",Toast.LENGTH_LONG).show();
                }
                LiveData<DemoGroup> selectedDemoGroup = mDemoGroupViewModel.findDemoGroupByShortName(displayDemoShortNameET);
                selectedDemoGroup.observe(this, new Observer<DemoGroup>() {
                    @Override
                    public void onChanged(@Nullable DemoGroup demoGroup) {
                        if(demoGroup == null){
                            Toast.makeText(getApplicationContext(), "Demographic group is not found!", Toast.LENGTH_SHORT).show();
                        }else{
                            LiveData<List<DemoGroupStream>> selectedAllDemoStreams = mDemoGroupViewModel.getAllDemoGroupStreams();
                            LiveData<List<DemoGroupStream>> liveDemoGroupStream = Transformations.switchMap(selectedDemoGroup, (x) -> {
                                return selectedAllDemoStreams;
                            });
                            liveDemoGroupStream.observe(DemoDetailsActivity.this, new Observer<List<DemoGroupStream>>() {
                                @Override
                                public void onChanged(List<DemoGroupStream> demoGroupStreams) {
                                    boolean watched = false;
                                    if(demoGroupStreams != null && demoGroupStreams.size()>0){
                                        for(DemoGroupStream temp : demoGroupStreams){
                                            if(temp.getDemoGroupStreamKey().contains(displayDemoShortNameET)){
                                                watched = true;   // already watched, cannot change
                                            }
                                        }
                                    }
                                    String displayDemoShortNameET = m_demoShortNameET.getText().toString();
                                    String displayDemoLongNameET = m_demoLongNameET.getText().toString();
                                    String displayDemoNumAccountsET = m_demoNumAccountsET.getText().toString();
                                    if(displayDemoLongNameET.equals("")){
                                        displayDemoLongNameET = demoGroup.getDemoGroupLongName();
                                    }
                                    if(!watched){
                                        Intent intent = new Intent(DemoDetailsActivity.this, DemoDisplayActivity.class);
                                        intent.putExtra("demo_shortname", displayDemoShortNameET);
                                        if(displayDemoNumAccountsET.equals("")){
                                            displayDemoNumAccountsET = String.valueOf(demoGroup.getDemoAccounts());
                                        }
                                        mDemoGroupViewModel.updateDemo(displayDemoShortNameET,displayDemoLongNameET,displayDemoNumAccountsET);
                                        startActivity(intent);
                                    }else{
                                        Intent intent = new Intent(DemoDetailsActivity.this, DemoDisplayActivity.class);
                                        intent.putExtra("demo_shortname", displayDemoShortNameET);
                                        if(displayDemoNumAccountsET.equals("")){  // can only change long name
                                            displayDemoNumAccountsET = String.valueOf(demoGroup.getDemoAccounts());
                                            mDemoGroupViewModel.updateDemo(displayDemoShortNameET,displayDemoLongNameET,displayDemoNumAccountsET);
                                            startActivity(intent);
                                        }else{
                                            Toast.makeText(getApplicationContext(), "Demo accounts cannot change because it has watched events!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    liveDemoGroupStream.removeObserver(this);
                                }
                            });
                        }
                        selectedDemoGroup.removeObserver(this);
                    }
                });
            }
        }else{
            Toast.makeText(getApplicationContext(), R.string.Internet, Toast.LENGTH_LONG).show();
        }
    }
}
