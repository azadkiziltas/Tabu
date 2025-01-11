package ui.popup

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.PopupWindow
import android.widget.TextView
import com.redstoneapps.tabu.R


class ChangeTeamPopup(
    val context: Context,
    val currentTeam: String,
    val teamOne: String,
    val teamTwo: String,
    private val listener: OnButtonClickListener,
    ) {

    interface OnButtonClickListener {
        fun onStartButtonClick()
    }

    private val popupView: View
    private val popupWindow: PopupWindow

    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        popupView = inflater.inflate(R.layout.game_start_popup, null)

        popupWindow = PopupWindow(
            popupView,
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )

        // Arka planı saydam yap
        popupWindow.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val btnStart = popupView.findViewById<Button>(R.id.gameStartButton)
        val textViewTeam = popupView.findViewById<TextView>(R.id.textViewTeam)

        btnStart.setOnClickListener {
            listener.onStartButtonClick()
            dismiss()
        }
        if (currentTeam.equals("1")) {
            textViewTeam.text = teamOne
        }
        else{
            textViewTeam.text = teamTwo
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
