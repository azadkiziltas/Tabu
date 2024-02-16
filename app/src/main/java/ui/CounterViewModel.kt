package ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.Timer
import java.util.TimerTask

class CounterViewModel(private val startingNumber: Int) : ViewModel() {

    private val countLiveData = MutableLiveData<Int>()
    private var count = startingNumber
    private var timer: Timer? = null
    private var timerTask: TimerTask? = null
    private var paused = false
    private var remainingTime: Long = 0L
    private val interval: Long = 1000 // 1 saniye

    init {
        countLiveData.value = count
    }

    fun getCount(): LiveData<Int> {
        return countLiveData
    }

    fun startTimer() {
        if (timer == null) {
            timer = Timer()
            timerTask = object : TimerTask() {
                override fun run() {
                    count--
                    countLiveData.postValue(count)
                    if (count == 0) {
                        stopTimer()
                    }
                }
            }
            timer?.scheduleAtFixedRate(timerTask, interval, interval)
        } else if (paused) {
            timer = Timer()
            timerTask = object : TimerTask() {
                override fun run() {
                    count--
                    countLiveData.postValue(count)
                    if (count == 0) {
                        stopTimer()
                    }
                }
            }
            timer?.scheduleAtFixedRate(timerTask, remainingTime, interval)
            paused = false
        }
    }

    fun pauseTimer() {
        timerTask?.cancel()
        timer?.cancel()
        paused = true
    }

    fun resumeTimer() {
        if (paused) {
            startTimer()
        }
    }

    fun stopTimer() {
        timerTask?.cancel()
        timer?.cancel()
        timer = null
        count = startingNumber
        countLiveData.postValue(count)
    }
}