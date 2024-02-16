package data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull
import java.io.Serializable

@Entity(tableName = "cards")
data class Card(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "card_id") @NotNull var cardId:Int,
    @ColumnInfo(name = "word") @NotNull var word:String,
    @ColumnInfo(name = "tabu_1") @NotNull var tabu_1:String,
    @ColumnInfo(name = "tabu_2") @NotNull var tabu_2:String,
    @ColumnInfo(name = "tabu_3") @NotNull var tabu_3:String,
    @ColumnInfo(name = "tabu_4") @NotNull var tabu_4:String,
    @ColumnInfo(name = "tabu_5") @NotNull var tabu_5:String,
    @ColumnInfo(name = "card_count") @NotNull  var count:Int
)