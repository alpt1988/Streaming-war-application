package edu.gatech.seclass.groupimplementation.model.offer;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "offer_table")
public class Offer {
    @PrimaryKey
    @NonNull
    public String offerKey;

    @NonNull
    private String offerStream;
    @NonNull
    private String offerType;
    @NonNull
    private String offerEventName;
    @NonNull
    private int offerEventYear;
    @NonNull
    private int offerPrice;

    public Offer() {

    }

    public Offer(String offerTypeValue, String offerStreamValue, String offerEventNameValue, int offerEventYearValue) {
        this.offerType = offerTypeValue;
        this.offerStream = offerStreamValue;
        this.offerEventName = offerEventNameValue;
        this.offerEventYear = offerEventYearValue;
        this.offerKey = getOfferKey();
    }

    public String getOfferStream() {
        return this.offerStream;
    }

    public String getOfferType() {
        return this.offerType;
    }

    public String getOfferEventName() {
        return this.offerEventName;
    }

    public int getOfferEventYear() {
        return this.offerEventYear;
    }

    public int getOfferPrice() {
        return this.offerPrice;
    }


    public String getOfferKey() {
        return this.offerStream + "," + this
                .offerType + "," + this.offerEventName + "," + this.offerEventYear;
    }

    public void setOfferStream(@NonNull String offerStream) {
        this.offerStream = offerStream;
    }

    public void setOfferType(@NonNull String offerType) {
        this.offerType = offerType;
    }

    public void setOfferEventName(@NonNull String offerEventName) {
        this.offerEventName = offerEventName;
    }

    public void setOfferEventYear(int offerEventYear) {
        this.offerEventYear = offerEventYear;
    }

    public void setOfferPrice(int offerPrice) {
        this.offerPrice = offerPrice;
    }
}
