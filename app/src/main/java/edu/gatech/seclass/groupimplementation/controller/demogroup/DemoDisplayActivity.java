package edu.gatech.seclass.groupimplementation.controller.demogroup;

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
import androidx.lifecycle.ViewModelProvider;
import edu.gatech.seclass.groupimplementation.MainActivity;
import edu.gatech.seclass.groupimplementation.R;
import edu.gatech.seclass.groupimplementation.model.db.DataViewModel;
import edu.gatech.seclass.groupimplementation.model.demo.DemoGroup;

public class DemoDisplayActivity extends AppCompatActivity {

    private EditText m_displayDemoShortNameET;
    private EditText m_displayDemoLongNameET;
    private EditText m_displayDemoNumAccountsET;
    private EditText m_displayDemoCurrentSpendingET;
    private EditText m_displayDemoPreviousSpendingET;
    private EditText m_displayDemoTotalSpendingET;
    private DataViewModel mDemoGroupViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_demogroup);

        m_displayDemoShortNameET = findViewById(R.id.displayDemoShortNameID);
        m_displayDemoLongNameET = findViewById(R.id.displayDemoLongNameID);
        m_displayDemoNumAccountsET = findViewById(R.id.displayDemoNumAccountsID);
        m_displayDemoCurrentSpendingET = findViewById(R.id.displayDemoCurrentSpendingID);
        m_displayDemoPreviousSpendingET = findViewById(R.id.displayDemoPreviousSpendingID);
        m_displayDemoTotalSpendingET = findViewById(R.id.displayDemoTotalSpendingID);
        mDemoGroupViewModel = new ViewModelProvider(this).get(DataViewModel.class);

        // receive from stream details to show success
        Bundle info = getIntent().getExtras();
        String demoShortNameText = info.getString("demo_shortname");
        LiveData<DemoGroup> selectedDemoGroup = mDemoGroupViewModel.findDemoGroupByShortName(demoShortNameText);
        selectedDemoGroup.observe(this, new Observer<DemoGroup>() {
            @Override
            public void onChanged(@Nullable DemoGroup selectedDemoGroup) {
                if(selectedDemoGroup == null){
                    Toast.makeText(getApplicationContext(), "Demographic group is not found in activity!", Toast.LENGTH_SHORT).show();
                }else {
                    m_displayDemoShortNameET.setText(selectedDemoGroup.getDemoGroupShortName());
                    m_displayDemoLongNameET.setText(selectedDemoGroup.getDemoGroupLongName());
                    m_displayDemoNumAccountsET.setText(String.valueOf(selectedDemoGroup.getDemoAccounts()));
                    m_displayDemoCurrentSpendingET.setText(String.valueOf(selectedDemoGroup.getDemoCurrentSpending()));
                    m_displayDemoPreviousSpendingET.setText(String.valueOf(selectedDemoGroup.getDemoPreviousSpending()));
                    m_displayDemoTotalSpendingET.setText(String.valueOf(selectedDemoGroup.getDemoTotalSpending()));
                }
            }
        });
    }

    public void onClickUpdateBackButton(View view) {
        startActivity(new Intent(DemoDisplayActivity.this, MainActivity.class));
    }
}
