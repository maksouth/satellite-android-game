package name.mharbovskyi.satellitegame.presentation.play

import android.arch.lifecycle.MutableLiveData
import android.util.Log
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import name.mharbovskyi.satellitegame.domain.ScaledValues
import name.mharbovskyi.satellitegame.domain.entity.Location
import name.mharbovskyi.satellitegame.domain.entity.ObjectState
import name.mharbovskyi.satellitegame.domain.entity.Planet
import name.mharbovskyi.satellitegame.domain.intervalTimer
import name.mharbovskyi.satellitegame.presentation.BaseViewModel
import name.mharbovskyi.satellitegame.presentation.ViewState

class PlayViewModel(
    private var satellite: ObjectState, //optional, should be passed to start
    private val scaledValues: ScaledValues,
    private val planet: Planet,
    private val trajectoryBuilder: (ObjectState, Double) -> Sequence<ObjectState>,
    locationScalerFactory: (ScaledValues) -> (Location) -> Location
): BaseViewModel() {

    val locationScaler = locationScalerFactory(scaledValues)
    val satellitePosition: MutableLiveData<ViewState<Location>> = MutableLiveData()
    private var satelliteLocationJob: Job? = null
    private val timer = intervalTimer(1000 / scaledValues.fps.toLong())

    fun start() {
        satelliteLocationJob = launch {

            val stateSequence = trajectoryBuilder(satellite, scaledValues.calculationStep)
                .filterIndexed{ count, _ -> count % scaledValues.frameDropRate == 0 }
                .iterator()

            for (time in timer) {
                val location = stateSequence.next().location
                val scaledLocation = locationScaler(location)
                satellitePosition.postValue(ViewState.success(scaledLocation))
                //Log.d("PlayViewModel", "new state: $location")
            }
        }
    }

    fun stop() {
        satelliteLocationJob?.cancel()
    }
}

