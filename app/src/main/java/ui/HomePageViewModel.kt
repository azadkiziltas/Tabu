package ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import data.LocalDatabase
import repo.AppPref
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class HomePageViewModel : ViewModel() {

    private val _timeSetting = MutableLiveData<String>()
    var timeSetting: LiveData<String> = _timeSetting

    private val _passSetting = MutableLiveData<String>()
    var passSetting: LiveData<String> = _passSetting

    private val _finishScoreSetting = MutableLiveData<String>()
    var finishScoreSetting: LiveData<String> = _finishScoreSetting

    private val _firstTeamSetting = MutableLiveData<String>()
    var firstTeamSetting: LiveData<String> = _firstTeamSetting

    private val _teamOneSetting = MutableLiveData<String>()
    var teamOneSetting: LiveData<String> = _teamOneSetting


    private val _teamTwoSetting = MutableLiveData<String>()
    var teamTwoSetting: LiveData<String> = _teamTwoSetting




    fun loadSettings(appPref: AppPref) {
        CoroutineScope(Dispatchers.Main).launch {
            _timeSetting.value = appPref.getTimeSetting()
            _passSetting.value = appPref.getPassSetting()
            _finishScoreSetting.value = appPref.getFinishScoreSetting()
            _firstTeamSetting.value = appPref.getFirstTeamSetting()
            _teamOneSetting.value = appPref.getTeamOneSetting()
            _teamTwoSetting.value = appPref.getTeamTwoSetting()
        }
    }












    fun setTime(time: String,appPref: AppPref) {
        CoroutineScope(Dispatchers.Main).launch {
            appPref.saveTimeSetting(time)
            _timeSetting.value = appPref.getTimeSetting()

        }
    }

    fun setPassSetting(pass: String,appPref: AppPref) {
        CoroutineScope(Dispatchers.Main).launch {
            appPref.savePassSetting(pass)
            _passSetting.value = appPref.getPassSetting()

        }
    }

    fun setFinishScoreSetting(finishScore: String,appPref: AppPref) {
        CoroutineScope(Dispatchers.Main).launch {
            appPref.saveFinishScoreSetting(finishScore)
            _finishScoreSetting.value = appPref.getFinishScoreSetting()

        }
    }

    fun setFirstTeamSetting(firstTeam: String,appPref: AppPref) {
        CoroutineScope(Dispatchers.Main).launch {
            appPref.saveFirstTeamSetting(firstTeam)
            _firstTeamSetting.value = appPref.getFirstTeamSetting()
        }
    }

    fun setTeamOneSetting(teamOne: String,appPref: AppPref) {
        CoroutineScope(Dispatchers.Main).launch {
            appPref.saveTeamOneSetting(teamOne)
            _teamOneSetting.value = appPref.getTeamOneSetting()

        }
    }

        fun setTeamTwoSetting(teamTwo: String,appPref: AppPref) {
        CoroutineScope(Dispatchers.Main).launch {
            appPref.saveTeamTwoSetting(teamTwo)
            _teamTwoSetting.value = appPref.getTeamTwoSetting()

        }
    }



}