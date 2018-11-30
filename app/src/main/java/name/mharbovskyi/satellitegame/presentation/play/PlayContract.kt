package name.mharbovskyi.satellitegame.presentation.play

import name.mharbovskyi.satellitegame.domain.entity.Location
import name.mharbovskyi.satellitegame.domain.entity.Planet
import name.mharbovskyi.satellitegame.domain.entity.Target
import name.mharbovskyi.satellitegame.presentation.BasePresenter
import name.mharbovskyi.satellitegame.presentation.BaseView

interface PlayContract {

    interface Presenter: BasePresenter {
        fun load()
        fun start(speedXTimes: Double, speedYTimes: Double)
        fun stop()
    }

    interface View: BaseView {
        fun showTarget(target: Target)
        fun showPlanet(planet: Planet)
        fun showSatellite(satellite: Location)
        fun showCollision(location: Location)
        fun showFinish(location: Location)
    }
}