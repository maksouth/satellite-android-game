package name.mharbovskyi.satellitegame.presentation.play

import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import name.mharbovskyi.satellitegame.domain.CoordinateTransformer
import name.mharbovskyi.satellitegame.domain.entity.Location
import name.mharbovskyi.satellitegame.domain.entity.ObjectState
import name.mharbovskyi.satellitegame.domain.entity.Planet
import name.mharbovskyi.satellitegame.domain.scaledTimer
import name.mharbovskyi.satellitegame.domain.usecase.*
import name.mharbovskyi.satellitegame.presentation.CoroutinePresenter

class PlayPresenter(
    private val view: PlayContract.View,
    private val planet: Planet,
    private val getTrajectoryHintUsecase: GetTrajectoryHintUsecase,
    private val getTrajectoryUsecase: GetTrajectoryUsecase,
    private val limitTrajectoryUsecase: LimitTrajectoryUsecase,
    private val timer: ReceiveChannel<Long>,
    private val coordinateTransformer: CoordinateTransformer
): PlayContract.Presenter, CoroutinePresenter() {

    private var satelliteMoveJob: Job? = null

    override fun load() {
        view.drawPlanet(coordinateTransformer.transformPlanet(planet))
    }

    override fun start(satellite: ObjectState) {

        val trajectory = getTrajectoryUsecase.get(satellite)
        val limitedTrajectory = limitTrajectoryUsecase.detect(trajectory)

        satelliteMoveJob = launch {
            val timerIterator = timer.iterator()
            val trajectoryIterator = limitedTrajectory
                .iterator()

            while (trajectoryIterator.hasNext()) {
                view.drawNewSatelliteLocation(trajectoryIterator.next().location.converted())
                delay( 1000 / 60)
            }

            val lastPoint = limitedTrajectory.last()
            when(lastPoint) {
                is Collision -> view.showCollision(lastPoint.location.converted())
                is Finish -> view.showFinish(lastPoint.location.converted())
            }
        }
    }

    override fun stop() {
        satelliteMoveJob?.cancel()
    }

    override fun requestTrajectoryHint(satellite: ObjectState) {
        val trajectoryHint = getTrajectoryHintUsecase.get(satellite)
        view.drawTrajectoryHint(trajectoryHint.toList())
    }

    private fun Location.converted() =
            coordinateTransformer.transformLocation(this)
}