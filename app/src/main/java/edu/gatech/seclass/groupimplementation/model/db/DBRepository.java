package edu.gatech.seclass.groupimplementation.model.db;

import androidx.lifecycle.LiveData;

import java.util.List;

import edu.gatech.seclass.groupimplementation.model.demo.DemoGroup;
import edu.gatech.seclass.groupimplementation.model.demo.DemoGroupStream;
import edu.gatech.seclass.groupimplementation.model.event.Event;
import edu.gatech.seclass.groupimplementation.model.offer.Offer;
import edu.gatech.seclass.groupimplementation.model.stream.Stream;
import edu.gatech.seclass.groupimplementation.model.studio.Studio;

public interface DBRepository {
    LiveData<List<DemoGroup>> getAllDemoGroups();
    LiveData<DemoGroup> findDemoGroupByShortName(String demoGroupShortName);
    void updateDemo(String shortName,String longName,String demoAccounts);
    void insert(DemoGroup demoGroup);
    void updateDemoGroupCurrentSpending(String demoCurrentSpending, String shortName);
    void updateDemoGroupPreviousSpending(String demoPreviousSpending, String shortName);
    void updateDemoGroupTotalSpending(String demoTotalSpending, String shortName);

    LiveData<List<Stream>> getAllStreams();
    LiveData<Stream> findStreamByShortName(String streamShortName);
    void updateStream(String shortName,String longName,String subscriptionPrice);
    void insert(Stream stream);
    void updateStreamCurrentRevenue(String streamCurrentRevenue, String shortName);
    void updateStreamPreviousRevenue(String streamPreviousRevenue, String shortName);
    void updateStreamTotalRevenue(String streamTotalRevenue, String shortName);
    void updateLicensingById (String newNumber, String shortName);

    LiveData<List<Offer>> getAllOffers();
    LiveData<Offer> findOfferByKey(String offerKey);
    LiveData<Offer> findOfferByKey(String offerStream, String offerEventName, String offerEventYear);
    void insert(Offer offer);
    void deleteOffer(String offerKey);
    void deleteAllOffer();

    LiveData<List<DemoGroupStream>> getAllDemoGroupStreams();
    LiveData<DemoGroupStream> getSelectedDemoGroupStream(String demoGroupStreamKey);
    void updateDemoGroupStreamWatchViewerCount(String watchViewerCount, String demoGroupStreamKey);
    void updateDemoGroupStreamWatchPPVCount(boolean watchedPPV, String demoGroupStreamKey);
    void deleteAllDemoGroupStream();

    LiveData<List<Studio>> getAllStudios();
    LiveData<Studio> findStudioByShortName(String studioShortName);
    void insert(Studio studio);
    void updateStudio (String longName, String shortName);
    void updateCurrentRevenueById (String newNumber, String shortName);
    void updatePreviousRevenueById (String newNumber, String shortName);
    void updateTotalRevenueById (String newNumber, String shortName);

    LiveData<List<Event>> getAllEvents();
    LiveData<Event> findEventById(String eventId);
    void updateEvent(String eventId,String duration,String licenseFee);
    void insert(Event event);
}
