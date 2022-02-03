package edu.gatech.seclass.groupimplementation.controller.offerevent;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModelProvider;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import edu.gatech.seclass.groupimplementation.ConnectionMode;
import edu.gatech.seclass.groupimplementation.R;
import edu.gatech.seclass.groupimplementation.controller.event.DisplayEventsActivity;
import edu.gatech.seclass.groupimplementation.model.db.GlobalRoomDatabase;
import edu.gatech.seclass.groupimplementation.model.event.Event;
import edu.gatech.seclass.groupimplementation.model.offer.Offer;
import edu.gatech.seclass.groupimplementation.model.stream.Stream;
import edu.gatech.seclass.groupimplementation.model.studio.Studio;
import edu.gatech.seclass.groupimplementation.model.db.DataViewModel;

public class OfferEventUIActivity extends AppCompatActivity {

    private EditText streamEditText;
    private EditText offerTypeEditText;
    private EditText offerNameEditText;
    private EditText offerYearEditText;
    private EditText offerPriceEditText;
    private DataViewModel mOfferEventViewModel;
    Offer offer;

    // jump to offer event tab from main menu
    @Override
    protected void onCreate(Bundle enterCurrentEvent) {
        super.onCreate(enterCurrentEvent);
        setContentView(R.layout.offer_event);

        streamEditText = findViewById(R.id.streaming_service);
        offerTypeEditText = findViewById(R.id.offer_type);
        offerNameEditText = findViewById(R.id.event_name);
        offerYearEditText = findViewById(R.id.year);
        offerPriceEditText = findViewById(R.id.price);
        mOfferEventViewModel = new ViewModelProvider(this).get(DataViewModel.class);
    }

    public void offer() {
        String offerPrice = offerPriceEditText.getText().toString();
        String offerType = offerTypeEditText.getText().toString();
        String offerName = offerNameEditText.getText().toString();
        String offerYear = offerYearEditText.getText().toString();
        String streamShortName = streamEditText.getText().toString();
        String eventID = offerNameEditText.getText().toString() + offerYearEditText.getText().toString();

        if(ConnectionMode.connection){
            boolean valid = true;
            // check empty input and non valid input
            if (streamShortName.equals("")) {
                streamEditText.setError("Invalid stream short name");
                valid = false;
            }
            if (offerType.equals("")) {
                offerTypeEditText.setError("Invalid offer type");
                valid = false;
            }
            if (offerName.equals("")) {
                offerNameEditText.setError("Invalid event name");
                valid = false;
            }
            if (offerYear.equals("")) {
                offerYearEditText.setError("Invalid year");
                valid = false;
            }
            if (offerPrice.equals("")) {
                offerPriceEditText.setError("Invalid offer price");
                valid = false;
            }
            //try catch errors to avoid app failure
            try {
                if(!offerYear.equals("")){
                    Integer.parseInt(offerYear);
                }
            }catch(NumberFormatException e) {
                offerYearEditText.setError("Year should be an integer!");
                valid = false;
            }
            try{
                if(!offerPrice.equals("")){
                    Integer.parseInt(offerPrice);
                }
                if(!offerYear.equals("")){
                    Integer.parseInt(offerYear);
                }
            }catch(NumberFormatException e) {
                offerPriceEditText.setError("Offer price and offer year should be integers!");
                valid = false;
            }
            if(valid){
                Offer offer = new Offer(offerType, streamShortName, offerName, Integer.parseInt(offerYear));
                if (offerType.toLowerCase().equals("movie")){
                    offer.setOfferPrice(0);
                }else{
                    offer.setOfferPrice(Integer.parseInt(offerPrice));
                }
                LiveData<Event> selectedEvent = mOfferEventViewModel.findEventById(offerName+offerYear);
                selectedEvent.observe(this, new Observer<Event>(){
                    @Override
                    public void onChanged(@Nullable Event event) {
                        if(event == null){
                            Toast.makeText(getApplicationContext(), "Event is not yet in the database!", Toast.LENGTH_SHORT).show();
                        }else{
                            LiveData<Studio> targetStudio = mOfferEventViewModel.findStudioByShortName(event.getEventStudioOwner());
                            LiveData<Studio> liveStudio = Transformations.switchMap(selectedEvent,(x) -> {return targetStudio;});
                            liveStudio.observe(OfferEventUIActivity.this, new Observer<Studio>() {
                                @Override
                                public void onChanged(@Nullable Studio studio) {
                                    if (studio == null) {
                                        Toast.makeText(getApplicationContext(), "Studio is not yet in the database!", Toast.LENGTH_SHORT).show();
                                    } else {
                                        LiveData<Stream> targetStream = mOfferEventViewModel.findStreamByShortName(streamShortName);
                                        LiveData<Stream> liveStream = Transformations.switchMap(targetStudio, (x) -> {
                                            return targetStream;
                                        });
                                        liveStream.observe(OfferEventUIActivity.this, new Observer<Stream>() {
                                            @Override
                                            public void onChanged(@Nullable Stream stream) {
                                                if(stream == null){
                                                    Toast.makeText(getApplicationContext(), "Streaming service is not yet in the database!", Toast.LENGTH_SHORT).show();
                                                }else {
                                                    int newIncome = studio.getStudioTotalRevenue() + event.getEventLicenseFee();
                                                    int newCurrentIncome = studio.getStudioCurrentRevenue() + event.getEventLicenseFee();
                                                    int newCost = stream.getStreamLicensing() + event.getEventLicenseFee();
                                                    mOfferEventViewModel.insert(offer);
                                                    mOfferEventViewModel.updateCurrentRevenueById(studio.getStudioShortName(), String.valueOf(newIncome));
                                                    mOfferEventViewModel.updateLicensingById(stream.getStreamShortName(), String.valueOf(newCost));
                                                    Toast.makeText(getApplicationContext(), "Offer added successfully!", Toast.LENGTH_SHORT).show();
                                                }
                                                liveStream.removeObserver(this);
                                            }
                                        });
                                    }
                                    liveStudio.removeObserver(this);
                                }
                            });
                        }
                        selectedEvent.removeObserver(this);
                    }
                });
            }
        }else{
            Toast.makeText(getApplicationContext(), R.string.Internet, Toast.LENGTH_LONG).show();
        }
    }

    // delete offer information
    public void delete() {
        String offerPrice = offerPriceEditText.getText().toString();
        String offerType = offerTypeEditText.getText().toString();
        String offerName = offerNameEditText.getText().toString();
        String offerYear = offerYearEditText.getText().toString();
        String streamShortName = streamEditText.getText().toString();
        String eventID = offerNameEditText.getText().toString() + offerYearEditText.getText().toString();

        if(ConnectionMode.connection){
            String offerKey = streamShortName + "," + offerType + "," + offerName + "," + offerYear;
            LiveData<Offer> findRes = mOfferEventViewModel.findOfferByKey(offerKey);
            findRes.observe(this, new Observer<Offer>() {
                @Override
                public void onChanged(@Nullable Offer offer) {
                    if(offer == null){
                        Toast.makeText(getApplicationContext(), "This offer does not exist in the database!", Toast.LENGTH_SHORT).show();
                    }else {
                        mOfferEventViewModel.deleteOffer(offerKey);
                        Toast.makeText(getApplicationContext(), "Offer deleted successfully!", Toast.LENGTH_SHORT).show();
                    }
                    findRes.removeObserver(this);
                }
            });
        }else {
            Toast.makeText(getApplicationContext(), R.string.Internet, Toast.LENGTH_LONG).show();
        }
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.offerthisevent:
                offer();
                break;
            case R.id.offer_back:
                finish();
                break;
            case R.id.deletethisoffer:
                delete();
        }
    }

}
