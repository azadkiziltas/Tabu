package data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Card::class], version = 1)
abstract class LocalDatabase : RoomDatabase() {

    abstract fun cardsDao():CardsDao

    companion object {
        var INSTANCE: LocalDatabase? = null
        fun accessLocalDatabase(context: Context): LocalDatabase? {
            if (INSTANCE == null) {
                synchronized(LocalDatabase::class) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        LocalDatabase::class.java,
                        name = "tabuCards.sqlite"
                    ).createFromAsset("tabuCards.sqlite").build()

                }
            }

            return INSTANCE
        }

    }
}
