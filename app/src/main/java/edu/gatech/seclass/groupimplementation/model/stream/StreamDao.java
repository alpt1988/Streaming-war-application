package edu.gatech.seclass.groupimplementation.model.stream;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import edu.gatech.seclass.groupimplementation.model.demo.DemoGroup;

@Dao
public interface StreamDao {

    // allowing the insert of the same word multiple times by passing a
    // conflict resolution strategy
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Stream stream);

    @Query("DELETE FROM stream_table")
    void deleteAll();

    @Query("SELECT * FROM stream_table ORDER BY streamShortName ASC")
    List<Stream> getAll();

    @Query("SELECT * FROM stream_table ORDER BY streamShortName ASC")
    LiveData<List<Stream>> getAlphabetizedWords();

    @Query("SELECT * FROM stream_table WHERE streamShortName = :streamShortName")
    int selectByShortName(final String streamShortName);

    @Query("SELECT * FROM stream_table where streamShortName = :streamShortName")
    LiveData<Stream> getSelectedStream(String streamShortName);

    @Query("SELECT EXISTS (SELECT 1 FROM stream_table WHERE streamShortName = :streamShortName)")
    Boolean exists(String streamShortName);

    // update cost (liscencing)
    @Query("UPDATE stream_table SET streamLicensing=:newNumber WHERE streamShortName =:shortName")
    void updateLicensingById (String newNumber, String shortName);

    // update stream current revenue
    @Query("UPDATE stream_table SET streamCurrentRevenue=:streamCurrentRevenue WHERE streamShortName =:shortName")
    void updateStreamCurrentRevenue(String streamCurrentRevenue, String shortName);

    // update stream Previous revenue
    @Query("UPDATE stream_table SET streamPreviousRevenue=:streamPreviousRevenue WHERE streamShortName =:shortName")
    void updateStreamPreviousRevenue(String streamPreviousRevenue, String shortName);

    // update stream Previous revenue
    @Query("UPDATE stream_table SET streamTotalRevenue=:streamTotalRevenue WHERE streamShortName =:shortName")
    void updateStreamTotalRevenue(String streamTotalRevenue, String shortName);

    // update stream long name
    @Query("UPDATE stream_table SET streamLongName =:longName WHERE streamShortName =:shortName")
    void updateStreamLongName(String longName, String shortName);

    // update subscription price
    @Query("UPDATE stream_table SET streamSubscription=:subscriptionPrice WHERE streamShortName =:shortName")
    void updateStreamSubscription(String subscriptionPrice, String shortName);
}