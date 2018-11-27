package name.mharbovskyi.satellitegame.presentation.play

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.util.Log
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import name.mharbovskyi.satellitegame.domain.entity.Location
import name.mharbovskyi.satellitegame.domain.entity.ObjectState
import name.mharbovskyi.satellitegame.domain.entity.Planet
import name.mharbovskyi.satellitegame.domain.intervalTimer
import name.mharbovskyi.satellitegame.presentation.BaseViewModel
import name.mharbovskyi.satellitegame.presentation.ViewState

class PlayViewModel(
    private var satellite: ObjectState,
    private val planet: Planet,
    private val trajectoryBuilder: (ObjectState, Double) -> Sequence<ObjectState>,
    private val locationScaler: (Location) -> Location
): BaseViewModel() {

    val satellitePosition: MutableLiveData<ViewState<Location>> = MutableLiveData()
    private var satelliteLocationJob: Job? = null
    private val timer = intervalTimer(50)

    fun start() = launch {
        satelliteLocationJob = launch {

            val stateSequence = trajectoryBuilder(satellite, 0.001)
                .filterIndexed{ count, _ -> count % 100 == 0 }
                .iterator()

            for (time in timer) {
                val location = stateSequence.next().location
                val scaledLocation = locationScaler(location)
                satellitePosition.postValue(ViewState.success(scaledLocation))
                Log.d("PlayViewModel", "new state: $location")
            }
        }
    }

    fun stop() {
        satelliteLocationJob?.cancel()
    }
}

