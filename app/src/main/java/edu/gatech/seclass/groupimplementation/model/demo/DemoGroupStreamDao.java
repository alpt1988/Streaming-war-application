package edu.gatech.seclass.groupimplementation.model.demo;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import edu.gatech.seclass.groupimplementation.model.event.Event;

@Dao
public interface DemoGroupStreamDao {

    // allowing the insert of the same word multiple times by passing a
    // conflict resolution strategy
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(DemoGroupStream demoGroupStream);

    @Query("DELETE FROM demoGroupStream_table")
    void deleteAll();

    @Query("SELECT * FROM demoGroupStream_table ORDER BY demoGroupStreamKey ASC")
    LiveData<List<DemoGroupStream>> getAlphabetizedWords();

    @Query("SELECT * FROM demoGroupStream_table where demoGroupStreamKey = :demoGroupStreamKey")
    LiveData<DemoGroupStream> getSelectedDemoGroupStream(final String demoGroupStreamKey);

    // update demo+stream watchViewerCount
    @Query("UPDATE demoGroupStream_table SET watchViewerCount=:watchViewerCount WHERE demoGroupStreamKey =:demoGroupStreamKey")
    void updateDemoGroupStreamWatchViewerCount(String watchViewerCount, String demoGroupStreamKey);

    @Query("UPDATE demoGroupStream_table SET watchPPV=:watchedPPV WHERE demoGroupStreamKey =:demoGroupStreamKey")
    void updateDemoGroupStreamWatchPPVCount(boolean watchedPPV, String demoGroupStreamKey);
}
