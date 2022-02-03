package edu.gatech.seclass.groupimplementation.model.demo;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "demoGroupStream_table")
public class DemoGroupStream {
    @PrimaryKey
    @NonNull
    public String demoGroupStreamKey;

    @NonNull
    private int watchViewerCount;

    @NonNull
    private boolean watchPPV;

    public DemoGroupStream(String demoGroupStreamKey, int watchViewerCount) {
        this.demoGroupStreamKey = demoGroupStreamKey;
        this.watchViewerCount = watchViewerCount;
        this.watchPPV = false;
    }

    public String getDemoGroupStreamKey(){
        return demoGroupStreamKey;
    }
    public int getWatchViewerCount() {
        return watchViewerCount;
    }

    public void setWatchViewerCount(int watchViewerCount) {
        this.watchViewerCount = watchViewerCount;
    }

    public boolean getWatchPPV(){
        return watchPPV;
    }

    public void setWatchPPV(boolean setWatch){
        this.watchPPV = setWatch;
    }
}
