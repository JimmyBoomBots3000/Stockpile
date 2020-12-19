package com.jrichmond.stockpile;

        import android.content.Context;
        import androidx.room.Database;
        import androidx.room.Room;
        import androidx.room.RoomDatabase;

@Database(entities = {Item.class, Count.class, Location.class, PhoneNumber.class}, version = 1)
public abstract class ItemDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "stockpile.db";

    private static ItemDatabase mItemDatabase;

    // Singleton
    public static ItemDatabase getInstance(Context context) {
        if (mItemDatabase == null) {
            mItemDatabase = Room.databaseBuilder(context, ItemDatabase.class,
                    DATABASE_NAME).allowMainThreadQueries().build();
        }
        return mItemDatabase;
    }

    public abstract ItemDao itemDao();
    public abstract LocationDao locationDao();
    public abstract CountDao countDao();
    public abstract PhoneDao phoneDao();

}

