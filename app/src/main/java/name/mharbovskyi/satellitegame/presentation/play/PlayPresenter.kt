package name.mharbovskyi.satellitegame.presentation.play

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import name.mharbovskyi.satellitegame.domain.CoordinateTransformer
import name.mharbovskyi.satellitegame.domain.entity.*
import name.mharbovskyi.satellitegame.domain.entity.Target
import name.mharbovskyi.satellitegame.domain.usecase.LimitedTrajectoryUsecase
import name.mharbovskyi.satellitegame.presentation.CoroutinePresenter
import kotlin.system.measureTimeMillis

class PlayPresenter(
    private var view: PlayContract.View?,
    private val baseSatellite: ObjectState,
    private val planetSystem: PlanetSystem,
    private val target: Target,
    private val limitedTrajectoryUsecase: LimitedTrajectoryUsecase,
    private val coordinateTransformer: CoordinateTransformer,
    private val frameDelay: Long

): PlayContract.Presenter, CoroutinePresenter() {

    private var startSatelliteJob: Job? = null

    override fun load() {
        planetSystem.showPlanets()
        post { view?.showTarget(target) }
        post { view?.showSatellite(baseSatellite.location) }
    }

    override fun start(speedXTimes: Double, speedYTimes: Double) {

        val speededSatellite = baseSatellite.copy(
            speed = Speed(
                speedXTimes * baseSatellite.speed.x,
                speedYTimes * baseSatellite.speed.y
            )
        )

        val trajectory = limitedTrajectoryUsecase.build(
            planetSystem,
            speededSatellite,
            target,
            finishListener = ::showFinish,
            collisionListener = ::showCollision
        ).map( ::scaleForScreen )

        startSatelliteJob = launch {
            var frameComputeTime: Long

            for ((planetSystem, satellite) in trajectory) {
                frameComputeTime = measureTimeMillis {
                    planetSystem.showPlanets()
                    post { view?.showSatellite(satellite.location) }
                }

                delay(frameDelay - frameComputeTime)
            }
        }
    }

    private fun showCollision(location: Location) = post { view?.showCollision(location) }

    private fun showFinish(location: Location) = post { view?.showFinish(location) }

    override fun stop() {
        startSatelliteJob?.cancel()
    }

    override fun destroy() {
        super.destroy()
        view = null
    }

    private fun scaleForScreen(
        trajectoryPoint: Pair<PlanetSystem, ObjectState>
    ): Pair<PlanetSystem, ObjectState> {
        val scaledPlanets = planetSystem.planets
            .map { coordinateTransformer.transformPlanet(it) }
        val scaledSatellite = trajectoryPoint.second
            .copy(location = coordinateTransformer.transformLocation(trajectoryPoint.second.location))
        return PlanetSystem(scaledPlanets) to scaledSatellite
    }

    private inline fun post(crossinline block: () -> Unit) {
        launch(Dispatchers.Main) {
            block()
        }
    }

    private fun PlanetSystem.showPlanets() =
        planets.forEach {planet ->  post { view?.showPlanet(planet) } }

}