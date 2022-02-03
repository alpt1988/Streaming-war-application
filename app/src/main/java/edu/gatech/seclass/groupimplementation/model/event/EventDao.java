package edu.gatech.seclass.groupimplementation.model.event;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface EventDao {
    // allowing the insert of the same word multiple times by passing a
    // conflict resolution strategy
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Event event);

    @Update
    public void updateEvent(Event event);

    @Query("DELETE FROM event_table WHERE eventID = :eventId")
    void delete(final String eventId);

    @Query("DELETE FROM event_table")
    void deleteAll();

    @Query("SELECT * FROM event_table ORDER BY eventID ASC")
    LiveData<List<Event>> getAlphabetizedWords();

    @Query("SELECT * FROM event_table ORDER BY eventID ASC")
    List<Event> getAll();

    @Query("SELECT * FROM event_table where eventID = :eventID")
    LiveData<Event> getSelectedEvent(String eventID);

    // update stream long name
    @Query("UPDATE event_table SET eventDuration =:duration WHERE eventID =:eventId")
    void updateEventDuration(String duration, String eventId);

    // update subscription price
    @Query("UPDATE event_table SET eventLicenseFee=:licenseFee WHERE eventID =:eventId")
    void updateEventLicenseFee(String licenseFee, String eventId);

}
