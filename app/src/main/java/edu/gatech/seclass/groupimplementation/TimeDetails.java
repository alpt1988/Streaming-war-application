package edu.gatech.seclass.groupimplementation;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.Spinner;

import java.sql.Time;
import java.util.List;

import edu.gatech.seclass.groupimplementation.model.db.DataViewModel;
import edu.gatech.seclass.groupimplementation.model.db.GlobalArchiveRoomDatabase;
import edu.gatech.seclass.groupimplementation.model.db.GlobalRoomDatabase;
import edu.gatech.seclass.groupimplementation.model.demo.DemoGroup;
import edu.gatech.seclass.groupimplementation.model.stream.Stream;
import edu.gatech.seclass.groupimplementation.model.offer.Offer;
import edu.gatech.seclass.groupimplementation.model.demo.DemoGroupStream;
import edu.gatech.seclass.groupimplementation.model.studio.Studio;

public class TimeDetails extends AppCompatActivity {

    private Spinner Month;
    private EditText Year;
    public static int yearStamp = 2000;
    public static int monthStamp = 1;
    private DataViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.time_details);
        Month = (Spinner) findViewById(R.id.monthid);
        Year = (EditText) findViewById(R.id.yearid);
        mViewModel = new ViewModelProvider(this).get(DataViewModel.class);

        // set with current initial value
        Month.setSelection(monthStamp-1);
        Year.setText(String.valueOf(yearStamp));
    }

    public void onClick(View view){
        int ID = view.getId();
        if(ID == R.id.configuration){
            String months = Month.getSelectedItem().toString();
            String years = Year.getText().toString();
            if(years.equals("")){ // need a non-empty longName
                Toast.makeText(getApplicationContext(),R.string.miss_year,Toast.LENGTH_LONG).show();
            }else{
                if (Integer.parseInt(years)<1900){
                    Toast.makeText(getApplicationContext(),R.string.valid_year,Toast.LENGTH_LONG).show();
                }else {
                    configDialog(months, years);
                }
            }
        }else if(ID == R.id.nextmonth){
            NextMonthDialog(true);
            updateEachMonth();
            updateArchiveDatabase();  // Update archive database
        }else if(ID == R.id.displaytime){
            NextMonthDialog(false);
        }else{
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
        }
    }

    private void configDialog(String month_input,String year_input){
        AlertDialog.Builder builder = new AlertDialog.Builder(TimeDetails.this);
        builder.setMessage( R.string.time_config)
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(TimeDetails.this);
                        String result = "Successfully reconfigure! Now the time is " + year_input + "-" + month_input;
                        builder.setMessage(result)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        yearStamp = Integer.parseInt(year_input);
                                        monthStamp = Integer.parseInt(month_input);
                                        Month.setSelection(monthStamp-1);
                                        Year.setText(year_input);
                                        dialog.dismiss();
                                    }
                                });
                        builder.create().show();
                    }
                })
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                        dialog.dismiss();
                    }
                });
        // Create the AlertDialog object and return it
        builder.create().show();
    }

    private void NextMonthDialog(boolean update){
        String result = "";
        if (update){
            if (monthStamp == 12) { yearStamp++; }
            monthStamp = (monthStamp % 12) + 1;
            result += "Welcome to the next month! ";
        }
        result = result + "Now the time is " + String.valueOf(yearStamp) + "-";
        if(monthStamp<10){
            result = result + "0" +  String.valueOf(monthStamp);
        }else{
            result = result + String.valueOf(monthStamp);
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(TimeDetails.this);
        builder.setMessage(result)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        if(update){
                            Month.setSelection(monthStamp-1);
                            Year.setText(String.valueOf(yearStamp));
                        }
                        dialog.dismiss();
                    }
                });
        // Create the AlertDialog object and return it
        builder.create().show();
    }

    public void updateEachMonth(){
        LiveData<List<Offer>> selectedAllOffers = mViewModel.getAllOffers();
        selectedAllOffers.observe(this, new Observer<List<Offer>>() {
            @Override
            public void onChanged(@Nullable List<Offer> offer) {
                mViewModel.deleteAllOffers();
                LiveData<List<DemoGroupStream>> selectedAllDemoStreams = mViewModel.getAllDemoGroupStreams();
                LiveData<List<DemoGroupStream>> liveDemoGroupStream = Transformations.switchMap(selectedAllOffers, (x) -> {
                    return selectedAllDemoStreams;
                });
                liveDemoGroupStream.observe(TimeDetails.this, new Observer<List<DemoGroupStream>>() {
                    @Override
                    public void onChanged(List<DemoGroupStream> demoGroupStreams) {
                        for(DemoGroupStream temp : demoGroupStreams){
                            mViewModel.deleteAllDemoGroupStream();
                        }
                        LiveData<List<DemoGroup>> selectedAllDemoGroups = mViewModel.getAllDemos();
                        LiveData<List<DemoGroup>> liveDemoGroup = Transformations.switchMap(selectedAllDemoStreams, (x) -> {
                            return selectedAllDemoGroups;
                        });
                        liveDemoGroup.observe(TimeDetails.this, new Observer<List<DemoGroup>>() {
                            @Override
                            public void onChanged(List<DemoGroup> demoGroups) {
                                for(DemoGroup temp : demoGroups){
                                    mViewModel.updateDemoGroupPreviousSpending(temp.getDemoGroupShortName(),String.valueOf(temp.getDemoCurrentSpending()));
                                    int totalSpending = temp.getDemoTotalSpending() + temp.getDemoCurrentSpending();
                                    mViewModel.updateDemoGroupTotalSpending(temp.getDemoGroupShortName(),String.valueOf(totalSpending));
                                    mViewModel.updateDemoGroupCurrentSpending(temp.getDemoGroupShortName(),"0");
                                }
                                LiveData<List<Stream>> selectedAllStreams = mViewModel.getAllStreams();
                                LiveData<List<Stream>> liveStream = Transformations.switchMap(selectedAllDemoGroups,(x) -> {
                                    return selectedAllStreams;
                                });
                                liveStream.observe(TimeDetails.this, new Observer<List<Stream>>() {
                                    @Override
                                    public void onChanged(List<Stream> streams) {
                                        for(Stream temp : streams){
                                            mViewModel.updateStreamPreviousRevenue(temp.getStreamShortName(),String.valueOf(temp.getStreamCurrentRevenue()));
                                            int totalSpending = temp.getStreamTotalRevenue() + temp.getStreamCurrentRevenue();
                                            mViewModel.updateStreamTotalRevenue(temp.getStreamShortName(),String.valueOf(totalSpending));
                                            mViewModel.updateStreamCurrentRevenue(temp.getStreamShortName(),"0");
                                        }
                                        LiveData<List<Studio>> selectedAllStudios = mViewModel.getAllStudios();
                                        LiveData<List<Studio>> liveAllStudios = Transformations.switchMap(selectedAllStreams,(x) -> {
                                            return selectedAllStudios;
                                        });
                                        liveAllStudios.observe(TimeDetails.this, new Observer<List<Studio>>() {
                                            @Override
                                            public void onChanged(List<Studio> studios) {
                                                for(Studio temp : studios){
                                                    mViewModel.updatePreviousRevenueById(temp.getStudioShortName(),String.valueOf(temp.getStudioCurrentRevenue()));
                                                    int totalSpending = temp.getStudioTotalRevenue() + temp.getStudioCurrentRevenue();
                                                    mViewModel.updateTotalRevenueById(temp.getStudioShortName(),String.valueOf(totalSpending));
                                                    mViewModel.updateCurrentRevenueById(temp.getStudioShortName(),"0");
                                                }
                                                liveAllStudios.removeObserver(this);
                                            }
                                        });
                                        liveStream.removeObserver(this);
                                    }
                                });
                                liveDemoGroup.removeObserver(this);
                            }
                        });
                        liveDemoGroupStream.removeObserver(this);
                    }
                });
                selectedAllOffers.removeObserver(this);
            }
        });
    }

    public void updateArchiveDatabase(){
        // update archive database and current database
        GlobalArchiveRoomDatabase db_archive = GlobalArchiveRoomDatabase.getDatabase(this.getApplicationContext());
        Log.d("TimeDetails", "global archive db is created");

        GlobalRoomDatabase db_global = GlobalRoomDatabase.getDatabase(getApplicationContext());
        Log.d("TimeDetails", "global db is created");

        new Thread(new Runnable() {
            @Override
            public void run() {
                List<DemoGroup>  demoGroups = db_global.demoGroupDao().getAll();
                db_archive.demoGroupDao().insertBatch(demoGroups);

//                // clear current month's data
//                db_global.demoGroupDao().deleteAll();
            }
        }).start();
    }
}
