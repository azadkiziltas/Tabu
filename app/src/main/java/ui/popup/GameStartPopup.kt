package ui.popup

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.redstoneapps.tabu.R

class GameStartPopup(context: Context,
                     val firstTeamSetting: String, val teamOneSetting:String, val teamTwoSetting:String, private val listener: OnButtonClickListener) : Dialog(context) {

    interface OnButtonClickListener {
        fun onOkButtonClick()
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window?.setBackgroundDrawableResource(android.R.color.transparent)
        window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        window?.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        // Sistem gezinme çubuğunu gizle
        window?.decorView?.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                )

        setContentView(R.layout.game_start_popup)

        val btnStart = findViewById<Button>(R.id.startButton)
        val textViewTeam = findViewById<TextView>(R.id.textViewTeam)
        setCanceledOnTouchOutside(false)



        // Onay düğmesine tıklama işlemi
        btnStart.setOnClickListener {
            listener.onOkButtonClick()
            dismiss()
        }
        if (firstTeamSetting.equals("1")){
            textViewTeam.text = teamOneSetting
        }
        else{
            textViewTeam.text = teamTwoSetting
        }
    }
}