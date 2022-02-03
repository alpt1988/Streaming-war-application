package edu.gatech.seclass.groupimplementation.model.studio;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import edu.gatech.seclass.groupimplementation.model.event.Event;

@Entity(tableName = "studio_table")
public class Studio {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "studioShortName", index = true)
    private String studioShortName;

    @NonNull
    private String studioLongName;

    @NonNull
    private int studioCurrentRevenue;

    @NonNull
    private int studioPreviousRevenue;

    @NonNull
    private int studioTotalRevenue;

    public Studio(@NonNull String studioShortName, @NonNull String studioLongName) {
        this.studioShortName = studioShortName;
        this.studioLongName = studioLongName;
    }

    @NonNull
    public String getStudioShortName() {
        return studioShortName;
    }

    public void setStudioShortName(@NonNull String studioShortName) {
        this.studioShortName = studioShortName;
    }

    @NonNull
    public String getStudioLongName() {
        return studioLongName;
    }

    public void setStudioLongName(@NonNull String studioLongName) {
        this.studioLongName = studioLongName;
    }


    public int getStudioCurrentRevenue() {
        return studioCurrentRevenue;
    }

    public void setStudioCurrentRevenue(int studioCurrentRevenue) {
        this.studioCurrentRevenue = studioCurrentRevenue;
    }

    public int getStudioPreviousRevenue() {
        return studioPreviousRevenue;
    }

    public void setStudioPreviousRevenue(int studioPreviousRevenue) {
        this.studioPreviousRevenue = studioPreviousRevenue;
    }

    public int getStudioTotalRevenue() {
        return studioTotalRevenue;
    }

    public void setStudioTotalRevenue(int studioTotalRevenue) {
        this.studioTotalRevenue = studioTotalRevenue;
    }
}