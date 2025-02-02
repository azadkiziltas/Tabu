package ui.popup

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.PopupWindow
import android.widget.TextView
import com.redstoneapps.tabu.R



class GameFinishPopup(val context: Context,val winnerTeam:String, private val listener: OnButtonClickListener) {

    interface OnButtonClickListener {
        fun onStartButtonClick()
        fun onHomePageButtonClick()
    }

    private val popupView: View
    private val popupWindow: PopupWindow

    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        popupView = inflater.inflate(R.layout.game_finish_popup, null)

        popupWindow = PopupWindow(
            popupView,
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )

        // Arka planı saydam yap
        popupWindow.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val btnStart = popupView.findViewById<Button>(R.id.startButton)
        val btnHomePage = popupView.findViewById<Button>(R.id.homePageButton)
        val textViewTeam = popupView.findViewById<TextView>(R.id.textViewWinnerTeam)

        textViewTeam.text = "KAZANAN TAKIM\n$winnerTeam"




        btnStart.setOnClickListener {
            listener.onStartButtonClick()
            dismiss()
        }

        btnHomePage.setOnClickListener {
            listener.onHomePageButtonClick()
            dismiss()
        }
    }

    fun showAtCenter() {
        val parentView = (context as Activity).window.decorView
        popupWindow.showAtLocation(parentView, Gravity.CENTER, 0, 0)
    }

    fun dismiss() {
        popupWindow.dismiss()
    }
}

