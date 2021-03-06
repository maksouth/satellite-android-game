package name.mharbovskyi.satellitegame.presentation.start

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_start.*
import name.mharbovskyi.satellitegame.R
import name.mharbovskyi.satellitegame.presentation.play.view.PlayActivity

class StartActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        start_button.setOnClickListener {
            startActivity(Intent(this, PlayActivity::class.java))
        }
    }
}
