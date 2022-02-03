package edu.gatech.seclass.groupimplementation.model.demo;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

// Annotations identify how each part of this class relates to an entry in the database. Room uses this information to generate code.
@Entity(tableName = "demoGroup_table")
public class DemoGroup {
    @PrimaryKey
    @NonNull
    private String demoGroupShortName;

    @NonNull
    private String demoGroupLongName;

    @NonNull
    private int demoAccounts;

    @NonNull
    private int demoCurrentSpending;

    @NonNull
    private int demoPreviousSpending;

    @NonNull
    private int demoTotalSpending;

//    public DemoGroup(@NonNull String demoGroupShortName, @NonNull String demoGroupLongName, @NonNull int demoAccounts) {
//        this.demoGroupShortName = demoGroupShortName;
//        this.demoGroupLongName = demoGroupLongName;
//        this.demoAccounts = demoAccounts;
//    }

    public DemoGroup(@NonNull String demoGroupShortName, @NonNull String demoGroupLongName, @NonNull int demoAccounts, @NonNull int demoCurrentSpending, @NonNull int demoPreviousSpending, @NonNull int demoTotalSpending) {
        this.demoGroupShortName = demoGroupShortName;
        this.demoGroupLongName = demoGroupLongName;
        this.demoAccounts = demoAccounts;
        this.demoCurrentSpending = demoCurrentSpending;
        this.demoPreviousSpending = demoPreviousSpending;
        this.demoTotalSpending = demoTotalSpending;
    }

    @NonNull
    public String getDemoGroupShortName() {
        return demoGroupShortName;
    }

    @NonNull
    public String getDemoGroupLongName() {
        return demoGroupLongName;
    }

    @NonNull
    public int getDemoAccounts() {
        return demoAccounts;
    }

    @NonNull
    public int getDemoCurrentSpending() {
        return demoCurrentSpending;
    }

    @NonNull
    public int getDemoPreviousSpending() {
        return demoPreviousSpending;
    }

    @NonNull
    public int getDemoTotalSpending() {
        return demoTotalSpending;
    }

    public void setDemoGroupShortName(@NonNull String demoGroupShortName) {
        this.demoGroupShortName = demoGroupShortName;
    }

    public void setDemoGroupLongName(@NonNull String demoGroupLongName) {
        this.demoGroupLongName = demoGroupLongName;
    }

    public void setDemoAccounts(int demoAccounts) {
        this.demoAccounts = demoAccounts;
    }

    public void setDemoCurrentSpending(int demoCurrentSpending) {
        this.demoCurrentSpending += demoCurrentSpending;
    }

    public void setDemoPreviousSpending(int demoPreviousSpending) {
        this.demoPreviousSpending = demoPreviousSpending;
    }

    public void setDemoTotalSpending(int demoTotalSpending) {
        this.demoTotalSpending = demoTotalSpending;
    }
}