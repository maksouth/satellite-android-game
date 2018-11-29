package name.mharbovskyi.satellitegame.presentation.play

import name.mharbovskyi.satellitegame.domain.entity.Location
import name.mharbovskyi.satellitegame.domain.entity.ObjectState
import name.mharbovskyi.satellitegame.domain.entity.Planet
import name.mharbovskyi.satellitegame.presentation.BasePresenter

interface PlayContract {
    interface View {
        fun drawPlanet(planet: Planet)
        fun drawTrajectoryHint(trajectory: List<Location>)
        fun drawNewSatelliteLocation(location: Location)
        fun showCollision(location: Location)
        fun showFinish(location: Location)
    }

    interface Presenter : BasePresenter {
        fun load()
        fun start(satellite: ObjectState)
        fun stop()
        fun requestTrajectoryHint(satellite: ObjectState)
    }
}
