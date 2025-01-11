package ui

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.redstoneapps.tabu.R
import com.redstoneapps.tabu.databinding.ActivityGameBinding
import data.LocalDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import repo.AppPref
import ui.popup.ChangeTeamPopup
import ui.popup.GameFinishPopup
import ui.popup.GamePausePopup
import ui.popup.GameStartPopup


class GamePageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGameBinding
    private lateinit var database: LocalDatabase
    private lateinit var viewModel: GamePageViewModel
    private val appPref = AppPref(this)

    private lateinit var timeSetting: String
    private lateinit var passSetting: String
    private lateinit var firstTeamSetting: String
    private lateinit var finishScoreSetting: String
    private lateinit var teamOneSetting: String
    private lateinit var teamTwoSetting: String
    val TAG = "___"
    private lateinit var counterViewModel: CounterViewModel
    private var isPopupOpen = false

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            val popup = GameStartPopup(
                this@GamePageActivity,
                firstTeamSetting,
                teamOneSetting,
                teamTwoSetting,
                object : GameStartPopup.OnButtonClickListener {
                    override fun onStartButtonClick() {
                        if (firstTeamSetting.equals("1")) {
                            viewModel.aPlayCount()
                        } else {
                            viewModel.bPlayCount()
                        }
                        counterViewModel.startTimer()
                    }


                })
            popup.showAtCenter()
            getNewTabuCard()

        }
        
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        database = LocalDatabase.accessLocalDatabase(this)!!
        viewModel = ViewModelProvider(this)[GamePageViewModel::class.java]


        timeSetting = intent.getStringExtra("timeSetting")!!
        passSetting = intent.getStringExtra("passSetting")!!
        firstTeamSetting = intent.getStringExtra("firstTeamSetting")!!
        finishScoreSetting = intent.getStringExtra("finishScoreSetting")!!
        teamOneSetting = intent.getStringExtra("teamOneSetting")!!
        teamTwoSetting = intent.getStringExtra("teamTwoSetting")!!

        counterViewModel = CounterViewModel(timeSetting.toInt())


        setGameData(
            timeSetting,
            passSetting,
            firstTeamSetting,
            finishScoreSetting,
            teamOneSetting,
            teamTwoSetting
        )
        viewModel.setGameState(true)
        viewModel.setCurrentTeam(firstTeamSetting)
        viewModel.setPassCount(passSetting.toInt())

        viewModel.setTeamAPassCount(passSetting.toInt())
        viewModel.setTeamBPassCount(passSetting.toInt())


        observeData()
        buttonListeners()


    }


    @SuppressLint("SuspiciousIndentation")
    private fun buttonListeners() {
        binding.apply {
            viewModel.setGameState(false)
            pauseButton.setOnClickListener {
                counterViewModel.pauseTimer()
                if (!isPopupOpen) {
                    isPopupOpen = true
                    val popup = GamePausePopup(this@GamePageActivity,
                        object : GamePausePopup.OnButtonClickListener {
                            override fun onOkButtonClick() {
                                counterViewModel.resumeTimer()
                                viewModel.setGameState(true)
                                isPopupOpen = false
                            }

                            override fun onHomePageButtonClick() {
                                finish()
                            }
                        })
                    popup.showAtCenter()

                }

            }
            trueButton.setOnClickListener {
                viewModel.getNewTabuCard(database)
                viewModel.score(true)
            }
            tabuButton.setOnClickListener {
                viewModel.getNewTabuCard(database)
                viewModel.score(false)
            }
            passButtton.setOnClickListener {
                if (viewModel.currentTeam.value.equals("1")) {
                    Log.d(TAG, "buttonListeners: A")
                    if (viewModel.teamAPassCount.value!! > 0) {
                        viewModel.getNewTabuCard(database)
                        viewModel.setTeamAPassCount(viewModel.teamAPassCount.value!! - 1)

                    }
                } else {
                    Log.d(TAG, "buttonListeners: B")

                    if (viewModel.teamBPassCount.value!! > 0) {
                        viewModel.getNewTabuCard(database)
                        viewModel.setTeamBPassCount(viewModel.teamBPassCount.value!! - 1)
                    }
                }


            }
        }
    }

    private fun getNewTabuCard() {
        viewModel.getNewTabuCard(database)
    }

    private fun observeData() {

        counterViewModel.apply {
            getCount().observe(this@GamePageActivity, Observer { currentTime ->
                if (currentTime == 0) {
                    checkGameStatus()
                }
                binding.textViewTime.text = currentTime.toString()


                if (currentTime < 10) {
                    binding.progressBar.progressDrawable =
                        getDrawable(R.drawable.curved_progress_bar_secondary)
                } else {
                    binding.progressBar.progressDrawable =
                        getDrawable(R.drawable.curved_progress_bar)

                }


                binding.progressBar.progress =
                    (currentTime.toInt() * 100 / (timeSetting.toInt())).toInt()

            })
        }
        viewModel.apply {

            scoreTeamA.observe(
                this@GamePageActivity, Observer {
                    binding.textViewScoreTeamA.text = it.toString()
                }
            )
            scoreTeamB.observe(
                this@GamePageActivity, Observer {
                    binding.textViewScoreTeamB.text = it.toString()
                }
            )

            currentCard.observe(
                this@GamePageActivity, Observer {
                    binding.textViewWord.text = it.word
                    binding.textViewTabu1.text = it.tabu_1
                    binding.textViewTabu2.text = it.tabu_2
                    binding.textViewTabu3.text = it.tabu_3
                    binding.textViewTabu4.text = it.tabu_4
                    binding.textViewTabu5.text = it.tabu_5
                    CoroutineScope(Dispatchers.IO).launch {
                        database.cardsDao()
                            .setCount(currentCard.value!!.cardId, currentCard.value!!.count + 1)
                    }
                }
            )

        }


    }

    private fun checkGameStatus() {
        Log.d(TAG, "A: ${viewModel.teamAPlayCount.value}")
        Log.d(TAG, "B: ${viewModel.teamBPlayCount.value}")


        if (viewModel.teamAPlayCount.value!! > viewModel.teamBPlayCount.value!!) {
            viewModel.bPlayCount()
            changeTeam(false)

            val popup = ChangeTeamPopup(
                this@GamePageActivity,
                viewModel.currentTeam.value!!,
                teamOneSetting,
                teamTwoSetting,
                object : ChangeTeamPopup.OnButtonClickListener {
                    override fun onStartButtonClick() {
                        counterViewModel.stopTimer()
                        viewModel.getNewTabuCard(database)
                        counterViewModel.startTimer()
                    }

                })
            popup.showAtCenter()

        } else if (viewModel.teamAPlayCount.value!! < viewModel.teamBPlayCount.value!!) {
            viewModel.aPlayCount()
            changeTeam(true)
            val popup = ChangeTeamPopup(
                this@GamePageActivity,
                viewModel.currentTeam.value!!,
                teamOneSetting,
                teamTwoSetting,
                object : ChangeTeamPopup.OnButtonClickListener {
                    override fun onStartButtonClick() {
                        counterViewModel.stopTimer()
                        viewModel.getNewTabuCard(database)
                        counterViewModel.startTimer()
                    }

                })
            popup.showAtCenter()

        } else {
            if ((viewModel.scoreTeamA.value!! > viewModel.scoreTeamB.value!!) && viewModel.scoreTeamA.value!! >= finishScoreSetting.toInt()) {
                Log.d(TAG, "checkGameStatus: TAKIM A KAZANDI")


                showGameFinishPopup(teamOneSetting)

            } else if (viewModel.scoreTeamA.value!! < viewModel.scoreTeamB.value!! && viewModel.scoreTeamB.value!! >= finishScoreSetting.toInt()) {
                Log.d(TAG, "checkGameStatus: TAKIM B KAZANDI")

                showGameFinishPopup(teamTwoSetting)


            } else {
                if (firstTeamSetting.equals("1")) {
                    viewModel.aPlayCount()
                    changeTeam(true)
                    viewModel.getNewTabuCard(database)

                } else {
                    viewModel.bPlayCount()
                    changeTeam(false)
                    viewModel.getNewTabuCard(database)
                }
                val popup = ChangeTeamPopup(
                    this@GamePageActivity,
                    viewModel.currentTeam.value!!,
                    teamOneSetting,
                    teamTwoSetting,
                    object : ChangeTeamPopup.OnButtonClickListener {
                        override fun onStartButtonClick() {
                            counterViewModel.stopTimer()
                            counterViewModel.startTimer()
                        }

                    })
                popup.showAtCenter()

            }

        }
    }

    private fun showGameFinishPopup(winnerTeam: String) {

        val popup = GameFinishPopup(
            this@GamePageActivity,
            winnerTeam,
            object : GameFinishPopup.OnButtonClickListener {
                override fun onStartButtonClick() {
                    viewModel.resetGame()
                    firstTeamSetting = "2"
                    viewModel.getNewTabuCard(database)
                    val popup = GameStartPopup(
                        this@GamePageActivity,
                        firstTeamSetting,
                        teamOneSetting,
                        teamTwoSetting,
                        object : GameStartPopup.OnButtonClickListener {
                            override fun onStartButtonClick() {
                                if (firstTeamSetting.equals("1")) {
                                    viewModel.aPlayCount()
                                } else {
                                    viewModel.bPlayCount()
                                }
                                counterViewModel.startTimer()

                            }


                        })
                    popup.showAtCenter()

                }

                override fun onHomePageButtonClick() {
                    Log.d(TAG, "onHomePageButtonClick: ")
                    finish()
                }

            })
        popup.showAtCenter()
    }

    private fun setGameData(
        timeSetting: String,
        passSetting: String,
        firstTeamSetting: String,
        finishScoreSetting: String,
        teamOneSetting: String,
        teamTwoSetting: String,
    ) {
        binding.apply {
            textViewTeamA.text = teamOneSetting
            textViewTeamB.text = teamTwoSetting
            textViewTime.text = timeSetting
            textViewTime.text = timeSetting
            viewModel.passCount.observe(this@GamePageActivity, Observer {
                passButtton.text = "Pas ($it)"
            })
            if (firstTeamSetting.equals("1")) {
                changeTeam(true)
            } else {
                changeTeam(false)
            }
        }

    }

    private fun changeTeam(currentTeam: Boolean) {
        if (currentTeam) {
            viewModel.changeTeam()
            binding.passButtton.text = "Pas (${viewModel.teamAPassCount.value})"

            binding.teamOneBackground.background =
                getDrawable(R.drawable.top_corner_background_true_medium)
            binding.textViewScoreTeamA.setTextColor(getColor(R.color.trueColor))
            binding.teamTwoBackground.background =
                getDrawable(R.drawable.top_corner_background_secondary_medium)
            binding.textViewScoreTeamB.setTextColor(getColor(R.color.secondary))
        } else {
            binding.passButtton.text = "Pas (${viewModel.teamBPassCount.value})"
            viewModel.changeTeam()
            binding.teamTwoBackground.background =
                getDrawable(R.drawable.top_corner_background_true_medium)
            binding.textViewScoreTeamB.setTextColor(getColor(R.color.trueColor))
            binding.teamOneBackground.background =
                getDrawable(R.drawable.top_corner_background_secondary_medium)
            binding.textViewScoreTeamA.setTextColor(getColor(R.color.secondary))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}