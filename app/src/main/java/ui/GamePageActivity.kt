package ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.Toast
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



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val decorView = window.decorView
        val uiOptions = (View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN)
        decorView.systemUiVisibility = uiOptions


        database = LocalDatabase.accessLocalDatabase(this)!!
        viewModel = ViewModelProvider(this)[GamePageViewModel::class.java]


        timeSetting = intent.getStringExtra("timeSetting")!!
        passSetting = intent.getStringExtra("passSetting")!!
        firstTeamSetting = intent.getStringExtra("firstTeamSetting")!!
        finishScoreSetting = intent.getStringExtra("finishScoreSetting")!!
        teamOneSetting = intent.getStringExtra("teamOneSetting")!!
        teamTwoSetting = intent.getStringExtra("teamTwoSetting")!!

        counterViewModel = CounterViewModel(timeSetting.toInt())


        Log.d("___", "onCreate: $timeSetting")
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


        Log.d("___", "onCreate: $firstTeamSetting")
        observeData()
        getNewTabuCard()
        buttonListeners()
        val popup = GameStartPopup(
            this,
            firstTeamSetting,
            teamOneSetting,
            teamTwoSetting,
            object : GameStartPopup.OnButtonClickListener {
                override fun onOkButtonClick() {
                    counterViewModel.startTimer()
                    Log.d(TAG, "onOkButtonClick: ")
                }
            })
        popup.show()
        popup.setOnKeyListener { _, keyCode, _ ->
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                finish() // Aktiviteyi kapat
                true
            } else {
                false
            }
        }
    }

    @SuppressLint("SuspiciousIndentation")
    private fun buttonListeners() {
        binding.apply {
            viewModel.setGameState(false)
            pauseButton.setOnClickListener {
                counterViewModel.pauseTimer()
                val popup = GamePausePopup(this@GamePageActivity,
                        object : GamePausePopup.OnButtonClickListener {
                            override fun onOkButtonClick() {
                                counterViewModel.resumeTimer()
                                viewModel.setGameState(true)
                            }
                        })
                    popup.show()
                popup.setOnKeyListener { _, keyCode, _ ->
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        counterViewModel.resumeTimer()
                        popup.dismiss()
                        true
                    } else {
                        false
                    }
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
                if (viewModel.passCount.value!! > 0) {
                    viewModel.getNewTabuCard(database)
                    viewModel.setPassCount(viewModel.passCount.value!! - 1)
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

                Log.d("___", "onCreate: ${currentTime.toInt()}")
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
                    binding.textViewWord.text = it.word + " - " + it.count
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
                changeTeam("1")
            } else {
                changeTeam("2")

            }
        }

    }

    private fun changeTeam(currentTeam: String) {
        if (currentTeam.equals("1")) {
            binding.teamOneBackground.background =
                getDrawable(R.drawable.top_corner_background_true_medium)
            binding.textViewScoreTeamA.setTextColor(getColor(R.color.trueColor))
            binding.teamTwoBackground.background =
                getDrawable(R.drawable.top_corner_background_secondary_medium)
            binding.textViewScoreTeamB.setTextColor(getColor(R.color.secondary))
        } else {
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