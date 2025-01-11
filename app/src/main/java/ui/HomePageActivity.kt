package ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.redstoneapps.tabu.R
import com.redstoneapps.tabu.databinding.ActivityHomePageBinding
import data.LocalDatabase
import repo.AppPref


class HomePageActivity : AppCompatActivity() {

    private val appPref = AppPref(this)
    private lateinit var binding: ActivityHomePageBinding
    private lateinit var database: LocalDatabase
    private lateinit var viewModel: HomePageViewModel



    val TAG = "___"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomePageBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)







        database = LocalDatabase.accessLocalDatabase(this)!!
        viewModel = ViewModelProvider(this)[HomePageViewModel::class.java]




        binding.apply {
            startButton.setOnClickListener {

                viewModel.apply {
                    setTeamOneSetting(editText1.text.toString(),appPref)
                    setTeamTwoSetting(editText2.text.toString(),appPref)

                    val intent = Intent(this@HomePageActivity, GamePageActivity::class.java)
                    intent.putExtra("timeSetting",viewModel.timeSetting.value)
                    intent.putExtra("passSetting",viewModel.passSetting.value)
                    intent.putExtra("firstTeamSetting",viewModel.firstTeamSetting.value)
                    intent.putExtra("finishScoreSetting",viewModel.finishScoreSetting.value)
                    intent.putExtra("teamOneSetting",editText1.text.toString())
                    intent.putExtra("teamTwoSetting",editText2.text.toString())
                    startActivity(intent)

                }


            }

            editText1.setOnEditorActionListener { _, actionId, event ->
                if (actionId == EditorInfo.IME_ACTION_DONE ||
                    (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN)) {

                    editText2.requestFocus()
                    editText2.post {
                        editText2.setSelection(editText2.text!!.length)
                    }

                    return@setOnEditorActionListener true
                }

                return@setOnEditorActionListener false
            }

            editText2.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    editText2.setSelection(editText2.text!!.length)
                }
            }

            editText2.setOnKeyListener { _, keyCode, event ->
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN) {
                    val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                    inputMethodManager.hideSoftInputFromWindow(editText2.windowToken, 0)
                    editText2.clearFocus()
                    true
                } else {
                    false
                }

            }
        }
        listenToggleGroups()
        observeViewModel()
        viewModel.loadSettings(appPref)

    }



    private fun observeViewModel() {
        viewModel.apply {
            timeSetting.observe(this@HomePageActivity) { timeSetting ->
                timeSetting?.let {
                    when (it) {
                        "5" -> firsTeamToggleButtonListener(binding.button1)
                        "90" -> firsTeamToggleButtonListener(binding.button2)
                        "120" -> firsTeamToggleButtonListener(binding.button3)
                    }
                }
            }
            passSetting.observe(this@HomePageActivity) { passSetting ->
                passSetting?.let {
                    when (it) {
                        "5" -> firsTeamToggleButtonListener(binding.button4)
                        "10" -> firsTeamToggleButtonListener(binding.button5)
                        "15" -> firsTeamToggleButtonListener(binding.button6)
                    }
                }
            }
            finishScoreSetting.observe(this@HomePageActivity) { finishScoreSetting ->
                finishScoreSetting?.let {
                    when (it) {
                        "10" -> firsTeamToggleButtonListener(binding.button7)
                        "16" -> firsTeamToggleButtonListener(binding.button8)
                        "24" -> firsTeamToggleButtonListener(binding.button9)
                    }
                }
            }
            firstTeamSetting.observe(this@HomePageActivity) { firstTeamSetting ->
                firstTeamSetting?.let {
                    when (it) {
                        "1" -> firsTeamToggleButtonListener(binding.button10)
                        "2" -> firsTeamToggleButtonListener(binding.button11)
                    }
                }
            }

            teamOneSetting.observe(this@HomePageActivity) { teamOneSetting ->
                teamOneSetting?.let { it ->
                    binding.editText1.setText(it)
                    Log.d(TAG, "observeViewModel: it")
                }
            }
            teamTwoSetting.observe(this@HomePageActivity) { teamTwoSetting ->
                teamTwoSetting?.let { it ->
                    binding.editText2.setText(it)
                    Log.d(TAG, "observeViewModel: it")

                }
            }

        }

    }

    private fun listenToggleGroups() {
        binding.apply {
            toggleButton.addOnButtonCheckedListener { group, checkedId, isChecked ->
                if (isChecked) {
                    when (checkedId) {
                        R.id.button1 -> {
                            timeToggleButtonListener(button1)
                            viewModel.setTime("60", appPref)

                        }

                        R.id.button2 -> {
                            timeToggleButtonListener(button2)
                            viewModel.setTime("90", appPref)


                        }

                        R.id.button3 -> {
                            timeToggleButtonListener(button3)
                            viewModel.setTime("120", appPref)


                        }
                    }
                }
            }
            toggleButton2.addOnButtonCheckedListener { group, checkedId, isChecked ->
                if (isChecked) {
                    when (checkedId) {
                        R.id.button4 -> {
                            passToggleButtonListener(button4)
                            viewModel.setPassSetting("5", appPref)

                        }

                        R.id.button5 -> {
                            passToggleButtonListener(button5)
                            viewModel.setPassSetting("10", appPref)


                        }

                        R.id.button6 -> {
                            passToggleButtonListener(button6)
                            viewModel.setPassSetting("15", appPref)


                        }
                    }
                }
            }
            toggleButton3.addOnButtonCheckedListener { group, checkedId, isChecked ->
                if (isChecked) {
                    when (checkedId) {
                        R.id.button7 -> {
                            scoreToggleButtonListener(button7)
                            viewModel.setFinishScoreSetting("10", appPref)

                        }

                        R.id.button8 -> {
                            scoreToggleButtonListener(button8)
                            viewModel.setFinishScoreSetting("16", appPref)


                        }

                        R.id.button9 -> {
                            scoreToggleButtonListener(button9)
                            viewModel.setFinishScoreSetting("24", appPref)


                        }
                    }
                }
            }
            toggleButton4.addOnButtonCheckedListener { group, checkedId, isChecked ->
                if (isChecked) {
                    when (checkedId) {
                        R.id.button10 -> {
                            firsTeamToggleButtonListener(button10)
                            viewModel.setFirstTeamSetting("1", appPref)

                        }

                        R.id.button11 -> {
                            firsTeamToggleButtonListener(button11)
                            viewModel.setFirstTeamSetting("2", appPref)

                        }
                    }
                }
            }

        }
    }

    private fun firsTeamToggleButtonListener(activeButton: Button) {
        binding.apply {
            button10.setBackgroundColor(getColor(R.color.primaryContainer3))
            button11.setBackgroundColor(getColor(R.color.primaryContainer3))
        }
        activeButton.setBackgroundColor(getColor(R.color.primaryContainer))
    }

    private fun timeToggleButtonListener(activeButton: Button) {
        binding.apply {
            button1.setBackgroundColor(getColor(R.color.primaryContainer3))
            button2.setBackgroundColor(getColor(R.color.primaryContainer3))
            button3.setBackgroundColor(getColor(R.color.primaryContainer3))
        }
        activeButton.setBackgroundColor(getColor(R.color.primaryContainer))
    }

    private fun passToggleButtonListener(activeButton: Button) {
        binding.apply {
            button4.setBackgroundColor(getColor(R.color.primaryContainer3))
            button5.setBackgroundColor(getColor(R.color.primaryContainer3))
            button6.setBackgroundColor(getColor(R.color.primaryContainer3))
        }
        activeButton.setBackgroundColor(getColor(R.color.primaryContainer))
    }

    private fun scoreToggleButtonListener(activeButton: Button) {
        binding.apply {
            button7.setBackgroundColor(getColor(R.color.primaryContainer3))
            button8.setBackgroundColor(getColor(R.color.primaryContainer3))
            button9.setBackgroundColor(getColor(R.color.primaryContainer3))
        }
        activeButton.setBackgroundColor(getColor(R.color.primaryContainer))
    }


    override fun onResume() {
        super.onResume()
    }
}