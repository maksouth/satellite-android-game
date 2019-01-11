package name.mharbovskyi.satellitegame.presentation.play

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import name.mharbovskyi.satellitegame.domain.CoordinateTransformer
import name.mharbovskyi.satellitegame.domain.entity.*
import name.mharbovskyi.satellitegame.domain.entity.TargetSpot
import name.mharbovskyi.satellitegame.domain.usecase.LimitedTrajectoryUsecase
import name.mharbovskyi.satellitegame.presentation.CoroutinePresenter

class PlayPresenter(
    private var view: PlayContract.View?,
    private var baseSatellite: ObjectState,
    private val planetSystem: PlanetSystem,
    private val target: TargetSpot,
    private val limitedTrajectoryUsecase: LimitedTrajectoryUsecase,
    private val coordinateTransformer: CoordinateTransformer,
    private val frameDelay: Long

): PlayContract.Presenter, CoroutinePresenter() {

    private var startSatelliteJob: Job? = null

    private var trajectory: Sequence<Pair<PlanetSystem, ObjectState>>? = null

    private var isRunning = true

    override fun load() {
        val scaledTarget = coordinateTransformer.transformTarget(target)
        val (scaledPlanets, scaledSatellite) =
                scaleForScreen(planetSystem to baseSatellite)

        scaledPlanets.showPlanets()
        post { view?.showTarget(scaledTarget) }
        post { view?.showSatellite(scaledSatellite.location) }
    }

    override fun start(speedXTimes: Double, speedYTimes: Double) {

        isRunning = true

        val speededSatellite = baseSatellite.copy(
            speed = Speed(
                speedXTimes * baseSatellite.speed.x,
                speedYTimes * baseSatellite.speed.y
            )
        )

        trajectory = limitedTrajectoryUsecase.build(
            planetSystem,
            speededSatellite,
            target.copy(radius = 1.5 * target.radius),
            finishListener = ::showFinish,
            collisionListener = ::showCollision
        ).map( ::scaleForScreen )

        startSatelliteJob = launch {

            trajectory?.takeWhile { isRunning }
                ?.forEach {(planetSystem, satellite) ->
                    planetSystem.showPlanets()
                    post { view?.showSatellite(satellite.location) }

                    delay(frameDelay)
                }
        }
    }

    override fun restart() {
        isRunning = false
        load()
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