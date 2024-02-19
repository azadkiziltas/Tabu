package repo

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first

class AppPref(var context:Context) {
    val Context.ds : DataStore<Preferences> by preferencesDataStore("settings")

    companion object {
        val TIME = stringPreferencesKey("time")
        val PASS = stringPreferencesKey("pass")
        val FINISH_SCORE = stringPreferencesKey("finishScore")
        val FIRST_TEAM = stringPreferencesKey("firstTeam")
        val TEAM_ONE = stringPreferencesKey("teamOne")
        val TEAM_TWO = stringPreferencesKey("teamTwo")
    }

    suspend fun saveTimeSetting(time:String){
        context.ds.edit {
            it[TIME] = time
        }
    }
    suspend fun getTimeSetting(): String {
        val p = context.ds.data.first()
        return p[TIME] ?: "90"
    }

    suspend fun savePassSetting(pass:String){
        context.ds.edit {
            it[PASS] = pass
        }
    }
    suspend fun getPassSetting(): String {
        val p = context.ds.data.first()
        return p[PASS] ?: "3"
    }

    suspend fun saveFinishScoreSetting(finishScore:String){
        context.ds.edit {
            it[FINISH_SCORE] = finishScore
        }
    }
    suspend fun getFinishScoreSetting(): String {
        val p = context.ds.data.first()
        return p[FINISH_SCORE] ?: "16"
    }

    suspend fun saveFirstTeamSetting(firstTeam:String){
        context.ds.edit {
            it[FIRST_TEAM] = firstTeam
        }
    }
    suspend fun getFirstTeamSetting(): String {
        val p = context.ds.data.first()
        return p[FIRST_TEAM] ?: "1"
    }

    suspend fun saveTeamOneSetting(teamOne:String){
        context.ds.edit {
            it[TEAM_ONE] = teamOne
        }
    }
    suspend fun getTeamOneSetting(): String {
        val p = context.ds.data.first()
        return p[TEAM_ONE] ?: "TAKIM A"
    }


    suspend fun saveTeamTwoSetting(teamTwo:String){
        context.ds.edit {
            it[TEAM_TWO] = teamTwo
        }
    }
    suspend fun getTeamTwoSetting(): String {
        val p = context.ds.data.first()
        return p[TEAM_TWO] ?: "TAKIM B"
    }


}