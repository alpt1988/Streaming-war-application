package edu.gatech.seclass.groupimplementation.model.stream;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "stream_table")
public class Stream {
    @PrimaryKey
    @NonNull
    private String streamShortName;

    @NonNull
    private String streamLongName;

    @NonNull
    private int streamSubscription;

    @NonNull
    //private double streamCurrentRevenue;
    private int streamCurrentRevenue;

    @NonNull
    //private double streamPreviousRevenue;
    private int streamPreviousRevenue;

    @NonNull
    private int streamLicensing;

    @NonNull
    private int streamTotalRevenue;

    public Stream(@NonNull String streamShortName, @NonNull String streamLongName, int streamSubscription) {
        this.streamShortName = streamShortName;
        this.streamLongName = streamLongName;
        this.streamSubscription = streamSubscription;
    }

    @NonNull
    public String getStreamShortName() {
        return streamShortName;
    }

    public void setStreamShortName(@NonNull String streamShortName) {
        this.streamShortName = streamShortName;
    }

    @NonNull
    public String getStreamLongName() {
        return streamLongName;
    }

    public void setStreamLongName(@NonNull String streamLongName) {
        this.streamLongName = streamLongName;
    }

    public int getStreamSubscription() {
        return streamSubscription;
    }

    public void setStreamSubscription(int streamSubscription) {
        this.streamSubscription = streamSubscription;
    }

//    public double getStreamCurrentRevenue() {
//        return streamCurrentRevenue;
//    }
    public int getStreamCurrentRevenue() {
        return streamCurrentRevenue;
    }

//    public void setStreamCurrentRevenue(double streamCurrentRevenue) {
//        this.streamCurrentRevenue = streamCurrentRevenue;
//    }
    public void setStreamCurrentRevenue(int streamCurrentRevenue) {
        this.streamCurrentRevenue = streamCurrentRevenue;
    }

//    public double getStreamPreviousRevenue() {
//        return streamPreviousRevenue;
//    }
    public int getStreamPreviousRevenue() {
        return streamPreviousRevenue;
    }

//    public void setStreamPreviousRevenue(double streamPreviousRevenue) {
//        this.streamPreviousRevenue = streamPreviousRevenue;
//    }
    public void setStreamPreviousRevenue(int streamPreviousRevenue) {
        this.streamPreviousRevenue = streamPreviousRevenue;
    }

    public int getStreamLicensing() {
        return streamLicensing;
    }

    public void setStreamLicensing(int streamLicensing) {
        this.streamLicensing = streamLicensing;
    }

    public void setStreamTotalRevenue(int streamTotalRevenue) {
        this.streamTotalRevenue = streamTotalRevenue;
    }

    public int getStreamTotalRevenue() {
        return streamTotalRevenue;
    }
}
