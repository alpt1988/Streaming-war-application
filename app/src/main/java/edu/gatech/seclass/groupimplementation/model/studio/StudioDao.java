package edu.gatech.seclass.groupimplementation.model.studio;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

import edu.gatech.seclass.groupimplementation.model.event.Event;

@Dao
public interface StudioDao {

    // allowing the insert of the same word multiple times by passing a
    // conflict resolution strategy
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Studio studio);

    @Query("DELETE FROM studio_table")
    void deleteAll();

    @Query("SELECT * FROM studio_table ORDER BY studioShortName ASC")
    LiveData<List<Studio>> getAlphabetizedWords();

    @Transaction
    @Query("SELECT * FROM studio_table")
    public List<StudioEventLine> getStudioWithEvents();

    @Query("SELECT * FROM studio_table ORDER BY studioShortName ASC")
    List<Studio> getAll();

    @Query("DELETE FROM studio_table WHERE studioShortName = :studioShortName")
    void delete(final String studioShortName);

    @Query("SELECT * FROM studio_table where studioShortName = :studioShortName")
    LiveData<Studio> getSelectedStudio(String studioShortName);

    // update income (liscencing)
    @Query("UPDATE studio_table SET studioTotalRevenue=:newNumber WHERE studioShortName =:shortName")
    void updateTotalRevenueById (String newNumber, String shortName);

    // update current income (liscencing)
    @Query("UPDATE studio_table SET studioCurrentRevenue=:newNumber WHERE studioShortName =:shortName")
    void updateCurrentRevenueById (String newNumber, String shortName);

    // update current income (liscencing)
    @Query("UPDATE studio_table SET studioPreviousRevenue=:newNumber WHERE studioShortName =:shortName")
    void updatePreviousRevenueById (String newNumber, String shortName);

    // update long name
    @Query("UPDATE studio_table SET studioLongName=:longName WHERE studioShortName =:shortName")
    void updateLongNameByShortName (String longName, String shortName);
}
