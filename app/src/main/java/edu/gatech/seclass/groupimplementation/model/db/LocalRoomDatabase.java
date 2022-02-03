package edu.gatech.seclass.groupimplementation.model.db;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.gatech.seclass.groupimplementation.model.demo.DemoGroup;
import edu.gatech.seclass.groupimplementation.model.demo.DemoGroupDao;
import edu.gatech.seclass.groupimplementation.model.demo.DemoGroupStream;
import edu.gatech.seclass.groupimplementation.model.demo.DemoGroupStreamDao;
import edu.gatech.seclass.groupimplementation.model.event.Event;
import edu.gatech.seclass.groupimplementation.model.event.EventDao;
import edu.gatech.seclass.groupimplementation.model.offer.Offer;
import edu.gatech.seclass.groupimplementation.model.offer.OfferDao;
import edu.gatech.seclass.groupimplementation.model.stream.Stream;
import edu.gatech.seclass.groupimplementation.model.stream.StreamDao;
import edu.gatech.seclass.groupimplementation.model.studio.Studio;
import edu.gatech.seclass.groupimplementation.model.studio.StudioDao;


@Database(entities = {DemoGroup.class, Stream.class, Studio.class, Offer.class, Event.class, DemoGroupStream.class}, version = 1, exportSchema = false)
public abstract class LocalRoomDatabase extends RoomDatabase {

    public abstract DemoGroupDao demoGroupDao();
    public abstract StreamDao streamDao();
    public abstract StudioDao studioDao();
    public abstract EventDao eventDao();
    public abstract OfferDao offerDao();
    public abstract DemoGroupStreamDao demoGroupStreamDao();

    private static volatile LocalRoomDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    // Singleton
    public static LocalRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            Log.d("LocalRoomDatabase", "INSTANCE is null");
            synchronized (LocalRoomDatabase.class) {
                if (INSTANCE == null) {
                    Log.d("LocalRoomDatabase", "INSTANCE is null after synchronize");
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            LocalRoomDatabase.class, "local_database")
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * Override the onCreate method to populate the database.
     * For this sample, we clear the database every time it is created.
     */
    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
        }
    };
}