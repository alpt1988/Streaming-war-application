package edu.gatech.seclass.groupimplementation.controller.demogroup;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModelProvider;

import edu.gatech.seclass.groupimplementation.ConnectionMode;
import edu.gatech.seclass.groupimplementation.MainActivity;
import edu.gatech.seclass.groupimplementation.R;
import edu.gatech.seclass.groupimplementation.model.demo.DemoGroup;
import edu.gatech.seclass.groupimplementation.model.demo.DemoGroupStream;
import edu.gatech.seclass.groupimplementation.model.db.DataViewModel;
import edu.gatech.seclass.groupimplementation.model.offer.Offer;
import edu.gatech.seclass.groupimplementation.model.stream.Stream;

public class WatchEventActivity extends AppCompatActivity {
    private EditText m_watchDemoShortNameET;
    private EditText m_watchPercentageET;
    private EditText m_watchStreamET;
    private EditText m_watchEventNameET;
    private EditText m_watchEventYearET;
    private DataViewModel mWatchEventViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.watch_event);

        m_watchDemoShortNameET = findViewById(R.id.watchDemoShortNameID);
        m_watchPercentageET = findViewById(R.id.watchPercentageID);
        m_watchStreamET = findViewById(R.id.watchStreamID);
        m_watchEventNameET = findViewById(R.id.watchEventNameID);
        m_watchEventYearET = findViewById(R.id.watchEventYearID);
        mWatchEventViewModel = new ViewModelProvider(this).get(DataViewModel.class);
    }

    public void onClickWatchButton(View view) {
        String watchDemoShortName = m_watchDemoShortNameET.getText().toString();
        String watchPercentage = m_watchPercentageET.getText().toString();
        String watchStream = m_watchStreamET.getText().toString();
        String watchEventName = m_watchEventNameET.getText().toString();
        String watchEventYear = m_watchEventYearET.getText().toString();

        if(ConnectionMode.connection) {
            if (watchDemoShortName.equals("") || watchPercentage.equals("")
                    || watchStream.equals("") || watchEventName.equals("") || watchEventYear.equals("") ) {
                Toast.makeText(getApplicationContext(), "Please fill in all the information", Toast.LENGTH_SHORT).show();
            } else {
                try{
                    int temp = Integer.parseInt(watchPercentage) + Integer.parseInt(watchEventYear);
                }catch(NumberFormatException e) {
                    Toast.makeText(getApplicationContext(),"The watch percentage and watch event year should be integers!",Toast.LENGTH_LONG).show();
                }
                LiveData<DemoGroup> watchDemoGroupObj = mWatchEventViewModel.findDemoGroupByShortName(watchDemoShortName);
                watchDemoGroupObj.observe(this, new Observer<DemoGroup>() {
                    @Override
                    public void onChanged(DemoGroup demoGroup) {
                        if(demoGroup == null){
                            Toast.makeText(getApplicationContext(), "Demographic group does not exist!", Toast.LENGTH_SHORT).show();
                        }else{
                            LiveData<Stream> watchStreamObj = mWatchEventViewModel.findStreamByShortName(watchStream);
                            LiveData<Stream> liveStream = Transformations.switchMap(watchDemoGroupObj, (x) -> {
                                return watchStreamObj;
                            });
                            liveStream.observe(WatchEventActivity.this, new Observer<Stream>() {
                                @Override
                                public void onChanged(Stream stream) {
                                    if (stream == null){
                                        Toast.makeText(getApplicationContext(), "Streaming service is not valid!", Toast.LENGTH_SHORT).show();
                                    }else{
                                        LiveData<Offer> watchedOfferObj = mWatchEventViewModel.findOfferByKey(watchStream,watchEventName,watchEventYear);
                                        LiveData<Offer> liveOffer = Transformations.switchMap(watchStreamObj,(x) -> {
                                            return watchedOfferObj;
                                        });
                                        liveOffer.observe(WatchEventActivity.this, new Observer<Offer>() {
                                            @Override
                                            public void onChanged(Offer offer) {
                                                if(offer == null){
                                                    Toast.makeText(getApplicationContext(), "Offer is not valid!", Toast.LENGTH_SHORT).show();
                                                }else{
                                                    String demoGroupStreamKey = demoGroup.getDemoGroupShortName() + "+" + stream.getStreamShortName() + "+"  + watchEventName + "+"  + watchEventYear;
                                                    LiveData<DemoGroupStream> demoGroupStream = mWatchEventViewModel.getSelectedDemoGroupStream(demoGroupStreamKey);
                                                    LiveData<DemoGroupStream> liveDemoStream = Transformations.switchMap(watchedOfferObj,(x) -> {
                                                        return demoGroupStream;
                                                    });
                                                    liveDemoStream.observe(WatchEventActivity.this, new Observer<DemoGroupStream>() {
                                                        @Override
                                                        public void onChanged(DemoGroupStream demoGroupStream) {
                                                            if(demoGroupStream == null){
                                                                demoGroupStream = new DemoGroupStream(demoGroupStreamKey, 0);
                                                                mWatchEventViewModel.insert(demoGroupStream);
                                                            }
                                                            int demoGroupNumAccounts = demoGroup.getDemoAccounts();
                                                            int watchViewerCount = (int) (demoGroupNumAccounts * Integer.parseInt(watchPercentage)/100);
                                                            // Identify the streaming service & the subscription fee
                                                            int watchSubscriptionFee = stream.getStreamSubscription();

                                                            // Identify the event selected & the Pay-Per-View price
                                                            String watchType = offer.getOfferType();
                                                            int watchPayPerViewPrice = offer.getOfferPrice();

                                                            // Calculate watch viewing cost
                                                            int watchViewingCost = 0;
                                                            if (watchType.equals("movie")){
                                                                int demoStreamCountFromDB = demoGroupStream.getWatchViewerCount();
                                                                if (watchViewerCount > demoStreamCountFromDB){
                                                                    watchViewingCost = (watchViewerCount - demoStreamCountFromDB) * watchSubscriptionFee;
                                                                    mWatchEventViewModel.updateDemoGroupStreamWatchViewerCount(demoGroupStreamKey, String.valueOf(watchViewerCount));
                                                                }
                                                            }
                                                            else if (watchType.equals("ppv")){
                                                                watchViewingCost = watchViewerCount * watchPayPerViewPrice;
                                                                mWatchEventViewModel.updateDemoGroupStreamWatchPPVCount(demoGroupStreamKey,true);
                                                            }
                                                            // Adjust funds for watching events
                                                            // setCurrentSpending for the DemoGroup object
                                                            int watchDemoGroupObjCurrentSpending = watchViewingCost + demoGroup.getDemoCurrentSpending();
                                                            mWatchEventViewModel.updateDemoGroupCurrentSpending(watchDemoShortName,String.valueOf(watchDemoGroupObjCurrentSpending));

                                                            // setCurrentRevenue for the Stream object
                                                            int watchStreamObjCurrentRevenue = stream.getStreamCurrentRevenue() + watchViewingCost;
                                                            mWatchEventViewModel.updateStreamCurrentRevenue(watchStream,String.valueOf(watchStreamObjCurrentRevenue));
                                                            Toast.makeText(getApplicationContext(), "Watch event updated successfully!", Toast.LENGTH_LONG).show();
                                                            liveDemoStream.removeObserver(this);
                                                        }
                                                    });
                                                }
                                                liveOffer.removeObserver(this);
                                            }
                                        });
                                    }
                                    liveStream.removeObserver(this);
                                }
                            });
                        }
                        watchDemoGroupObj.removeObserver(this);
                    }
                });
            }
        }else {
            Toast.makeText(getApplicationContext(), R.string.Internet, Toast.LENGTH_LONG).show();
        }
    }

    public void onClickBack(View view){
        startActivity(new Intent(WatchEventActivity.this, MainActivity.class));
    }
}
