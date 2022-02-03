package edu.gatech.seclass.groupimplementation.model.event;

import androidx.room.TypeConverter;

import java.math.BigInteger;

public class Converter {

    @TypeConverter
    public static String fromBigInteger(BigInteger x) {
        return x.toString();
    }

    @TypeConverter
    public static BigInteger toBigInteger(String x) {
        return new BigInteger(x);
    }
}

