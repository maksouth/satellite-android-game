package name.mharbovskyi.satellitegame.presentation.play.view

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.view.Window
import android.widget.Toast
import name.mharbovskyi.satellitegame.R

class FinishSuccessDialog(activity: Activity)
    : Dialog(activity)
{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.finish_dialog)

        finish_button.setOnClickListener{
            Toast.makeText(this, "Not ready yet", Toast.LENGTH_SHORT).show()
        }
    }
}