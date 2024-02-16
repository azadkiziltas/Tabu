package ui

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import data.Card
import data.LocalDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GamePageViewModel : ViewModel() {

    private var cardList: MutableList<Card> = mutableListOf()

    private val _scoreTeamA = MutableLiveData<Int>(0)
    var scoreTeamA: LiveData<Int> = _scoreTeamA

    private val _scoreTeamB = MutableLiveData<Int>(0)
    var scoreTeamB: LiveData<Int> = _scoreTeamB

    private val _gameState = MutableLiveData<Boolean>()
    var gameState: LiveData<Boolean> = _gameState

    private val _currentCard = MutableLiveData<Card>()
    var currentCard: LiveData<Card> = _currentCard

    private val _currentTeam = MutableLiveData<String>()
    var currentTeam: LiveData<String> = _currentTeam

    private val _passCount = MutableLiveData<Int>()
    var passCount: LiveData<Int> = _passCount



    val TAG = "___"

    fun setGameState(gameState: Boolean) {
        _gameState.value = gameState
    }



    fun setCurrentTeam(currentTeam: String) {
        _currentTeam.value = currentTeam
    }

    fun setPassCount(passCount: Int) {
        _passCount.value = passCount
    }



    fun getNewTabuCard(database: LocalDatabase) {
        if (cardList.isEmpty()) {
            CoroutineScope(Dispatchers.IO).launch {
                cardList.addAll(database.cardsDao().getRandomCards().sortedBy { it.count })
                for (card in cardList) {

                    Log.d(TAG, "getNewTabuCard: ${card.word} - ${card.count}")
                }
                _currentCard.postValue(cardList.first())
                cardList.removeAt(0)

            }
        } else {
            _currentCard.postValue(cardList.first())
            cardList.removeAt(0)
        }
    }

    fun score(boolean: Boolean) {
        if (boolean) {
            if (currentTeam.value.equals("1")) {
                _scoreTeamA.value = _scoreTeamA.value!! + 1
            } else {
                _scoreTeamB.value = _scoreTeamB.value!! + 1
            }
        } else {
            if (currentTeam.value.equals("1")) {
                _scoreTeamA.value = _scoreTeamA.value!! - 1
            } else {
                _scoreTeamB.value = _scoreTeamB.value!! - 1
            }
        }


    }

}
