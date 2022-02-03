package edu.gatech.seclass.groupimplementation.controller.studio;

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

import java.lang.ref.WeakReference;
import java.util.concurrent.ExecutionException;

import edu.gatech.seclass.groupimplementation.ConnectionMode;
import edu.gatech.seclass.groupimplementation.R;
import edu.gatech.seclass.groupimplementation.model.studio.Studio;
import edu.gatech.seclass.groupimplementation.model.db.DataViewModel;

public class ViewSelectedStudioDetailsActivity extends AppCompatActivity {
    Studio studio;
    private EditText studioShortNameText;
    private EditText studioLongNameText;
    private EditText studioCurrentRevenueText;
    private EditText studioPreviousRevenueText;
    private EditText studioTotalRevenueText;
    private DataViewModel mStudioViewModel;

    @Override
    protected void onCreate(Bundle savedStudioDetailsState) {
        super.onCreate(savedStudioDetailsState);
        setContentView(R.layout.detailed_studio_view);

        studioShortNameText = findViewById(R.id.studioShortNameID);
        studioLongNameText = findViewById(R.id.studioLongNameID);
        studioCurrentRevenueText = findViewById(R.id.currentRevId);
        studioPreviousRevenueText = findViewById(R.id.previousRevId);
        studioTotalRevenueText = findViewById(R.id.totalRevId);
        mStudioViewModel = new ViewModelProvider(this).get(DataViewModel.class);

        try {
            studio = new ViewSelectedStudioDetailsActivity.GetSelectedStudio(ViewSelectedStudioDetailsActivity.this).execute().get();
        }
        catch (InterruptedException | ExecutionException e) {
            Toast.makeText(getApplicationContext(), "Error populating studio details",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public static class GetSelectedStudio extends AsyncTask<Void, Void, Studio> {
        private WeakReference<Context> context;

        public GetSelectedStudio(Context context) {
            this.context = new WeakReference<>(context);
        }

        @Override
        protected Studio doInBackground(Void... voids) {
            return DisplayStudiosActivity.studioSelected;
        }

        @Override
        protected void onPostExecute(Studio studio) {
            super.onPostExecute(studio);

            if (studio == null) {
                return;
            }
            EditText studioLongNameEditText = ((Activity) context.get()).findViewById(R.id.studioLongNameID);
            EditText studioShortNameEditText = ((Activity) context.get()).findViewById(R.id.studioShortNameID);
            EditText studioCurrentRevenueEditText = ((Activity) context.get()).findViewById(R.id.currentRevId);
            EditText studioPreviousRevenueEditText = ((Activity) context.get()).findViewById(R.id.previousRevId);
            EditText studioTotalRevenueEditText = ((Activity) context.get()).findViewById(R.id.totalRevId);

            studioLongNameEditText.setText(studio.getStudioLongName());
            studioShortNameEditText .setText(studio.getStudioShortName());
            studioCurrentRevenueEditText.setText(String.valueOf(studio.getStudioCurrentRevenue()));
            studioPreviousRevenueEditText.setText(String.valueOf(studio.getStudioPreviousRevenue()));
            studioTotalRevenueEditText.setText(String.valueOf(studio.getStudioTotalRevenue()));
        }
    }

    // save updated event information
    public void save() {
        studioLongNameText = findViewById(R.id.studioLongNameID);
        studioShortNameText= findViewById(R.id.studioShortNameID);

        String studioShortName = studioShortNameText.getText().toString();
        String studioLongName = studioLongNameText.getText().toString();

        if(ConnectionMode.connection){
            if (studioLongName.equals("") || studioShortName.equals("")) {
                // if not fill all information
                Toast.makeText(getApplicationContext(), "Please fill in all the information", Toast.LENGTH_SHORT).show();
            } else {
                LiveData<Studio> selectedStudio = mStudioViewModel.findStudioByShortName(studioShortName);
                selectedStudio.observe(this, new Observer<Studio>() {
                    @Override
                    public void onChanged(@Nullable Studio studio) {
                        if(studio == null){
                            Toast.makeText(getApplicationContext(), "Cannot find the studio in the database!", Toast.LENGTH_SHORT).show();
                        }else {
                            mStudioViewModel.updateStudio(studioShortName,studioLongName);
                            Toast.makeText(getApplicationContext(), "Studio updated successfully! Only the long name is changed!", Toast.LENGTH_SHORT).show();
                        }
                        selectedStudio.removeObserver(this);
                    }
                });
            }
        }else{
            Toast.makeText(getApplicationContext(), R.string.Internet, Toast.LENGTH_LONG).show();
        }
    }


    public void onClickStudioDetailsUpdateBack(View view) {
        switch (view.getId()) {
            case R.id.updateStudioNameButton:
                save();
                break;
            case R.id.studioDetailsBack:
                Intent studioDetailsIntent = new Intent(ViewSelectedStudioDetailsActivity.this, DisplayStudiosActivity.class);
                startActivity(studioDetailsIntent);
                break;
        }
    }
}
