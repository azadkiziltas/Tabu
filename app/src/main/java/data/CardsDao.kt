package data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Dao
import androidx.room.Query

@Dao
interface CardsDao {
@Query("SELECT * FROM cards")
fun getAllCards(): List<Card>
}