package edu.gatech.seclass.groupimplementation.model.event;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import edu.gatech.seclass.groupimplementation.model.studio.Studio;

@Entity(tableName = "event_table")

/*        foreignKeys = {@ForeignKey(entity = Studio.class,
                parentColumns = "studioShortName",
                childColumns =  "eventStudioOwner",
                onDelete = ForeignKey.CASCADE)
        }

 */
public class Event {
    public void setEventID(@NonNull String eventID) {
        this.eventID = eventID;
    }

    @NonNull
    public String getEventID() {
        return eventID;
    }

    @PrimaryKey
    @NonNull
    private String eventID;

    @NonNull
    private String eventType;

    @NonNull
    private String eventName;

    @ColumnInfo(name = "eventStudioOwner", index = true)
    @NonNull
    private String eventStudioOwner;

    @NonNull
    private int eventYear;

    @NonNull
    private int eventDuration;

    @NonNull
    private int eventLicenseFee;

    public Event(
                 @NonNull String eventType,
                 @NonNull String eventName,
                 @NonNull int eventYear,
                 @NonNull int eventDuration,
                 @NonNull String eventStudioOwner,
                 @NonNull int eventLicenseFee) {
        this.eventID = eventName + eventYear;
        this.eventType = eventType;
        this.eventName = eventName;
        this.eventYear = eventYear;
        this.eventDuration = eventDuration;
        this.eventStudioOwner = eventStudioOwner;
        this.eventLicenseFee = eventLicenseFee;
    }


    @NonNull
    public String getEventType() {
        return eventType;
    }

    public void setEventType(@NonNull String eventType) {
        this.eventType = eventType;
    }

    @NonNull
    public String getEventName() {
        return eventName;
    }

    public void setEventName(@NonNull String eventName) {
        this.eventName = eventName;
    }

    @NonNull
    public String getEventStudioOwner() {
        return eventStudioOwner;
    }

    public void setEventStudioOwner(@NonNull String eventStudioOwner) {
        this.eventStudioOwner = eventStudioOwner;
    }

    public int getEventYear() {
        return eventYear;
    }

    public void setEventYear(int eventYear) {
        this.eventYear = eventYear;
    }

    public int getEventDuration() {
        return eventDuration;
    }

    public void setEventDuration(int eventDuration) {
        this.eventDuration = eventDuration;
    }

    public int getEventLicenseFee() {
        return eventLicenseFee;
    }

    public void setEventLicenseFee(int eventLicenseFee) {
        this.eventLicenseFee = eventLicenseFee;
    }
}