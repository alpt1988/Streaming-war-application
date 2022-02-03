package edu.gatech.seclass.groupimplementation.model.db;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import edu.gatech.seclass.groupimplementation.model.demo.DemoGroup;
import edu.gatech.seclass.groupimplementation.model.demo.DemoGroupDao;
import edu.gatech.seclass.groupimplementation.model.demo.DemoGroupStream;
import edu.gatech.seclass.groupimplementation.model.demo.DemoGroupStreamDao;
import edu.gatech.seclass.groupimplementation.model.event.Event;
import edu.gatech.seclass.groupimplementation.model.event.EventDao;
import edu.gatech.seclass.groupimplementation.model.offer.Offer;
import edu.gatech.seclass.groupimplementation.model.offer.OfferDao;
import edu.gatech.seclass.groupimplementation.model.stream.Stream;
import edu.gatech.seclass.groupimplementation.model.stream.StreamDao;
import edu.gatech.seclass.groupimplementation.model.studio.Studio;
import edu.gatech.seclass.groupimplementation.model.studio.StudioDao;

class GlobalRepository implements DBRepository{

    private StreamDao mGlobalStreamDao;
    private LiveData<List<Stream>> mAllStreams;

    private DemoGroupDao mGlobalDemoGroupDao;
    private LiveData<List<DemoGroup>> mAllDemoGroups;

    private OfferDao mGlobalOfferDao;
    private LiveData<List<Offer>> mAllOffers;

    private DemoGroupStreamDao mGlobalDemoGroupStreamDao;
    private LiveData<List<DemoGroupStream>> mAllDemoGroupStreams;

    private StudioDao mGlobalStudioDao;
    private LiveData<List<Studio>> mAllStudios;

    private EventDao mGlobalEventDao;
    private LiveData<List<Event>> mAllEvents;


    // Note that in order to unit test the WordRepository, you have to remove the Application
    // dependency. This adds complexity and much more code, and this sample is not about testing.
    // See the BasicSample in the android-architecture-components repository at
    // https://github.com/googlesamples
    GlobalRepository(Application application) {
        GlobalRoomDatabase global_db = GlobalRoomDatabase.getDatabase(application);

        mGlobalDemoGroupDao = global_db.demoGroupDao();
        mAllDemoGroups = mGlobalDemoGroupDao.getAlphabetizedWords();

        mGlobalStreamDao = global_db.streamDao();
        mAllStreams = mGlobalStreamDao.getAlphabetizedWords();

        mGlobalOfferDao = global_db.offerDao();
        mAllOffers = mGlobalOfferDao.getAlphabetizedWords();

        mGlobalDemoGroupStreamDao = global_db.demoGroupStreamDao();
        mAllDemoGroupStreams = mGlobalDemoGroupStreamDao.getAlphabetizedWords();

        mGlobalStudioDao = global_db.studioDao();
        mAllStudios = mGlobalStudioDao.getAlphabetizedWords();

        mGlobalEventDao = global_db.eventDao();
        mAllEvents = mGlobalEventDao.getAlphabetizedWords();
    }

    // demo group
    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    public LiveData<List<DemoGroup>> getAllDemoGroups() {
        return mAllDemoGroups;
    }
    public LiveData<DemoGroup> findDemoGroupByShortName(String demoGroupShortName){
        return mGlobalDemoGroupDao.getSelectedDemoGroup(demoGroupShortName);
    }
    public void updateDemo(String shortName,String longName,String demoAccounts){
        GlobalRoomDatabase.databaseWriteExecutor.execute(() -> {
            mGlobalDemoGroupDao.updateDemo(longName, demoAccounts, shortName);
        });
    }
    // You must call this on a non-UI thread or your app will throw an exception. Room ensures
    // that you're not doing any long running operations on the main thread, blocking the UI.
    public void insert(DemoGroup demoGroup) {
        GlobalRoomDatabase.databaseWriteExecutor.execute(() -> {
            mGlobalDemoGroupDao.insert(demoGroup);
        });
    }
    public void updateDemoGroupCurrentSpending(String demoCurrentSpending, String shortName){
        GlobalRoomDatabase.databaseWriteExecutor.execute(() -> {
            mGlobalDemoGroupDao.updateDemoGroupCurrentSpending(demoCurrentSpending, shortName);
        });
    }
    public void updateDemoGroupPreviousSpending(String demoPreviousSpending, String shortName){
        GlobalRoomDatabase.databaseWriteExecutor.execute(() -> {
            mGlobalDemoGroupDao.updateDemoGroupPreviousSpending(demoPreviousSpending, shortName);
        });
    }
    public void updateDemoGroupTotalSpending(String demoTotalSpending, String shortName){
        GlobalRoomDatabase.databaseWriteExecutor.execute(() -> {
            mGlobalDemoGroupDao.updateDemoGroupTotalSpending(demoTotalSpending, shortName);
        });
    }


    // streaming service
    public LiveData<List<Stream>> getAllStreams() {
        return mAllStreams;
    }
    public LiveData<Stream> findStreamByShortName(String streamShortName){
        return mGlobalStreamDao.getSelectedStream(streamShortName);
    }
    public void updateStream(String shortName,String longName,String subscriptionPrice){
        GlobalRoomDatabase.databaseWriteExecutor.execute(() -> {
            mGlobalStreamDao.updateStreamLongName(longName, shortName);
            mGlobalStreamDao.updateStreamSubscription(subscriptionPrice, shortName);
        });
    }
    public void insert(Stream stream) {
        GlobalRoomDatabase.databaseWriteExecutor.execute(() -> {
            mGlobalStreamDao.insert(stream);
        });
    }
    public void updateStreamCurrentRevenue(String streamCurrentRevenue, String shortName){
        GlobalRoomDatabase.databaseWriteExecutor.execute(() -> {
            mGlobalStreamDao.updateStreamCurrentRevenue(streamCurrentRevenue, shortName);
        });
    }
    public void updateStreamPreviousRevenue(String streamPreviousRevenue, String shortName){
        GlobalRoomDatabase.databaseWriteExecutor.execute(() -> {
            mGlobalStreamDao.updateStreamPreviousRevenue(streamPreviousRevenue, shortName);
        });
    }
    public void updateStreamTotalRevenue(String streamTotalRevenue, String shortName){
        GlobalRoomDatabase.databaseWriteExecutor.execute(() -> {
            mGlobalStreamDao.updateStreamTotalRevenue(streamTotalRevenue, shortName);
        });
    }

    public void updateLicensingById (String newNumber, String shortName){
        GlobalRoomDatabase.databaseWriteExecutor.execute(() -> {
            mGlobalStreamDao.updateLicensingById(newNumber, shortName);
        });
    }


    // offer
    public LiveData<List<Offer>> getAllOffers() {
        return mAllOffers;
    }
    public LiveData<Offer> findOfferByKey(String offerKey){
        return mGlobalOfferDao.selectSpecifiedOffer(offerKey);
    }
    public LiveData<Offer> findOfferByKey(String offerStream, String offerEventName, String offerEventYear){
        return mGlobalOfferDao.selectWatchedOffer(offerStream,offerEventName,offerEventYear);
    }
    public void insert(Offer offer) {
        GlobalRoomDatabase.databaseWriteExecutor.execute(() -> {
            mGlobalOfferDao.insert(offer);
        });
    }
    public void deleteOffer(String offerKey) {
        GlobalRoomDatabase.databaseWriteExecutor.execute(() -> {
            mGlobalOfferDao.delete(offerKey);
        });
    }
    public void deleteAllOffer(){
        GlobalRoomDatabase.databaseWriteExecutor.execute(() -> {
            mGlobalOfferDao.deleteAll();
        });
    }


    //demogroup stream
    public LiveData<List<DemoGroupStream>> getAllDemoGroupStreams() {
        return mAllDemoGroupStreams;
    }
    public void insert(DemoGroupStream demoGroupStream) {
        GlobalRoomDatabase.databaseWriteExecutor.execute(() -> {
            mGlobalDemoGroupStreamDao.insert(demoGroupStream);
        });
    }
    public LiveData<DemoGroupStream> getSelectedDemoGroupStream(String demoGroupStreamKey){
        return mGlobalDemoGroupStreamDao.getSelectedDemoGroupStream(demoGroupStreamKey);
    }
    public void updateDemoGroupStreamWatchViewerCount(String watchViewerCount, String demoGroupStreamKey){
        GlobalRoomDatabase.databaseWriteExecutor.execute(() -> {
            mGlobalDemoGroupStreamDao.updateDemoGroupStreamWatchViewerCount(watchViewerCount, demoGroupStreamKey);
        });
    }
    public void updateDemoGroupStreamWatchPPVCount(boolean watchedPPV, String demoGroupStreamKey){
        GlobalRoomDatabase.databaseWriteExecutor.execute(() -> {
            mGlobalDemoGroupStreamDao.updateDemoGroupStreamWatchPPVCount(watchedPPV, demoGroupStreamKey);
        });
    }
    public void deleteAllDemoGroupStream(){
        GlobalRoomDatabase.databaseWriteExecutor.execute(() -> {
            mGlobalDemoGroupStreamDao.deleteAll();
        });
    }

    // studio
    public LiveData<List<Studio>> getAllStudios() {
        return mAllStudios;
    }
    public LiveData<Studio> findStudioByShortName(String studioShortName){
        return mGlobalStudioDao.getSelectedStudio(studioShortName);
    }
    public void insert(Studio studio) {
        GlobalRoomDatabase.databaseWriteExecutor.execute(() -> {
            mGlobalStudioDao.insert(studio);
        });
    }
    public void updateStudio (String longName, String shortName){
        GlobalRoomDatabase.databaseWriteExecutor.execute(() -> {
            mGlobalStudioDao.updateLongNameByShortName(longName, shortName);
        });
    }
    public void updateCurrentRevenueById (String newNumber, String shortName){
        GlobalRoomDatabase.databaseWriteExecutor.execute(() -> {
            mGlobalStudioDao.updateCurrentRevenueById(newNumber, shortName);
        });
    }
    public void updatePreviousRevenueById (String newNumber, String shortName){
        GlobalRoomDatabase.databaseWriteExecutor.execute(() -> {
            mGlobalStudioDao.updatePreviousRevenueById(newNumber, shortName);
        });
    }
    public void updateTotalRevenueById (String newNumber, String shortName){
        GlobalRoomDatabase.databaseWriteExecutor.execute(() -> {
            mGlobalStudioDao.updateTotalRevenueById(newNumber, shortName);
        });
    }

    // event
    public LiveData<List<Event>> getAllEvents() {
        return mAllEvents;
    }
    public LiveData<Event> findEventById(String eventId){
        return mGlobalEventDao.getSelectedEvent(eventId);
    }
    public void updateEvent(String eventId,String duration,String licenseFee){
        GlobalRoomDatabase.databaseWriteExecutor.execute(() -> {
            mGlobalEventDao.updateEventDuration(duration,eventId);
            mGlobalEventDao.updateEventLicenseFee(licenseFee,eventId);
        });
    }
    public void insert(Event event) {
        GlobalRoomDatabase.databaseWriteExecutor.execute(() -> {
            mGlobalEventDao.insert(event);
        });
    }
}
