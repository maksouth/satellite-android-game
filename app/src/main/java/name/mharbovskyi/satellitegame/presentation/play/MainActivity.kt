package name.mharbovskyi.satellitegame.presentation.play

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.DisplayMetrics
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import name.mharbovskyi.satellitegame.R
import name.mharbovskyi.satellitegame.domain.entity.Location
import name.mharbovskyi.satellitegame.domain.entity.Planet
import name.mharbovskyi.satellitegame.domain.entity.Target

class MainActivity : AppCompatActivity(), PlayContract.View {

    private val tag = MainActivity::class.java.simpleName.toString()

    lateinit var presenter: PlayContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)

        val height = displayMetrics.heightPixels
        val width = displayMetrics.widthPixels

        presenter = PlayPresenterFactory.build(this, width, height)
        presenter.load()
    }

    override fun onStart() {
        super.onStart()
        presenter.start(1.0, 1.0)
    }

    override fun onStop() {
        super.onStop()
        presenter.stop()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.destroy()
    }

    override fun showPlanet(planet: Planet) {
        humongous_doge.x = planet.location.x.toFloat() + humongous_doge.width / 2
        humongous_doge.y = planet.location.y.toFloat() + humongous_doge.height / 2
    }

    override fun showSatellite(satellite: Location) {
        doge.x = satellite.x.toFloat() + doge.width / 2
        doge.y = satellite.y.toFloat() + doge.height / 2
    }

    override fun showTarget(target: Target) {
        target_star.x = target.location.x.toFloat() + target_star.width / 2
        target_star.y = target.location.y.toFloat() + target_star.height / 2
    }

    override fun showCollision(location: Location) {
        Toast.makeText(this, "Collision", Toast.LENGTH_SHORT).show()
    }

    override fun showFinish(location: Location) {
        Toast.makeText(this, "Finish", Toast.LENGTH_SHORT).show()
    }

    override fun hideLoading() {
        Toast.makeText(this, "Loading hidden", Toast.LENGTH_LONG).show()
    }

    override fun showLoading() {
        Toast.makeText(this, "Loading shown", Toast.LENGTH_LONG).show()
    }
}
