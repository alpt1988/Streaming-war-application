package edu.gatech.seclass.groupimplementation.model.demo;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import edu.gatech.seclass.groupimplementation.model.event.Event;

@Dao
public interface DemoGroupDao {

    // allowing the insert of the same word multiple times by passing a
    // conflict resolution strategy
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(DemoGroup demoGroup);

    // Insert a batch of demos
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertBatch(List<DemoGroup> demoGroups);

    @Query("DELETE FROM demoGroup_table")
    void deleteAll();

    @Query("DELETE FROM demoGroup_table WHERE demoGroupShortName = :demoGroupShortName")
    int delete(final String demoGroupShortName);

    @Query("SELECT * FROM demoGroup_table ORDER BY demoGroupShortName ASC")
    LiveData<List<DemoGroup>> getAlphabetizedWords();

    @Query("SELECT * FROM demoGroup_table ORDER BY demoGroupShortName ASC")
    List<DemoGroup> getAll();

    @Query("SELECT * FROM demoGroup_table where demoGroupShortName = :shortName")
    LiveData<DemoGroup> getSelectedDemoGroup(String shortName);

    // update demo long name
    @Query("UPDATE demoGroup_table SET demoGroupLongName=:longName, demoAccounts=:numAccounts WHERE demoGroupShortName =:shortName")
    void updateDemo(String longName, String numAccounts, String shortName);

    // update demo current spending
    @Query("UPDATE demoGroup_table SET demoCurrentSpending=:demoCurrentSpending WHERE demoGroupShortName =:shortName")
    void updateDemoGroupCurrentSpending(String demoCurrentSpending, String shortName);

    // update demo previous spending
    @Query("UPDATE demoGroup_table SET demoPreviousSpending=:demoPreviousSpending WHERE demoGroupShortName =:shortName")
    void updateDemoGroupPreviousSpending(String demoPreviousSpending, String shortName);

    // update demo total spending
    @Query("UPDATE demoGroup_table SET demoTotalSpending=:demoTotalSpending WHERE demoGroupShortName =:shortName")
    void updateDemoGroupTotalSpending(String demoTotalSpending, String shortName);
}
