package data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Dao
import androidx.room.Query

@Dao
interface CardsDao {

@Query("SELECT * FROM cards ORDER BY RANDOM()")
fun getRandomCards(): List<Card>


@Query("UPDATE cards SET card_count = :count WHERE card_id = :cardId")
fun setCount(cardId:Int,count:Int)





}