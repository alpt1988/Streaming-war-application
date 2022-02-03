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
import edu.gatech.seclass.groupimplementation.model.event.Event;
import edu.gatech.seclass.groupimplementation.model.event.EventDao;
import edu.gatech.seclass.groupimplementation.model.offer.Offer;
import edu.gatech.seclass.groupimplementation.model.offer.OfferDao;
import edu.gatech.seclass.groupimplementation.model.stream.Stream;
import edu.gatech.seclass.groupimplementation.model.stream.StreamDao;
import edu.gatech.seclass.groupimplementation.model.studio.Studio;
import edu.gatech.seclass.groupimplementation.model.studio.StudioDao;


@Database(entities = {DemoGroup.class}, version = 1, exportSchema = false)
public abstract class GlobalArchiveRoomDatabase extends RoomDatabase {

    public abstract DemoGroupDao demoGroupDao();
    private static volatile GlobalArchiveRoomDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    // Singleton
    public static GlobalArchiveRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            Log.d("GlobalArchiveRoomDatabase", "INSTANCE is null");
            synchronized (GlobalArchiveRoomDatabase.class) {
                if (INSTANCE == null) {
                    Log.d("GlobalArchiveRoomDatabase", "INSTANCE is null after synchronize");
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            GlobalArchiveRoomDatabase.class, "global_archive_database")
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