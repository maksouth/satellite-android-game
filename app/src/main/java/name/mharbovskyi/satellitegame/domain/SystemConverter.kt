package name.mharbovskyi.satellitegame.domain

import name.mharbovskyi.satellitegame.domain.physics.measurement.MeasurementSystem
import kotlin.math.roundToInt

data class ScaledValues(
    val calculationStep: Double,
    val coordinateScale: Double,
    val frameDropRate: Int,
    val fps: Int,
    val trajectoryFrames: Int
)

data class ScreenSize(
    val height: Int,
    val width: Int
)

fun scaledValuesFor(
    measurementSystem: MeasurementSystem,
    screenSize: ScreenSize,
    framesRate: Int = 60,
    trajectoryFrames: Int = 10_000,
    scaledPeriod: Double = 4.0 // how long satellite will actually rotate around medium normal weight planet
): ScaledValues {
    val step = measurementSystem.period / trajectoryFrames
    val scale = 12 * measurementSystem.planet.radius.medium / screenSize.height
    val dropRate = trajectoryFrames / framesRate * scaledPeriod * 1.5

    return ScaledValues(
        step,
        scale,
        dropRate.roundToInt(),
        framesRate,
        trajectoryFrames
    )
}