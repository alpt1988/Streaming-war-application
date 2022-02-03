package edu.gatech.seclass.groupimplementation.model.db;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import edu.gatech.seclass.groupimplementation.ConnectionMode;
import edu.gatech.seclass.groupimplementation.model.demo.DemoGroup;
import edu.gatech.seclass.groupimplementation.model.demo.DemoGroupDao;
import edu.gatech.seclass.groupimplementation.model.stream.Stream;
import edu.gatech.seclass.groupimplementation.model.stream.StreamDao;
import edu.gatech.seclass.groupimplementation.model.offer.Offer;
import edu.gatech.seclass.groupimplementation.model.offer.OfferDao;
import edu.gatech.seclass.groupimplementation.model.demo.DemoGroupStream;
import edu.gatech.seclass.groupimplementation.model.demo.DemoGroupStreamDao;
import edu.gatech.seclass.groupimplementation.model.studio.Studio;
import edu.gatech.seclass.groupimplementation.model.studio.StudioDao;
import edu.gatech.seclass.groupimplementation.model.event.Event;
import edu.gatech.seclass.groupimplementation.model.event.EventDao;
import edu.gatech.seclass.groupimplementation.model.db.GlobalRepository;

class LocalRepository implements DBRepository{

    private StreamDao mStreamDao;
    private LiveData<List<Stream>> mAllStreams;

    private DemoGroupDao mDemoGroupDao;
    private LiveData<List<DemoGroup>> mAllDemoGroups;

    private OfferDao mOfferDao;
    private LiveData<List<Offer>> mAllOffers;

    private DemoGroupStreamDao mDemoGroupStreamDao;
    private LiveData<List<DemoGroupStream>> mAllDemoGroupStreams;

    private StudioDao mStudioDao;
    private LiveData<List<Studio>> mAllStudios;

    private EventDao mEventDao;
    private LiveData<List<Event>> mAllEvents;

    GlobalRepository globalProxy;

    // Note that in order to unit test the WordRepository, you have to remove the Application
    // dependency. This adds complexity and much more code, and this sample is not about testing.
    // See the BasicSample in the android-architecture-components repository at
    // https://github.com/googlesamples
    LocalRepository(Application application) {
        LocalRoomDatabase local_db = LocalRoomDatabase.getDatabase(application);
        globalProxy = new GlobalRepository(application);

        mDemoGroupDao = local_db.demoGroupDao();
        mAllDemoGroups = mDemoGroupDao.getAlphabetizedWords();

        mStreamDao = local_db.streamDao();
        mAllStreams = mStreamDao.getAlphabetizedWords();

        mOfferDao = local_db.offerDao();
        mAllOffers = mOfferDao.getAlphabetizedWords();

        mDemoGroupStreamDao = local_db.demoGroupStreamDao();
        mAllDemoGroupStreams = mDemoGroupStreamDao.getAlphabetizedWords();

        mStudioDao = local_db.studioDao();
        mAllStudios = mStudioDao.getAlphabetizedWords();

        mEventDao = local_db.eventDao();
        mAllEvents = mEventDao.getAlphabetizedWords();
    }

    // demo group
    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    public LiveData<List<DemoGroup>> getAllDemoGroups() {
        if(ConnectionMode.connection){
            return globalProxy.getAllDemoGroups();
        }else{
            return mAllDemoGroups;
        }
    }
    public LiveData<DemoGroup> findDemoGroupByShortName(String demoGroupShortName){
        if(ConnectionMode.connection){
            return globalProxy.findDemoGroupByShortName(demoGroupShortName);
        }else {
            return mDemoGroupDao.getSelectedDemoGroup(demoGroupShortName);
        }
    }
    public void updateDemo(String shortName,String longName,String demoAccounts){
        if(ConnectionMode.connection){
            globalProxy.updateDemo(shortName,longName,demoAccounts);
        }
        LocalRoomDatabase.databaseWriteExecutor.execute(() -> {
            mDemoGroupDao.updateDemo(longName, demoAccounts, shortName);
        });
    }
    // You must call this on a non-UI thread or your app will throw an exception. Room ensures
    // that you're not doing any long running operations on the main thread, blocking the UI.
    public void insert(DemoGroup demoGroup) {
        if(ConnectionMode.connection){
            globalProxy.insert(demoGroup);
        }
        LocalRoomDatabase.databaseWriteExecutor.execute(() -> {
            mDemoGroupDao.insert(demoGroup);
        });
    }
    public void updateDemoGroupCurrentSpending(String demoCurrentSpending, String shortName){
        if(ConnectionMode.connection){
            globalProxy.updateDemoGroupCurrentSpending(demoCurrentSpending, shortName);
        }
        LocalRoomDatabase.databaseWriteExecutor.execute(() -> {
            mDemoGroupDao.updateDemoGroupCurrentSpending(demoCurrentSpending, shortName);
        });
    }
    public void updateDemoGroupPreviousSpending(String demoPreviousSpending, String shortName){
        if(ConnectionMode.connection){
            globalProxy.updateDemoGroupPreviousSpending(demoPreviousSpending, shortName);
        }
        LocalRoomDatabase.databaseWriteExecutor.execute(() -> {
            mDemoGroupDao.updateDemoGroupPreviousSpending(demoPreviousSpending, shortName);
        });
    }
    public void updateDemoGroupTotalSpending(String demoTotalSpending, String shortName){
        if(ConnectionMode.connection){
            globalProxy.updateDemoGroupTotalSpending(demoTotalSpending, shortName);
        }
        LocalRoomDatabase.databaseWriteExecutor.execute(() -> {
            mDemoGroupDao.updateDemoGroupTotalSpending(demoTotalSpending, shortName);
        });
    }


    // streaming service
    public LiveData<List<Stream>> getAllStreams() {
        if(ConnectionMode.connection){
            return globalProxy.getAllStreams();
        }else {
            return mAllStreams;
        }
    }
    public LiveData<Stream> findStreamByShortName(String streamShortName){
        if(ConnectionMode.connection){
            return globalProxy.findStreamByShortName(streamShortName);
        }else {
            return mStreamDao.getSelectedStream(streamShortName);
        }
    }
    public void updateStream(String shortName,String longName,String subscriptionPrice){
        if(ConnectionMode.connection){
            globalProxy.updateStream(shortName, longName, subscriptionPrice);
        }
        LocalRoomDatabase.databaseWriteExecutor.execute(() -> {
            mStreamDao.updateStreamLongName(longName, shortName);
            mStreamDao.updateStreamSubscription(subscriptionPrice, shortName);
        });
    }
    public void insert(Stream stream) {
        if(ConnectionMode.connection){
            globalProxy.insert(stream);
        }
        LocalRoomDatabase.databaseWriteExecutor.execute(() -> {
            mStreamDao.insert(stream);
        });
    }
    public void updateStreamCurrentRevenue(String streamCurrentRevenue, String shortName){
        if(ConnectionMode.connection){
            globalProxy.updateStreamCurrentRevenue(streamCurrentRevenue, shortName);
        }
        LocalRoomDatabase.databaseWriteExecutor.execute(() -> {
            mStreamDao.updateStreamCurrentRevenue(streamCurrentRevenue, shortName);
        });
    }
    public void updateStreamPreviousRevenue(String streamPreviousRevenue, String shortName){
        if(ConnectionMode.connection){
            globalProxy.updateStreamPreviousRevenue(streamPreviousRevenue, shortName);
        }
        LocalRoomDatabase.databaseWriteExecutor.execute(() -> {
            mStreamDao.updateStreamPreviousRevenue(streamPreviousRevenue, shortName);
        });
    }
    public void updateStreamTotalRevenue(String streamTotalRevenue, String shortName){
        if(ConnectionMode.connection){
            globalProxy.updateStreamTotalRevenue(streamTotalRevenue, shortName);
        }
        LocalRoomDatabase.databaseWriteExecutor.execute(() -> {
            mStreamDao.updateStreamTotalRevenue(streamTotalRevenue, shortName);
        });
    }
    public void updateLicensingById (String newNumber, String shortName){
        if(ConnectionMode.connection){
            globalProxy.updateLicensingById(newNumber, shortName);
        }
        LocalRoomDatabase.databaseWriteExecutor.execute(() -> {
            mStreamDao.updateLicensingById(newNumber, shortName);
        });
    }


    // offer
    public LiveData<List<Offer>> getAllOffers() {
        if(ConnectionMode.connection){
            return globalProxy.getAllOffers();
        }else {
            return mAllOffers;
        }
    }
    public LiveData<Offer> findOfferByKey(String offerKey){
        if(ConnectionMode.connection){
            return globalProxy.findOfferByKey(offerKey);
        }else {
            return mOfferDao.selectSpecifiedOffer(offerKey);
        }
    }
    public LiveData<Offer> findOfferByKey(String offerStream, String offerEventName, String offerEventYear){
        if(ConnectionMode.connection){
            return globalProxy.findOfferByKey(offerStream, offerEventName, offerEventYear);
        }else {
            return mOfferDao.selectWatchedOffer(offerStream, offerEventName, offerEventYear);
        }
    }
    public void insert(Offer offer) {
        if(ConnectionMode.connection){
            globalProxy.insert(offer);
        }
        LocalRoomDatabase.databaseWriteExecutor.execute(() -> {
            mOfferDao.insert(offer);
        });
    }
    public void deleteOffer(String offerKey) {
        if(ConnectionMode.connection){
            globalProxy.deleteOffer(offerKey);
        }
        LocalRoomDatabase.databaseWriteExecutor.execute(() -> {
            mOfferDao.delete(offerKey);
        });
    }
    public void deleteAllOffer(){
        if(ConnectionMode.connection){
            globalProxy.deleteAllOffer();
        }
        LocalRoomDatabase.databaseWriteExecutor.execute(() -> {
            mOfferDao.deleteAll();
        });
    }


    //demogroup stream
    public LiveData<List<DemoGroupStream>> getAllDemoGroupStreams() {
        if(ConnectionMode.connection){
            return globalProxy.getAllDemoGroupStreams();
        }
        return mAllDemoGroupStreams;
    }
    public void insert(DemoGroupStream demoGroupStream) {
        if(ConnectionMode.connection){
            globalProxy.insert(demoGroupStream);
        }
        LocalRoomDatabase.databaseWriteExecutor.execute(() -> {
            mDemoGroupStreamDao.insert(demoGroupStream);
        });
    }
    public LiveData<DemoGroupStream> getSelectedDemoGroupStream(String demoGroupStreamKey){
        if(ConnectionMode.connection){
            return globalProxy.getSelectedDemoGroupStream(demoGroupStreamKey);
        }
        return mDemoGroupStreamDao.getSelectedDemoGroupStream(demoGroupStreamKey);
    }
    public void updateDemoGroupStreamWatchViewerCount(String watchViewerCount, String demoGroupStreamKey){
        if(ConnectionMode.connection){
            globalProxy.updateDemoGroupStreamWatchViewerCount(watchViewerCount, demoGroupStreamKey);
        }
        LocalRoomDatabase.databaseWriteExecutor.execute(() -> {
            mDemoGroupStreamDao.updateDemoGroupStreamWatchViewerCount(watchViewerCount, demoGroupStreamKey);
        });
    }
    public void updateDemoGroupStreamWatchPPVCount(boolean watchedPPV, String demoGroupStreamKey){
        if(ConnectionMode.connection){
            globalProxy.updateDemoGroupStreamWatchPPVCount(watchedPPV, demoGroupStreamKey);
        }
        LocalRoomDatabase.databaseWriteExecutor.execute(() -> {
            mDemoGroupStreamDao.updateDemoGroupStreamWatchPPVCount(watchedPPV, demoGroupStreamKey);
        });
    }
    public void deleteAllDemoGroupStream(){
        if(ConnectionMode.connection){
            globalProxy.deleteAllDemoGroupStream();
        }
        LocalRoomDatabase.databaseWriteExecutor.execute(() -> {
            mDemoGroupStreamDao.deleteAll();
        });
    }

    // studio
    public LiveData<List<Studio>> getAllStudios() {
        if(ConnectionMode.connection){
            return globalProxy.getAllStudios();
        }
        return mAllStudios;
    }
    public LiveData<Studio> findStudioByShortName(String studioShortName){
        if(ConnectionMode.connection){
            return globalProxy.findStudioByShortName(studioShortName);
        }
        return mStudioDao.getSelectedStudio(studioShortName);
    }
    public void insert(Studio studio) {
        if(ConnectionMode.connection){
            globalProxy.insert(studio);
        }
        LocalRoomDatabase.databaseWriteExecutor.execute(() -> {
            mStudioDao.insert(studio);
        });
    }
    public void updateStudio (String longName, String shortName){
        if(ConnectionMode.connection){
            globalProxy.updateStudio(longName,shortName);
        }
        LocalRoomDatabase.databaseWriteExecutor.execute(() -> {
            mStudioDao.updateLongNameByShortName(longName, shortName);
        });
    }
    public void updateCurrentRevenueById (String newNumber, String shortName){
        if(ConnectionMode.connection){
            globalProxy.updateCurrentRevenueById(newNumber,shortName);
        }
        LocalRoomDatabase.databaseWriteExecutor.execute(() -> {
            mStudioDao.updateCurrentRevenueById(newNumber, shortName);
        });
    }
    public void updatePreviousRevenueById (String newNumber, String shortName){
        if(ConnectionMode.connection){
            globalProxy.updatePreviousRevenueById(newNumber, shortName);
        }
        LocalRoomDatabase.databaseWriteExecutor.execute(() -> {
            mStudioDao.updatePreviousRevenueById(newNumber, shortName);
        });
    }
    public void updateTotalRevenueById (String newNumber, String shortName){
        if(ConnectionMode.connection){
            globalProxy.updateTotalRevenueById(newNumber, shortName);
        }
        LocalRoomDatabase.databaseWriteExecutor.execute(() -> {
            mStudioDao.updateTotalRevenueById(newNumber, shortName);
        });
    }

    // event
    public LiveData<List<Event>> getAllEvents() {
        if(ConnectionMode.connection){
            return globalProxy.getAllEvents();
        }
        return mAllEvents;
    }
    public LiveData<Event> findEventById(String eventId){
        if(ConnectionMode.connection){
            return globalProxy.findEventById(eventId);
        }
        return mEventDao.getSelectedEvent(eventId);
    }
    public void updateEvent(String eventId,String duration,String licenseFee){
        if(ConnectionMode.connection){
            globalProxy.updateEvent(eventId, duration, licenseFee);
        }
        LocalRoomDatabase.databaseWriteExecutor.execute(() -> {
            mEventDao.updateEventDuration(duration,eventId);
            mEventDao.updateEventLicenseFee(licenseFee,eventId);
        });
    }
    public void insert(Event event) {
        if(ConnectionMode.connection){
            globalProxy.insert(event);
        }
        LocalRoomDatabase.databaseWriteExecutor.execute(() -> {
            mEventDao.insert(event);
        });
    }
}
