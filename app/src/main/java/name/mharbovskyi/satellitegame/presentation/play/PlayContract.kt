package name.mharbovskyi.satellitegame.presentation.play

import android.view.MotionEvent
import name.mharbovskyi.satellitegame.domain.entity.Location
import name.mharbovskyi.satellitegame.domain.entity.Planet
import name.mharbovskyi.satellitegame.domain.entity.TargetSpot
import name.mharbovskyi.satellitegame.presentation.BasePresenter
import name.mharbovskyi.satellitegame.presentation.BaseView

interface PlayContract {

    interface Presenter: BasePresenter {
        fun load()
        fun start()
        fun stop()
        fun restart()
        fun onTouchEvent(event: MotionEvent)
    }

    interface View: BaseView {
        fun showTarget(target: TargetSpot)
        fun showPlanet(planet: Planet)
        fun showSatellite(satellite: Location)
        fun showCollision(location: Location)
        fun showFinish(location: Location)
        fun clearTrajectoryTail()
        fun showTrajectoryHint(trajectory: List<Location>)
        fun clearTrajectoryHint()
    }
}