package name.mharbovskyi.satellitegame.presentation.play

import android.view.MotionEvent
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

    override fun start() {

        isRunning = true

        view?.clearTrajectoryHint()

        val speededSatellite = baseSatellite.copy(
            speed = Speed(
                speedXProjection * baseSatellite.speed.x,
                speedYProjection * baseSatellite.speed.y
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
        view?.clearTrajectoryTail()
        load()
    }

    var isMoving: Boolean = false
    var speedXProjection: Double = 0.0
    var speedYProjection: Double = 1.0
    override fun onTouchEvent(event: MotionEvent) {
        val x = event.x
        val y = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                isMoving = checkIfTouchedSatellite(x, y)
            }
            MotionEvent.ACTION_MOVE -> {
                if (isMoving) {
                    val (xProjection, yProjection) = calculateSpeedProjections(x, y)
                    val satelliteHint = baseSatellite.copy(
                        speed = Speed(
                            xProjection * baseSatellite.speed.x,
                            yProjection * baseSatellite.speed.y
                        ))
                    showTrajectoryHint(satelliteHint)
                }
            }
            MotionEvent.ACTION_UP -> {
                if(isMoving) {
                    val (xProjection, yProjection) = calculateSpeedProjections(x, y)
                    speedXProjection = xProjection
                    speedYProjection = yProjection

                    start()
                    isMoving = false
                }
            }
        }
    }

    private fun checkIfTouchedSatellite(x: Float, y: Float): Boolean {
        val screenSatellite = coordinateTransformer.transformLocation(baseSatellite.location)

        return screenSatellite.distanceTo(Location(x.toDouble(), y.toDouble())) < 10
    }

    private fun calculateSpeedProjections(xEndTouch: Float, yEndTouch: Float): Pair<Double, Double> {
        val screenSatellite = coordinateTransformer.transformLocation(baseSatellite.location)

        val primarySpeedProjection = 200

        return (screenSatellite.x - xEndTouch) / primarySpeedProjection to
                (screenSatellite.y - yEndTouch) / primarySpeedProjection
    }

    private fun showTrajectoryHint(satellite: ObjectState) {
        val trajectory = limitedTrajectoryUsecase.build(
            planetSystem,
            satellite,
            target.copy(radius = 1.5 * target.radius))
            .filterIndexed{ index, _ -> index % 20 == 0}
            .take(10)
            .map( ::scaleForScreen )
            .map { (_, satellite) ->  satellite.location }
            .toList()

        view?.showTrajectoryHint(trajectory)
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