package name.mharbovskyi.satellitegame.presentation.start

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import kotlinx.android.synthetic.main.activity_start.*
import name.mharbovskyi.satellitegame.R
import name.mharbovskyi.satellitegame.domain.entity.*
import name.mharbovskyi.satellitegame.presentation.play.PlayActivity

class StartActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        start_button.setOnClickListener {
            startActivity(Intent(this, PlayActivity::class.java))
        }
    }
}
