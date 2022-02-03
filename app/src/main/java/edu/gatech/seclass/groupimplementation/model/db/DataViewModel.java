package edu.gatech.seclass.groupimplementation.model.db;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import edu.gatech.seclass.groupimplementation.model.demo.DemoGroup;
import edu.gatech.seclass.groupimplementation.model.db.LocalRepository;
import edu.gatech.seclass.groupimplementation.model.stream.Stream;
import edu.gatech.seclass.groupimplementation.model.offer.Offer;
import edu.gatech.seclass.groupimplementation.model.demo.DemoGroupStream;
import edu.gatech.seclass.groupimplementation.model.studio.Studio;
import edu.gatech.seclass.groupimplementation.model.event.Event;

public class DataViewModel extends AndroidViewModel {

    private LocalRepository mRepository;

    private final LiveData<List<DemoGroup>> mAllDemos;
    private final LiveData<List<Stream>> mAllStreams;
    private final LiveData<List<Offer>> mAllOffers;
    private final LiveData<List<Studio>> mAllStudios;
    private final LiveData<List<Event>> mAllEvents;
    private final LiveData<List<DemoGroupStream>> mAllDemoGroupStreams;

    public DataViewModel(Application application) {
        super(application);
        mRepository = new LocalRepository(application);
        mAllDemos = mRepository.getAllDemoGroups();
        mAllStreams = mRepository.getAllStreams();
        mAllOffers = mRepository.getAllOffers();
        mAllStudios = mRepository.getAllStudios();
        mAllEvents = mRepository.getAllEvents();
        mAllDemoGroupStreams = mRepository.getAllDemoGroupStreams();
    }

    // demo group
    public LiveData<List<DemoGroup>> getAllDemos() { return mAllDemos; }
    public LiveData<DemoGroup> findDemoGroupByShortName(String demoGroupShortName){
        return mRepository.findDemoGroupByShortName(demoGroupShortName);
    }
    public void insert(DemoGroup demoGroup) { mRepository.insert(demoGroup); }
    public void updateDemo(String shortName,String longName,String demoAccounts) {
        mRepository.updateDemo(shortName,longName,demoAccounts);
    }
    public void updateDemoGroupCurrentSpending(String shortName, String demoCurrentSpending){
        mRepository.updateDemoGroupCurrentSpending(demoCurrentSpending,shortName);
    }
    public void updateDemoGroupPreviousSpending(String shortName, String demoPreviousSpending){
        mRepository.updateDemoGroupPreviousSpending(demoPreviousSpending,shortName);
    }
    public void updateDemoGroupTotalSpending(String shortName, String demoTotalSpending){
        mRepository.updateDemoGroupTotalSpending(demoTotalSpending,shortName);
    }


    // streaming service
    public LiveData<List<Stream>> getAllStreams() { return mAllStreams; }
    public LiveData<Stream> findStreamByShortName(String streamShortName){
        return mRepository.findStreamByShortName(streamShortName);
    }
    public void insert(Stream stream) { mRepository.insert(stream); }
    public void updateStream(String shortName, String longName, String subscriptionPrice){
        mRepository.updateStream(shortName,longName,subscriptionPrice);
    }
    public void updateStreamCurrentRevenue(String shortName, String streamCurrentRevenue){
        mRepository.updateStreamCurrentRevenue(streamCurrentRevenue,shortName);
    }
    public void updateStreamPreviousRevenue(String shortName, String streamPreviousRevenue){
        mRepository.updateStreamPreviousRevenue(streamPreviousRevenue,shortName);
    }
    public void updateStreamTotalRevenue(String shortName, String streamTotalRevenue){
        mRepository.updateStreamTotalRevenue(streamTotalRevenue,shortName);
    }
    public void updateLicensingById (String shortName, String newNumber){
        mRepository.updateLicensingById(newNumber, shortName);
    }

    // offer
    public LiveData<List<Offer>> getAllOffers() { return mAllOffers; }
    public LiveData<Offer> findOfferByKey(String offerKey){
        return mRepository.findOfferByKey(offerKey);
    }
    public void insert(Offer offer) { mRepository.insert(offer); }
    public LiveData<Offer> findOfferByKey(String offerStream, String offerEventName, String offerEventYear){
        return mRepository.findOfferByKey(offerStream,offerEventName,offerEventYear);
    }
    public void deleteOffer(String offerKey){
        mRepository.deleteOffer(offerKey);
    }
    public void deleteAllOffers() { mRepository.deleteAllOffer(); }

    //demogroup stream
    public LiveData<List<DemoGroupStream>> getAllDemoGroupStreams() {
        return mAllDemoGroupStreams;
    }
    public void insert(DemoGroupStream demoGroupStream) {
        mRepository.insert(demoGroupStream);
    }
    public LiveData<DemoGroupStream> getSelectedDemoGroupStream(String demoGroupStreamKey){
        return mRepository.getSelectedDemoGroupStream(demoGroupStreamKey);
    }
    public void updateDemoGroupStreamWatchViewerCount(String demoGroupStreamKey, String watchViewerCount){
        mRepository.updateDemoGroupStreamWatchViewerCount(watchViewerCount,demoGroupStreamKey);
    }
    public void updateDemoGroupStreamWatchPPVCount(String demoGroupStreamKey, boolean watchedPPV){
        mRepository.updateDemoGroupStreamWatchPPVCount(watchedPPV, demoGroupStreamKey);
    }
    public void deleteAllDemoGroupStream(){
        mRepository.deleteAllDemoGroupStream();
    }

    // studio
    public LiveData<List<Studio>> getAllStudios() { return mAllStudios; }
    public LiveData<Studio> findStudioByShortName(String studioShortName){
        return mRepository.findStudioByShortName(studioShortName);
    }
    public void insert(Studio studio) { mRepository.insert(studio); }
    public void updateStudio (String shortName, String longName){
        mRepository.updateStudio(longName,shortName);
    }
    public void updateCurrentRevenueById (String shortName, String newNumber){
        mRepository.updateCurrentRevenueById(newNumber, shortName);
    }
    public void updatePreviousRevenueById (String shortName, String newNumber){
        mRepository.updatePreviousRevenueById(newNumber, shortName);
    }
    public void updateTotalRevenueById (String shortName, String newNumber){
        mRepository.updateTotalRevenueById(newNumber, shortName);
    }


    // events
    public LiveData<List<Event>> getAllEvents() { return mAllEvents; }
    public LiveData<Event> findEventById(String eventId){
        return mRepository.findEventById(eventId);
    }
    public void insert(Event event) { mRepository.insert(event); }
    public void updateEvent (String eventId,String duration,String licenseFee){
        mRepository.updateEvent(eventId,duration,licenseFee);
    }
}
