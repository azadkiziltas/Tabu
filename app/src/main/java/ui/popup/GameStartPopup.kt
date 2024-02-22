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
import android.widget.Toast
import com.redstoneapps.tabu.R


class GameStartPopup(
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


//class GameStartPopup(context: Context,
//                     val firstTeamSetting: String, val teamOneSetting:String, val teamTwoSetting:String, private val listener: OnButtonClickListener) : Dialog(context) {
//
//    interface OnButtonClickListener {
//        fun onOkButtonClick()
//    }
//
//    @SuppressLint("MissingInflatedId")
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        requestWindowFeature(Window.FEATURE_NO_TITLE)
//        window?.setBackgroundDrawableResource(android.R.color.transparent)
//        window?.setLayout(
//            ViewGroup.LayoutParams.MATCH_PARENT,
//            ViewGroup.LayoutParams.MATCH_PARENT
//        )
//        window?.setFlags(
//            WindowManager.LayoutParams.FLAG_FULLSCREEN,
//            WindowManager.LayoutParams.FLAG_FULLSCREEN
//        )
//
//        // Sistem gezinme çubuğunu gizle
//        window?.decorView?.systemUiVisibility = (
//                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                        or View.SYSTEM_UI_FLAG_FULLSCREEN
//                        or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                )
//
//        setContentView(R.layout.game_start_popup)
//
//        val btnStart = findViewById<Button>(R.id.startButton)
//        val textViewTeam = findViewById<TextView>(R.id.textViewTeam)
//        setCanceledOnTouchOutside(false)
//
//
//
//        btnStart.setOnClickListener {
//            listener.onOkButtonClick()
//            dismiss()
//        }
//
//        if (firstTeamSetting.equals("1")){
//            textViewTeam.text = teamOneSetting
//        }
//        else{
//            textViewTeam.text = teamTwoSetting
//        }
//    }
//}