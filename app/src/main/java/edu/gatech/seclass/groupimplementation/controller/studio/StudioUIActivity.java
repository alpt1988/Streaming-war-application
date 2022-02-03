package edu.gatech.seclass.groupimplementation.controller.studio;

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
import edu.gatech.seclass.groupimplementation.controller.stream.StreamCreate;
import edu.gatech.seclass.groupimplementation.controller.stream.StreamUIActivity;
import edu.gatech.seclass.groupimplementation.model.db.DataViewModel;
import edu.gatech.seclass.groupimplementation.model.stream.Stream;
import edu.gatech.seclass.groupimplementation.model.studio.Studio;

public class StudioUIActivity extends AppCompatActivity {
    private EditText studioShortNameText;
    private EditText studioLongNameText;
    private DataViewModel mStudioViewModel;

    // jump to event tab from main menu
    @Override
    protected void onCreate(Bundle enterCurrentStudio) {
        super.onCreate(enterCurrentStudio);
        setContentView(R.layout.studio_details);
        studioShortNameText = findViewById(R.id.studioShortNameID);
        studioLongNameText = findViewById(R.id.studioLongNameID);
        mStudioViewModel = new ViewModelProvider(this).get(DataViewModel.class);
    }


    // save action: check if complete and save to database --------------------------------
    public void save() {
        studioShortNameText = findViewById(R.id.studioShortNameID);
        studioLongNameText = findViewById(R.id.studioLongNameID);

        String studioShortName = studioShortNameText.getText().toString();
        String studioLongName = studioLongNameText.getText().toString();

        if(ConnectionMode.connection){
            if (studioShortName.equals("") || studioLongName.equals("")) {
                // if not fill all information
                Toast.makeText(getApplicationContext(), "Please fill in all the information", Toast.LENGTH_SHORT).show();
            } else {
                LiveData<Studio> selectedStudio = mStudioViewModel.findStudioByShortName(studioShortName);
                selectedStudio.observe(this, new Observer<Studio>() {
                    @Override
                    public void onChanged(@Nullable Studio studio) {
                        if(studio == null){
                            Studio newStudio = new Studio(studioShortName, studioLongName);
                            mStudioViewModel.insert(newStudio);
                            Toast.makeText(getApplicationContext(), "Studio successfully created!", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(getApplicationContext(), "Studio of this short name already exists!", Toast.LENGTH_SHORT).show();
                        }
                        selectedStudio.removeObserver(this);
                    }
                });
            }
        }else{
            Toast.makeText(getApplicationContext(), R.string.Internet, Toast.LENGTH_LONG).show();
        }
    }

    // execute display studio in another UI page
    public void onClickDisplayStudios(View view) {
        Intent displayStudioIntent = new Intent(StudioUIActivity.this, DisplayStudiosActivity.class);
        startActivity(displayStudioIntent);
    }

    public void onClickCreateBack(View view) {
        switch (view.getId()) {
            case R.id.createstudio:
                save();
                break;
            case R.id.studio_back:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
        }
    }
}
