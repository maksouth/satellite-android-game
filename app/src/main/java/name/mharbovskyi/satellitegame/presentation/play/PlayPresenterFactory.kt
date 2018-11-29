package name.mharbovskyi.satellitegame.presentation.play

import name.mharbovskyi.satellitegame.domain.*
import name.mharbovskyi.satellitegame.domain.entity.Location
import name.mharbovskyi.satellitegame.domain.entity.Planet
import name.mharbovskyi.satellitegame.domain.physics.measurement.FirstMeasurementSystem
import name.mharbovskyi.satellitegame.domain.usecase.GetTrajectoryHintUsecase
import name.mharbovskyi.satellitegame.domain.usecase.GetTrajectoryUsecase
import name.mharbovskyi.satellitegame.domain.usecase.LimitTrajectoryUsecase
import kotlin.math.sqrt

object PlayPresenterFactory{

    fun create(view: PlayContract.View, width: Int, height: Int): PlayContract.Presenter {

        val screenSize = ScreenSize(height, width)
        val measurementSystem = FirstMeasurementSystem
        val scaledValues = scaledValuesFor(measurementSystem, screenSize)
        val coordinateTransformer = ScreenCoordinateTransformer(
            scale = scaledValues.coordinateScale,
            width = width,
            height = height
        )

        val planet = Planet(
            "Ventura",
            measurementSystem.planet.weight.heavy,
            measurementSystem.planet.radius.medium,
            Location(0.0, 0.0)
        )

        val trajectoryBuilder = trajectorySequenceBuilder(planet, measurementSystem.g, scaledValues.calculationStep)

        val getTrajectoryHintUsecase = GetTrajectoryHintUsecase(
            scaledValues = scaledValues,
            trajectoryBuilder = trajectoryBuilder
        )

        val getTrajectoryUsecase = GetTrajectoryUsecase(
            scaledValues = scaledValues,
            trajectoryBuilder = trajectoryBuilder
        )


        val target = Location(planet.radius / sqrt(2.0), planet.radius / sqrt(2.0))
        val limitTrajectoryUsecase = LimitTrajectoryUsecase(
            hasCollision = implicitHasCollisionFactory(planet),
            isFinish = implicitIsFinishFactory(target, planet.radius / 10)
        )

        return PlayPresenter(
            view = view,
            planet = planet,
            getTrajectoryHintUsecase = getTrajectoryHintUsecase,
            getTrajectoryUsecase = getTrajectoryUsecase,
            limitTrajectoryUsecase = limitTrajectoryUsecase,
            timer = tick( 1000 / scaledValues.fps.toLong() ),
            coordinateTransformer = coordinateTransformer
            )
    }
}