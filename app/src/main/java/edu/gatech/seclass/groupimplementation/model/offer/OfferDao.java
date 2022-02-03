package edu.gatech.seclass.groupimplementation.model.offer;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface OfferDao {

    // allowing the insert of the same word multiple times by passing a
    // conflict resolution strategy
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Offer offer);

    @Query("DELETE FROM offer_table")
    void deleteAll();

    @Query("SELECT * FROM offer_table ORDER BY offerEventName ASC")
    LiveData<List<Offer>> getAlphabetizedWords();

    @Query("SELECT * FROM offer_table ORDER BY offerEventName ASC")
    List<Offer> getAll();

    @Query("DELETE FROM offer_table WHERE offerKey = :offerKey")
    void delete(final String offerKey);

    @Query("SELECT * FROM offer_table WHERE offerKey = :offerKey")
    LiveData<Offer> selectSpecifiedOffer(final String offerKey);

    @Query("SELECT * FROM offer_table WHERE offerStream = :offerStream AND offerEventName = :offerEventName AND offerEventYear = :offerEventYear")
    LiveData<Offer> selectWatchedOffer(final String offerStream, final String offerEventName, final String offerEventYear);

}
