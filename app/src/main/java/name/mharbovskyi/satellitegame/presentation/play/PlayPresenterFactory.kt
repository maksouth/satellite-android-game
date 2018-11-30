package name.mharbovskyi.satellitegame.presentation.play

import name.mharbovskyi.satellitegame.domain.CompletionTrackerImpl
import name.mharbovskyi.satellitegame.domain.ScreenCoordinateTransformer
import name.mharbovskyi.satellitegame.domain.entity.*
import name.mharbovskyi.satellitegame.domain.entity.Target
import name.mharbovskyi.satellitegame.domain.physics.calculation.primaryOrbitSpeed
import name.mharbovskyi.satellitegame.domain.physics.measurement.FirstMeasurementSystem
import name.mharbovskyi.satellitegame.domain.ScreenSize
import name.mharbovskyi.satellitegame.domain.TrajectoryBuilderImpl
import name.mharbovskyi.satellitegame.domain.physics.SinglePlanetStateTransformer
import name.mharbovskyi.satellitegame.domain.scaledValuesFor
import name.mharbovskyi.satellitegame.domain.usecase.LimitedTrajectoryUsecase

object PlayPresenterFactory {
    fun build(view: PlayContract.View, width: Int, height: Int): PlayContract.Presenter {

        val measurementSystem = FirstMeasurementSystem
        val scaledValues = scaledValuesFor(measurementSystem, ScreenSize(height, width))

        val planet = Planet(
            "Ventura",
            measurementSystem.planet.weight.heavy,
            measurementSystem.planet.radius.medium,
            Location(0.0, 0.0)
        )

        val planetSystem = PlanetSystem(listOf(planet))

        val target = Target(
            Location(0.0, 10*planet.radius),
            planet.radius / 10
        )

        val speed = primaryOrbitSpeed(planet, measurementSystem.g)
        val satellite = ObjectState(
            Speed(speed, 0.0),
            Acceleration(0.0, 0.0),
            Location(0.0, 2 * planet.radius)
        )

        val stateTransformer = SinglePlanetStateTransformer(measurementSystem.g)

        val trajectoryBuilder = TrajectoryBuilderImpl(stateTransformer)

        val completionTracker = CompletionTrackerImpl()

        val limitedTrajectoryUsecase = LimitedTrajectoryUsecase(
            trajectoryBuilder,
            completionTracker,
            scaledValues.frameDropRate,
            scaledValues.calculationStep
        )

        val coordinateTransformer = ScreenCoordinateTransformer(
            scaledValues.coordinateScale,
            width,
            height
        )

        return PlayPresenter(
            view,
            satellite,
            planetSystem,
            target,
            limitedTrajectoryUsecase,
            coordinateTransformer,
            frameDelay = 1000 / scaledValues.fps.toLong()
        )
    }
}