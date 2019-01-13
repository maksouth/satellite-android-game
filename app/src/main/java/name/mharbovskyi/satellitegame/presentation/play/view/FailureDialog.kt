package name.mharbovskyi.satellitegame.presentation.play.view

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.view.Window
import kotlinx.android.synthetic.main.failure_dialog.*
import name.mharbovskyi.satellitegame.R

class FailureDialog(activity: Activity)
    : Dialog(activity)
{
    private var listener: (() -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.failure_dialog)

        retry_button.setOnClickListener{
            listener?.invoke()
        }
    }

    fun setListener(listener: () -> Unit) {
        this.listener = listener
    }
}